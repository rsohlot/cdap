/*
 * Copyright © 2023 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.cdap.sourcecontrol.operationrunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.cdap.cdap.common.NotFoundException;
import io.cdap.cdap.common.io.CaseInsensitiveEnumTypeAdapterFactory;
import io.cdap.cdap.common.utils.DirUtils;
import io.cdap.cdap.common.utils.FileUtils;
import io.cdap.cdap.proto.ApplicationDetail;
import io.cdap.cdap.proto.artifact.AppRequest;
import io.cdap.cdap.proto.id.ApplicationReference;
import io.cdap.cdap.sourcecontrol.ApplicationManager;
import io.cdap.cdap.sourcecontrol.AuthenticationConfigException;
import io.cdap.cdap.sourcecontrol.CommitMeta;
import io.cdap.cdap.sourcecontrol.ConfigFileWriteException;
import io.cdap.cdap.sourcecontrol.GitOperationException;
import io.cdap.cdap.sourcecontrol.InvalidApplicationConfigException;
import io.cdap.cdap.sourcecontrol.NoChangesToPushException;
import io.cdap.cdap.sourcecontrol.RepositoryManager;
import io.cdap.cdap.sourcecontrol.RepositoryManagerFactory;
import io.cdap.cdap.sourcecontrol.SecureSystemReader;
import io.cdap.cdap.sourcecontrol.SourceControlAppConfigNotFoundException;
import io.cdap.cdap.sourcecontrol.SourceControlException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-Memory implementation for {@link SourceControlOperationRunner}.
 * Runs all git operation inside calling service.
 */
