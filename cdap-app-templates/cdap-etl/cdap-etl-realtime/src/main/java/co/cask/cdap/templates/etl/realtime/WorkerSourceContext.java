/*
 * Copyright © 2015 Cask Data, Inc.
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

package co.cask.cdap.templates.etl.realtime;

import co.cask.cdap.api.metrics.Metrics;
import co.cask.cdap.api.worker.Worker;
import co.cask.cdap.api.worker.WorkerContext;
import co.cask.cdap.templates.etl.api.StageSpecification;
import co.cask.cdap.templates.etl.api.config.ETLStage;
import co.cask.cdap.templates.etl.api.realtime.SourceContext;
import co.cask.cdap.templates.etl.common.StageMetrics;

import java.util.Map;

/**
 * Implementation of {@link SourceContext} for a {@link Worker} driver.
 */
public class WorkerSourceContext implements SourceContext {
  private final WorkerContext context;
  private final StageSpecification specification;
  private final ETLStage stage;
  private final Metrics metrics;

  public WorkerSourceContext(WorkerContext context, ETLStage sourceStage,
                             StageSpecification specification, Metrics metrics) {
    this.context = context;
    this.specification = specification;
    this.stage = sourceStage;
    this.metrics = new StageMetrics(metrics, StageMetrics.Type.SOURCE, specification.getName());
  }

  @Override
  public StageSpecification getSpecification() {
    return specification;
  }

  @Override
  public Metrics getMetrics() {
    return metrics;
  }

  @Override
  public int getInstanceId() {
    return context.getInstanceId();
  }

  @Override
  public int getInstanceCount() {
    return context.getInstanceCount();
  }

  @Override
  public Map<String, String> getRuntimeArguments() {
    return stage.getProperties();
  }
}
