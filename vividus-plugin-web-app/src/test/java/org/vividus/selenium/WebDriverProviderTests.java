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

package org.vividus.selenium;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.vividus.testcontext.SimpleTestContext;
import org.vividus.testcontext.TestContext;
import org.vividus.ui.web.event.WebDriverCreateEvent;
import org.vividus.ui.web.event.WebDriverQuitEvent;

@ExtendWith(MockitoExtension.class)
class WebDriverProviderTests
{
    private final TestContext testContext = new SimpleTestContext();

    @Mock
    private VividusWebDriverFactory vividusWebDriverFactory;

    @Mock
    private RemoteWebDriver driver;

    @Mock(extraInterfaces = WrapsDriver.class)
    private WebDriver wrapsDriver;

    @Mock
    private VividusWebDriver vividusWebDriver;

    @Mock
    private EventBus mockedEventBus;

    @InjectMocks
    private WebDriverProvider webDriverProvider;

    @BeforeEach
    void beforeEach()
    {
        webDriverProvider.setTestContext(testContext);
    }

    @Test
    void testEnd()
    {
        testContext.put(VividusWebDriver.class, vividusWebDriver);
        when(vividusWebDriver.getWrappedDriver()).thenReturn(wrapsDriver);
        webDriverProvider.end();
        verify(wrapsDriver).quit();
        verify(mockedEventBus).post(any(WebDriverQuitEvent.class));
    }

    @Test
    void testEndWebDriveException()
    {
        testContext.put(VividusWebDriver.class, vividusWebDriver);
        when(vividusWebDriver.getWrappedDriver()).thenReturn(wrapsDriver);
        doThrow(new WebDriverException()).when(wrapsDriver).quit();
        assertThrows(WebDriverException.class, webDriverProvider :: end);
        verify(mockedEventBus).post(any(WebDriverQuitEvent.class));
    }

    @Test
    void testEndNoWebDriver()
    {
        webDriverProvider.end();
        verify(mockedEventBus).post(any(WebDriverQuitEvent.class));
    }

    @Test
    void testDestroy()
    {
        Mockito.doNothing().when(mockedEventBus).post(any(WebDriverCreateEvent.class));
        when(vividusWebDriverFactory.create()).thenReturn(vividusWebDriver);
        when(vividusWebDriver.getWrappedDriver()).thenReturn(driver);
        webDriverProvider.get();
        webDriverProvider.destroy();
        verify(driver).quit();
    }
}
