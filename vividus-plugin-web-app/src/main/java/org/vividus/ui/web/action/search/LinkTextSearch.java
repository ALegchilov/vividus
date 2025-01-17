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

package org.vividus.ui.web.action.search;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.vividus.ui.web.util.LocatorUtil;

public class LinkTextSearch extends AbstractElementSearchAction implements IElementSearchAction
{
    private static final String LINK_WITH_ANY_ATTRIBUTE_OR_TEXT = ".//a[text()=%1$s or @*=%1$s or *=%1$s]";

    @Override
    public List<WebElement> search(SearchContext searchContext, SearchParameters parameters)
    {
        if (searchContext != null)
        {
            String text = parameters.getValue();
            By locator = By.linkText(text);
            List<WebElement> links = findElements(searchContext, locator, parameters);
            if (links.isEmpty())
            {
                locator = LocatorUtil.getXPathLocator(LINK_WITH_ANY_ATTRIBUTE_OR_TEXT, text);
                return findElementsByText(searchContext, locator, parameters, "a");
            }
            return links;
        }
        return List.of();
    }
}
