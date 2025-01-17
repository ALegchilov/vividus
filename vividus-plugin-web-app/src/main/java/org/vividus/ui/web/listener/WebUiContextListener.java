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

import com.google.common.eventbus.Subscribe;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.vividus.ui.web.context.IWebUiContext;
import org.vividus.ui.web.event.WebDriverCreateEvent;
import org.vividus.ui.web.event.WebDriverQuitEvent;

public class WebUiContextListener extends AbstractWebDriverEventListener
{
    private IWebUiContext webUiContext;

    @Subscribe
    public void onWebDriverCreate(WebDriverCreateEvent event)
    {
        webUiContext.putSearchContext(event.getWebDriver(), () -> onWebDriverCreate(event));
    }

    @Subscribe
    public void onWebDriverQuit(@SuppressWarnings("unused") WebDriverQuitEvent event)
    {
        webUiContext.clear();
    }

    @Override
    public void afterNavigateBack(WebDriver driver)
    {
        webUiContext.reset();
    }

    @Override
    public void afterNavigateForward(WebDriver driver)
    {
        webUiContext.reset();
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver)
    {
        webUiContext.reset();
    }

    public void setWebUiContext(IWebUiContext webUiContext)
    {
        this.webUiContext = webUiContext;
    }
}
