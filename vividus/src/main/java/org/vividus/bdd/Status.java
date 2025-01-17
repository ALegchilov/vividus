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

package org.vividus.bdd;

public enum Status
{
    BROKEN(0),
    FAILED(1),
    KNOWN_ISSUES_ONLY(2),
    PENDING(3),
    SKIPPED(4),
    PASSED(5),
    NOT_COVERED(6);

    private static final Status LOWEST = NOT_COVERED;

    private final int priority;

    Status(int priority)
    {
        this.priority = priority;
    }

    public static Status getLowest()
    {
        return LOWEST;
    }

    public int getPriority()
    {
        return priority;
    }
}
