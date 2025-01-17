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

package org.vividus.softassert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.eventbus.EventBus;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.vividus.softassert.event.AssertionFailedEvent;

class AssertionManagerTests
{
    @ParameterizedTest
    @CsvSource({"true, 1", "false, 0"})
    void shouldFailFast(boolean failFast, int numberOfVerifyInvocations)
    {
        ISoftAssert softAssert = mock(SoftAssert.class);
        AssertionManager assertionManager = new AssertionManager(new EventBus(), softAssert);
        assertionManager.setFailFast(failFast);
        assertionManager.onAssertionFailure(new AssertionFailedEvent());
        verify(softAssert, times(numberOfVerifyInvocations)).verify();
    }
}
