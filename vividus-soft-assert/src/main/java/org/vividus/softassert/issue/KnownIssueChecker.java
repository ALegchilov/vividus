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

package org.vividus.softassert.issue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Pattern;

import org.vividus.softassert.model.KnownIssue;

public class KnownIssueChecker implements IKnownIssueChecker
{
    private IKnownIssueProvider knownIssueProvider;
    private IIssueStateProvider issueStateProvider;
    private ITestInfoProvider testInfoProvider;
    private Map<String, IKnownIssueDataProvider> knownIssueDataProviders;

    @Override
    public KnownIssue getKnownIssue(String failedAssertion)
    {
        TestInfo testInfo = testInfoProvider != null ? testInfoProvider.getTestInfo() : null;
        CandidateIssue candidateIssue = new CandidateIssue(testInfo);
        for (Entry<String, ? extends KnownIssueIdentifier> knownIssueEntry : knownIssueProvider
                .getKnownIssueIdentifiers().entrySet())
        {
            KnownIssueIdentifier knownIssueIdentifier = knownIssueEntry.getValue();
            if (knownIssueIdentifier.getAssertionCompiledPattern().matcher(failedAssertion).matches() && candidateIssue
                    .isProperCandidate(knownIssueEntry.getKey(), knownIssueIdentifier))
            {
                break;
            }
        }
        Optional.ofNullable(candidateIssue.issue).ifPresent(this::setState);
        return candidateIssue.issue;
    }

    private void setState(KnownIssue knownIssue)
    {
        if (issueStateProvider != null)
        {
            knownIssue.setStatus(issueStateProvider.getIssueStatus(knownIssue.getIdentifier()));
            if (knownIssue.isClosed())
            {
                knownIssue.setResolution(issueStateProvider.getIssueResolution(knownIssue.getIdentifier()));
            }
        }
    }

    public void setKnownIssueProvider(IKnownIssueProvider knownIssueProvider)
    {
        this.knownIssueProvider = knownIssueProvider;
    }

    public void setTestInfoProvider(ITestInfoProvider testInfoProvider)
    {
        this.testInfoProvider = testInfoProvider;
    }

    public void setIssueStateProvider(IIssueStateProvider issueStateProvider)
    {
        this.issueStateProvider = issueStateProvider;
    }

    public void setKnownIssueDataProviders(Map<String, IKnownIssueDataProvider> knownIssueDataProviders)
    {
        this.knownIssueDataProviders = knownIssueDataProviders;
    }

    private class CandidateIssue
    {
        private static final int ALL_PATTERNS = 3;

        private int bestPatternsMatched;
        private int currentPatternsMatched;
        private final TestInfo testInfo;
        private KnownIssue issue;

        CandidateIssue(TestInfo testInfo)
        {
            this.testInfo = testInfo;
        }

        boolean isProperCandidate(String candidateId, KnownIssueIdentifier candidate)
        {
            resetCurrentPatternsMatched();
            Map<String, Pattern> dynamicPatterns = candidate.getDynamicCompiledPatterns();
            if (!doAllDynamicPatternsMatch(dynamicPatterns))
            {
                return false;
            }
            boolean potentiallyKnown = isPotentiallyKnown(candidate);
            if (potentiallyKnown)
            {
                resetCurrentPatternsMatched();
            }
            if (issue == null || bestPatternsMatched < currentPatternsMatched)
            {
                bestPatternsMatched = currentPatternsMatched;
                issue = new KnownIssue(candidateId, candidate.getType(), potentiallyKnown);
                return bestPatternsMatched == ALL_PATTERNS;
            }
            return false;
        }

        private boolean doAllDynamicPatternsMatch(Map<String, Pattern> dynamicPatterns)
        {
            return dynamicPatterns.entrySet().stream().allMatch(entry -> {
                String key = entry.getKey();
                Pattern pattern = entry.getValue();
                return Optional.ofNullable(knownIssueDataProviders.get(key))
                           .map(p -> pattern.matcher(p.getData()).matches())
                           .orElse(false);
            });
        }

        boolean isPotentiallyKnown(KnownIssueIdentifier knownIssueIdentifier)
        {
            return testInfo != null && (
                    isPotentiallyKnown(testInfo.getTestSuite(), knownIssueIdentifier.getTestSuiteCompiledPattern())
                            || isPotentiallyKnown(testInfo.getTestCase(),
                            knownIssueIdentifier.getTestCaseCompiledPattern()) || isPotentiallyKnown(
                            testInfo.getTestStep(), knownIssueIdentifier.getTestStepCompiledPattern()));
        }

        boolean isPotentiallyKnown(String testInfo, Pattern pattern)
        {
            if (testInfo != null && pattern != null)
            {
                if (!pattern.matcher(testInfo).matches())
                {
                    return true;
                }
                ++currentPatternsMatched;
            }
            return false;
        }

        void resetCurrentPatternsMatched()
        {
            currentPatternsMatched = 0;
        }
    }
}
