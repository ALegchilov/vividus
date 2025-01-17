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

package org.vividus.ui.web.listener;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.vividus.selenium.manager.IWebDriverManager;
import org.vividus.ui.web.context.IWebUiContext;

public class WindowSwitchListener extends AbstractWebDriverEventListener
{
    private final ThreadLocal<String> currentWindowIdentifier = ThreadLocal.withInitial(() -> StringUtils.EMPTY);
    @Inject private IWebDriverManager webDriverManager;
    @Inject private IWebUiContext webUiContext;

    @Override
    public void beforeSwitchToWindow(String windowName, WebDriver driver)
    {
        String currentIdentifier = currentWindowIdentifier.get();
        if (currentIdentifier.isEmpty())
        {
            return;
        }
        Set<String> windowHandles = webDriverManager.getWindowHandles();
        if (!windowHandles.contains(currentIdentifier))
        {
            driver.switchTo().window(windowHandles.iterator().next());
            webUiContext.reset();
        }
    }

    @Override
    public void afterSwitchToWindow(String windowName, WebDriver driver)
    {
        currentWindowIdentifier.set(windowName);
    }
}
