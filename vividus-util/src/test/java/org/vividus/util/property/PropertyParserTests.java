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

package org.vividus.util.property;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyParserTests
{
    private static final String FAMILY = "family";
    private static final String OTHER_PROP3 = ".other.prop3";
    private static final String VAL3 = "val3";

    private PropertyParser parser;
    private Map<String, String> expectedPropertyValues;
    private Properties properties;

    @BeforeEach
    void beforeEach()
    {
        expectedPropertyValues = new HashMap<>();
        expectedPropertyValues.put("prop1", "val1");
        expectedPropertyValues.put("prop2", "val2");

        properties = new Properties();
        expectedPropertyValues.forEach((key, value) -> properties.put(FAMILY + "." + key, value));
        properties.put(FAMILY + OTHER_PROP3, VAL3);
        parser = new PropertyParser();
        parser.setProperties(properties);
    }

    @Test
    void testGetPropertiesByPrefix()
    {
        Map<String, String> propertiesByPrefix = parser.getPropertiesByPrefix(FAMILY);
        assertThat(propertiesByPrefix.entrySet(), equalTo(properties.entrySet()));
    }

    @Test
    void testGetPropertyValuesByFamily()
    {
        Map<String, String> actualPropertyValues = parser.getPropertyValuesByFamily(FAMILY);
        assertThat(actualPropertyValues.entrySet(), equalTo(expectedPropertyValues.entrySet()));
    }

    @Test
    void testGetPropertiesByRegex()
    {
        Map<String, String> propertiesByPrefix = parser.getPropertiesByRegex(Pattern.compile(FAMILY + ".*prop3"));
        assertThat(propertiesByPrefix.entrySet(),
                equalTo(Collections.singletonMap(FAMILY + OTHER_PROP3, VAL3).entrySet()));
    }

    @Test
    void testGetPropertyValue()
    {
        Entry<String, String> property = expectedPropertyValues.entrySet().iterator().next();
        assertEquals(property.getValue(), parser.getPropertyValue(FAMILY + ".%s", property.getKey()));
    }
}
