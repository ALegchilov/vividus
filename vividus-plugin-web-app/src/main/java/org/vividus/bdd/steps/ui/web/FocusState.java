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

package org.vividus.bdd.steps.ui.web;

import org.openqa.selenium.WebElement;
import org.vividus.ui.web.action.IJavascriptActions;

public enum FocusState
{
    IN_FOCUS
    {
        @Override
        public boolean isInState(IJavascriptActions javaScriptActions, WebElement expectedElement)
        {
            return isInFocus(javaScriptActions, expectedElement);
        }
    },
    NOT_IN_FOCUS
    {
        @Override
        public boolean isInState(IJavascriptActions javaScriptActions, WebElement expectedElement)
        {
            return !isInFocus(javaScriptActions, expectedElement);
        }
    };

    public abstract boolean isInState(IJavascriptActions javaScriptActions, WebElement expectedElement);

    private static boolean isInFocus(IJavascriptActions javaScriptActions, WebElement expectedElement)
    {
        return javaScriptActions.executeScript("return arguments[0]==document.activeElement", expectedElement);
    }
}
