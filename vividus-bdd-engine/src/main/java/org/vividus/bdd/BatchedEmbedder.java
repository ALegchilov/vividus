/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vividus.bdd;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.MetaFilter;
import org.jbehave.core.embedder.PerformableTree;
import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.failures.BatchFailures;
import org.vividus.bdd.context.BddRunContext;

public class BatchedEmbedder extends ExtendedEmbedder
{
    private BddRunContext bddRunContext;

    private String batch;
    private boolean ignoreFailureInBatches;
    private boolean reportBeforeStories = true;
    private boolean reportAfterStories;

    public void runStoriesAsPaths(Map<String, List<String>> storyPathsBatches)
    {
        processSystemProperties();
        int batchesSize = storyPathsBatches.size();
        Iterator<Entry<String, List<String>>> iterator = storyPathsBatches.entrySet().iterator();
        generateViewAfterExecution(() ->
        {
            for (int i = 1; iterator.hasNext(); i++)
            {
                reportBeforeStories = i == 1;
                reportAfterStories = i == batchesSize;

                Entry<String, List<String>> storyPathsBatch = iterator.next();
                batch = storyPathsBatch.getKey();

                EmbedderControls embedderControls = embedderControls();
                embedderMonitor.usingControls(embedderControls);

                List<String> storyPaths = storyPathsBatch.getValue();
                if (embedderControls.skip())
                {
                    embedderMonitor.storiesSkipped(storyPaths);
                    continue;
                }

                try
                {
                    bddRunContext.putRunningBatch(batch);
                    StoryManager storyManager = storyManager();
                    MetaFilter filter = metaFilter();
                    BatchFailures failures = new BatchFailures(embedderControls.verboseFailures());

                    storyManager.runStoriesAsPaths(storyPaths, filter, failures);

                    handleFailures(failures);
                    if (!ignoreFailureInBatches && !failures.isEmpty())
                    {
                        break;
                    }
                }
                finally
                {
                    bddRunContext.removeRunningBatch();
                    shutdownExecutorService();
                }
            }
        });
    }

    private void generateViewAfterExecution(Runnable runnable)
    {
        try
        {
            runnable.run();
        }
        finally
        {
            if (getEmbedderControlsProvider().isGenerateViewAfterBatches())
            {
                generateReportsView();
            }
        }
    }

    @Override
    public StoryManager storyManager()
    {
        return createStoryManager();
    }

    @Override
    public PerformableTree performableTree()
    {
        BatchedPerformableTree performableTree = (BatchedPerformableTree) super.performableTree();
        performableTree.setReportBeforeStories(reportBeforeStories);
        performableTree.setReportAfterStories(reportAfterStories);
        performableTree.setIgnoreFailureInBatches(ignoreFailureInBatches);
        return performableTree;
    }

    @Override
    public EmbedderControls embedderControls()
    {
        IEmbedderControlsProvider embedderControlsProvider = getEmbedderControlsProvider();
        return Optional.ofNullable(batch)
                .map(embedderControlsProvider::get)
                .orElseGet(embedderControlsProvider::getDefault);
    }

    public void setBddRunContext(BddRunContext bddRunContext)
    {
        this.bddRunContext = bddRunContext;
    }

    public void setPerformableTree(PerformableTree performableTree)
    {
        usePerformableTree(performableTree);
    }

    public void setIgnoreFailureInBatches(boolean ignoreFailureInBatches)
    {
        this.ignoreFailureInBatches = ignoreFailureInBatches;
    }
}
