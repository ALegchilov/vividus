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

package org.vividus.bdd.transformer;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.model.ExamplesTableProperties;
import org.jbehave.core.model.TableTransformers.TableTransformer;

public interface ExtendedTableTransformer extends TableTransformer
{
    default void checkTableEmptiness(String tableAsString)
    {
        checkArgument(StringUtils.isBlank(tableAsString), "Input table must be empty");
    }

    static String getMandatoryNonBlankProperty(ExamplesTableProperties tableProperties, String propertyName)
    {
        String propertyValue = tableProperties.getProperties().getProperty(propertyName);
        checkArgument(propertyValue != null, "'%s' is not set in ExamplesTable properties", propertyName);
        checkArgument(StringUtils.isNotBlank(propertyValue), "ExamplesTable property '%s' is blank", propertyName);
        return propertyValue;
    }

    default <E extends Enum<E>> E getMandatoryEnumProperty(ExamplesTableProperties properties, String propertyName,
            Class<E> enumClass)
    {
        String propertyValueStr = properties.getProperties().getProperty(propertyName);
        E propertyValue = EnumUtils.getEnumIgnoreCase(enumClass, propertyValueStr);
        checkArgument(propertyValue != null, "Value of ExamplesTable property '%s' must be from range %s",
                propertyName, Arrays.toString(enumClass.getEnumConstants()));
        return propertyValue;
    }

    default <T extends Entry<String, Function<String, R>>, R> R processCompetingMandatoryProperties(
            ExamplesTableProperties tableProperties, T processor1, T processor2)
    {
        String propertyName1 = processor1.getKey();
        String propertyName2 = processor2.getKey();
        String propertyValue1 = tableProperties.getProperties().getProperty(propertyName1);
        String propertyValue2 = tableProperties.getProperties().getProperty(propertyName2);
        if (propertyValue1 != null)
        {
            checkArgument(propertyValue2 == null,
                    "Only one ExamplesTable property must be set, but found both '%s' and '%s'", propertyName1,
                    propertyName2);
            return processor1.getValue().apply(propertyValue1);
        }
        else
        {
            checkArgument(propertyValue2 != null, "One of ExamplesTable properties must be set: either '%s' or '%s'",
                    propertyName1, propertyName2);
            return processor2.getValue().apply(propertyValue2);
        }
    }
}
