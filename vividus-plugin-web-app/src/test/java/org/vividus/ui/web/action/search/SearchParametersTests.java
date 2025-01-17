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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SearchParametersTests
{
    private static final String VALUE = "value";

    @Test
    void testDefaultConstructor()
    {
        SearchParameters parameters = new SearchParameters();
        assertNull(parameters.getValue());
        assertTrue(parameters.isVisibility(Visibility.VISIBLE));
        assertTrue(parameters.isWaitForElement());
    }

    @Test
    void testStringArgConstructor()
    {
        SearchParameters parameters = new SearchParameters(VALUE);
        assertEquals(VALUE, parameters.getValue());
        assertTrue(parameters.isVisibility(Visibility.VISIBLE));
        assertTrue(parameters.isWaitForElement());
    }

    @Test
    void testSearchParametersArgConstructor()
    {
        SearchParameters testParameters = new SearchParameters(VALUE);
        testParameters.setVisibility(Visibility.ALL);
        testParameters.setWaitForElement(false);

        SearchParameters parameters = new SearchParameters(testParameters);
        assertEquals(VALUE, parameters.getValue());
        assertFalse(parameters.isVisibility(Visibility.VISIBLE));
        assertFalse(parameters.isWaitForElement());
    }

    @Test
    void testSetDisplayedOnly()
    {
        SearchParameters parameters = new SearchParameters();
        parameters.setVisibility(Visibility.ALL);
        assertFalse(parameters.isVisibility(Visibility.VISIBLE));
    }

    @Test
    void testSetWaitForElement()
    {
        SearchParameters parameters = new SearchParameters();
        parameters.setWaitForElement(false);
        assertFalse(parameters.isWaitForElement());
    }

    @Test
    void shouldSetVisibility()
    {
        SearchParameters parameters = new SearchParameters();
        assertEquals(Visibility.VISIBLE, parameters.getVisibility());
        parameters.setVisibility(Visibility.ALL);
        assertEquals(Visibility.ALL, parameters.getVisibility());
    }

    @Test
    void shouldCheckVisibility()
    {
        SearchParameters parameters = new SearchParameters();
        assertTrue(parameters.isVisibility(Visibility.VISIBLE));
    }
}