@Singleton
public class InMemorySourceControlOperationRunner extends
    AbstractIdleService implements SourceControlOperationRunner {
  // Gson for decoding request
  private static final Gson DECODE_GSON =
    new GsonBuilder().registerTypeAdapterFactory(new CaseInsensitiveEnumTypeAdapterFactory()).create();
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Logger LOG = LoggerFactory.getLogger(InMemorySourceControlOperationRunner.class);
  private final RepositoryManagerFactory repoManagerFactory;

  @Inject
  InMemorySourceControlOperationRunner(RepositoryManagerFactory repoManagerFactory) {
    this.repoManagerFactory = repoManagerFactory;
  }

  @Override
  public PushAppResponse push(PushAppOperationRequest pushRequest) throws NoChangesToPushException,
      AuthenticationConfigException {
    try (
        RepositoryManager repositoryManager = repoManagerFactory.create(
            pushRequest.getNamespaceId(),
            pushRequest.getRepositoryConfig())
    ) {
      cloneAndCreateBaseDir(repositoryManager);
      ApplicationDetail app = pushRequest.getApp();
      writeAppDetail(repositoryManager, app);

      List<PushAppResponse> responses = new ArrayList<>(commitAndPush(repositoryManager,
          pushRequest.getCommitDetails(),
          ImmutableList.of(new PushFile(
                  app.getName(),
                  app.getAppVersion(),
                  repositoryManager.getFileRelativePath(generateConfigFileName(app.getName()))
              )
          )
      ));

      // it should never be empty as in case of any error we will get an exception
      return responses.get(0);
    }
  }

  @Override
  public Collection<PushAppResponse> multiPush(MultiPushAppOperationRequest pushRequest,
      ApplicationManager appManager)
      throws NoChangesToPushException, AuthenticationConfigException {
    try (
        RepositoryManager repositoryManager = repoManagerFactory.create(
            pushRequest.getNamespaceId(),
            pushRequest.getRepositoryConfig())
    ) {
      cloneAndCreateBaseDir(repositoryManager);

      LOG.debug("Pushing application configs for {} apps.", pushRequest.getApps().size());

      List<PushFile> filesToPush = new LinkedList<>();

      for (String appToPush : pushRequest.getApps()) {
        LOG.trace("Pushing application configs for : {}.", appToPush);
        ApplicationReference appRef = new ApplicationReference(pushRequest.getNamespaceId(),
            appToPush);
        try {
          ApplicationDetail detail = appManager.get(appRef);
          writeAppDetail(repositoryManager, detail);
          filesToPush.add(new PushFile(detail.getName(), detail.getAppVersion(),
              repositoryManager.getFileRelativePath(generateConfigFileName(detail.getName()))));
        } catch (IOException | NotFoundException e) {
          throw new SourceControlException(
              String.format("Failed to fetch details for app %s", appRef), e);
        }
      }

      return commitAndPush(repositoryManager, pushRequest.getCommitDetails(), filesToPush);
    }
  }

  @Override
  public PullAppResponse<?> pull(PullAppOperationRequest pullRequest)
      throws NotFoundException, AuthenticationConfigException {
    AtomicReference<PullAppResponse<?>> response = new AtomicReference<>();
    multiPull(
        new MultiPullAppOperationRequest(
            pullRequest.getRepositoryConfig(),
            pullRequest.getApp().getNamespaceId(),
            ImmutableSet.of(pullRequest.getApp().getApplication())
        ),
        response::set
    );
    // it should never be null as if the application is not found we will get an exception
    return response.get();
  }

  @Override
  public void multiPull(MultiPullAppOperationRequest pullRequest, Consumer<PullAppResponse<?>> consumer)
      throws SourceControlAppConfigNotFoundException, AuthenticationConfigException {
    LOG.info("Cloning remote to pull applications {}", pullRequest.getApps());

    try (RepositoryManager repositoryManager = repoManagerFactory.create(pullRequest.getNamespace(),
        pullRequest.getRepositoryConfig())) {
      String commitId = repositoryManager.cloneRemote();

      for (String applicationName : pullRequest.getApps()) {
        PullAppResponse<?> response = pullSingle(repositoryManager, commitId, applicationName);
        consumer.accept(response);
      }
    } catch (GitAPIException | IOException e) {
      throw new GitOperationException(
          String.format("Failed to clone repository %s", e.getMessage()), e);
    }
  }

  private PullAppResponse<?> pullSingle(RepositoryManager repositoryManager, String commitId,
      String applicationName)
      throws SourceControlException, SourceControlAppConfigNotFoundException {
    String configFileName = generateConfigFileName(applicationName);
    Path appRelativePath = repositoryManager.getFileRelativePath(configFileName);
    Path filePathToRead = validateAppConfigRelativePath(repositoryManager, appRelativePath);
    if (!Files.exists(filePathToRead)) {
      throw new SourceControlAppConfigNotFoundException(applicationName, appRelativePath);
    }
    LOG.info("Getting file hash for application {}", applicationName);
    try {
      String fileHash = repositoryManager.getFileHash(appRelativePath, commitId);
      String contents = new String(Files.readAllBytes(filePathToRead), StandardCharsets.UTF_8);
      AppRequest<?> appRequest = DECODE_GSON.fromJson(contents, AppRequest.class);

      return new PullAppResponse<>(applicationName, fileHash, appRequest);
    } catch (GitAPIException e) {
      throw new GitOperationException(String.format("Failed to pull application %s: %s",
          applicationName, e.getMessage()), e);
    } catch (JsonSyntaxException e) {
      throw new InvalidApplicationConfigException(appRelativePath, e);
    } catch (NotFoundException e) {
      throw new SourceControlAppConfigNotFoundException(applicationName, appRelativePath);
    } catch (Exception e) {
      throw new SourceControlException(
          String.format("Failed to pull application %s.", applicationName), e);
    }
  }

  private void cloneAndCreateBaseDir(RepositoryManager repositoryManager) {
    try {
      repositoryManager.cloneRemote();
    } catch (GitAPIException | IOException e) {
      throw new GitOperationException("Failed to clone remote repository.", e);
    }
    try {
      // Creates the base directory if it does not exist. This method does not throw an exception if the directory
      // already exists. This is for the case that the repo is new and user configured prefix path.
      Files.createDirectories(repositoryManager.getBasePath());
    } catch (IOException e) {
      throw new SourceControlException("Failed to create repository base directory", e);
    }
  }

  /**
   * Write {@link ApplicationDetail} to config file in git repository.
   *
   * @param repositoryManager {@link RepositoryManager} to conduct git operations
   * @param appToPush application details to write
   * @throws SourceControlException for failures while writing config file or doing git
   *     operations
   */
  private void writeAppDetail(RepositoryManager repositoryManager, ApplicationDetail appToPush) {
    String configFileName = generateConfigFileName(appToPush.getName());

    Path appRelativePath = repositoryManager.getFileRelativePath(configFileName);
    Path filePathToWrite;
    try {
      filePathToWrite = validateAppConfigRelativePath(repositoryManager, appRelativePath);
    } catch (IllegalArgumentException e) {
      throw new SourceControlException(String.format("Failed to push application %s: %s",
                                                     appToPush.getName(),
                                                     e.getMessage()), e);
    }
    // Opens the file for writing, creating the file if it doesn't exist,
    // or truncating an existing regular-file to a size of 0
    try (FileWriter writer = new FileWriter(filePathToWrite.toString())) {
      GSON.toJson(appToPush, writer);
    } catch (IOException e) {
      throw new ConfigFileWriteException(
          String.format("Failed to write application config to path %s", appRelativePath), e
      );
    }

    LOG.debug("Wrote application configs for {} in file {}", appToPush.getName(),
        appRelativePath);
  }

  /**
   * Atomic operation of writing commit and push, return push responses.
   *
   * @param repositoryManager {@link RepositoryManager} to conduct git operations
   * @param commitMeta {@link CommitMeta} from user input
   * @param filesToPush an {@link Iterator} of objects of the generic type T.
   * @throws NoChangesToPushException if there's no effective change between applications in
   *     namespace and git repository
   * @throws SourceControlException for failures while writing config file or doing git
   *     operations
   */
  //TODO: CDAP-20371, Add retry logic here in case the head at remote moved while we are doing push
  private Collection<PushAppResponse> commitAndPush(RepositoryManager repositoryManager,
      CommitMeta commitMeta, List<PushFile> filesToPush) throws NoChangesToPushException {

    // TODO: CDAP-20383, handle NoChangesToPushException
    //  Define the case that the application to push does not have any changes
    try {
      return repositoryManager.commitAndPush(commitMeta, filesToPush,
          (PushFile pushFile, String hash) -> new PushAppResponse(
              pushFile.getAppName(),
              pushFile.getAppVersion(),
              hash
          ));
    } catch (GitAPIException e) {
      throw new GitOperationException(
          String.format("Failed to push configs to git: %s", e.getMessage()), e);
    }
  }

  /**
   * Generate config file name from app name. Currently, it only adds `.json` as extension with app
   * name being the filename.
   *
   * @param appName Name of the application
   * @return The file name we want to store application config in
   */
  private String generateConfigFileName(String appName) {
    return String.format("%s.json", appName);
  }

  /**
   * Validates if the resolved path is not a symbolic link and not a directory.
   *
   * @param repositoryManager the RepositoryManager
   * @param appRelativePath   the relative {@link Path} of the application to write to
   * @return A valid application config file relative path
   */
  private Path validateAppConfigRelativePath(RepositoryManager repositoryManager, Path appRelativePath) throws
    IllegalArgumentException {
    Path filePath = repositoryManager.getRepositoryRoot().resolve(appRelativePath);
    if (Files.isSymbolicLink(filePath)) {
      throw new IllegalArgumentException(String.format(
        "%s exists but refers to a symbolic link. Symbolic links are " + "not allowed.", appRelativePath));
    }
    if (Files.isDirectory(filePath)) {
      throw new IllegalArgumentException(String.format("%s refers to a directory not a file.", appRelativePath));
    }
    return filePath;
  }

  @Override
  public RepositoryAppsResponse list(NamespaceRepository nameSpaceRepository) throws
    AuthenticationConfigException, NotFoundException {
    try (RepositoryManager repositoryManager = repoManagerFactory.create(nameSpaceRepository.getNamespaceId(),
                                                                         nameSpaceRepository.getRepositoryConfig())) {
      String currentCommit = repositoryManager.cloneRemote();
      Path basePath = repositoryManager.getBasePath();

      if (!Files.exists(basePath)) {
        throw new NotFoundException(String.format("Missing repository base path %s", basePath));
      }

      FileFilter configFileFilter = getConfigFileFilter();
      List<File> configFiles = DirUtils.listFiles(basePath.toFile(), configFileFilter);
      List<RepositoryApp> responses = new ArrayList<>();

      // TODO CDAP-20420 Implement bulk fetch file hash in RepositoryManager
      for (File configFile : configFiles) {
        String applicationName = FileUtils.getNameWithoutExtension(configFile.getName());
        Path appConfigRelativePath = repositoryManager.getFileRelativePath(configFile.getName());
        try {
          String fileHash = repositoryManager.getFileHash(appConfigRelativePath, currentCommit);
          responses.add(new RepositoryApp(applicationName, fileHash));
        } catch (NotFoundException e) {
          // ignore file if not found in git tree
          LOG.warn("Skipping config file {}: {}", appConfigRelativePath, e.getMessage());
        }
      }
      return new RepositoryAppsResponse(responses);
    } catch (IOException | GitAPIException e) {
      throw new GitOperationException(String.format("Failed to list application configs in directory %s: %s",
                                                     nameSpaceRepository.getRepositoryConfig().getPathPrefix(),
                                                     e.getMessage()), e);
    }
  }


  /**
   * A helper function to get a {@link java.io.FileFilter} for application config files with following rules
   *    1. Filter non-symbolic link files
   *    2. Filter files with extension json
   */
  private FileFilter getConfigFileFilter() {
    return file -> {
      return Files.isRegularFile(file.toPath(), LinkOption.NOFOLLOW_LINKS) // only allow regular files
          && FileUtils.getExtension(file.getName()).equalsIgnoreCase("json"); // filter json extension
    };
  }

  @Override
  protected void startUp() throws Exception {
    SecureSystemReader.setAsSystemReader();
  }

  @Override
  protected void shutDown() throws Exception {
    // No-op.
  }

  /**
   * Container class to encapsulate files to push information.
   */
  public static class PushFile implements Supplier<Path> {

    private final String appName;
    private final String appVersion;
    private final Path path;

    /**
     * Constructs an object encapsulating information for a file to push.
     * @param appName name of the application to push
     * @param appVersion version of the application to push
     * @param path filepath of the application json to push
     */
    public PushFile(String appName, String appVersion, Path path) {
      this.appName = appName;
      this.appVersion = appVersion;
      this.path = path;
    }

    public String getAppName() {
      return appName;
    }

    public String getAppVersion() {
      return appVersion;
    }

    @Override
    public Path get() {
      return path;
    }
  }
}

