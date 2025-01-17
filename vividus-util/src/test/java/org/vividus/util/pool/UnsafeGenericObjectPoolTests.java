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

package org.vividus.util.pool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UnsafeGenericObjectPoolTests
{
    private static final String VALUE = "value";

    @Test
    void testApply()
    {
        assertEquals(VALUE, new UnsafeGenericObjectPool<>(() -> VALUE).apply(pooled -> pooled));
    }

    @Test
    void testAccept()
    {
        new UnsafeGenericObjectPool<>(() -> VALUE).accept(pooled -> assertEquals(VALUE, pooled));
    }

    @Test
    void testApplyWithExceptionAtCreation()
    {
        IllegalArgumentException exception = new IllegalArgumentException();
        IllegalStateException actual = assertThrows(IllegalStateException.class,
            () -> new UnsafeGenericObjectPool<>(() ->
            {
                throw exception;
            }).apply(pooled -> pooled));
        assertEquals(exception, actual.getCause());
    }

    @Test
    void testApplyWithExceptionAtAppliance()
    {
        IllegalArgumentException exception = new IllegalArgumentException();
        IllegalStateException actual = assertThrows(IllegalStateException.class,
            () -> new UnsafeGenericObjectPool<>(() -> VALUE).apply(pooled ->
            {
                throw exception;
            }));
        assertEquals(exception, actual.getCause());
    }
}
