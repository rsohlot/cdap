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

package io.cdap.cdap.internal.operation;

import io.cdap.cdap.internal.app.sourcecontrol.PullAppsOperationFactory;
import io.cdap.cdap.internal.app.sourcecontrol.PullAppsRequest;

/**
 * Abstract runner implementation with common functionality.
 */
public abstract class AbstractOperationRunner implements OperationRunner {

  private final PullAppsOperationFactory pullOperationFactory;

  AbstractOperationRunner(PullAppsOperationFactory pullOperationFactory) {
    this.pullOperationFactory = pullOperationFactory;
  }

  /**
   * Creates a {@link LongRunningOperation} given the {@link OperationRunDetail}.
   *
   * @param detail {@link OperationRunDetail} for the operation
   * @return {@link LongRunningOperation} for the operation type in request
   */
  protected LongRunningOperation createOperation(OperationRunDetail detail)
      throws IllegalStateException {
    switch (detail.getRun().getType()) {
      case PULL_APPS:
        PullAppsRequest request = detail.getPullAppsRequest();
        if (request == null) {
          throw new IllegalStateException("Missing request for pull operation");
        }
        return pullOperationFactory.create(request);
      case PUSH_APPS:
      default:
        throw new IllegalStateException(
            String.format("Invalid operation type %s", detail.getRun().getType()));
    }
  }
}
