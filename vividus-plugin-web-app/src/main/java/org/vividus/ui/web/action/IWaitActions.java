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

package org.vividus.ui.web.action;

import java.time.Duration;
import java.util.function.Function;

import org.openqa.selenium.WebDriver;

public interface IWaitActions
{
    void waitForPageLoad();

    void waitForPageLoad(WebDriver webDriver);

    <T, V> WaitResult<V> wait(T input, Function<T, V> isTrue);

    <T, V> WaitResult<V> wait(T input, Function<T, V> isTrue, boolean recordAssertionIfTimeout);

    <T, V> WaitResult<V> wait(T input, Duration timeout, Function<T, V> isTrue);

    <T, V> WaitResult<V> wait(T input, Duration timeout, Function<T, V> isTrue, boolean recordAssertionIfTimeout);

    <T, V> WaitResult<V> wait(T input, Duration timeout, Duration pollingPeriod, Function<T, V> isTrue);

    <T, V> WaitResult<V> wait(T input, Duration timeout, Duration pollingPeriod, Function<T, V> isTrue,
            boolean recordAssertionIfTimeout);

    WaitResult<Boolean> waitForNewWindowOpen(WebDriver webDriver, int alreadyOpenedWindowNumber, Duration timeout);

    WaitResult<Boolean> waitForNewWindowOpen(WebDriver webDriver, int alreadyOpenedWindowNumber, Duration timeout,
            boolean recordAssertionIfTimeout);

    WaitResult<Boolean> waitForPageOpen(WebDriver webDriver, String oldUrl, Duration timeout);

    WaitResult<Boolean> waitForNewWindowOpen(WebDriver webDriver, int alreadyOpenedWindowNumber);

    WaitResult<Boolean> waitForNewWindowOpen(WebDriver webDriver, int alreadyOpenedWindowNumber,
            boolean recordAssertionIfTimeout);

    WaitResult<Boolean> waitForPageOpen(WebDriver webDriver, String oldUrl);

    WaitResult<Boolean> waitForWindowToClose(WebDriver webDriver, String windowHandleToClose);

    void sleepForTimeout(Duration time);
}
