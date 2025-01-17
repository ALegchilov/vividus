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

package org.vividus.softassert.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AssertionCollection
{
    private final AtomicInteger assertionsCount = new AtomicInteger();
    private final Queue<SoftAssertionError> assertionErrors = new ConcurrentLinkedQueue<>();

    public int getAssertionsCount()
    {
        return assertionsCount.get();
    }

    public List<SoftAssertionError> getAssertionErrors()
    {
        return Collections.unmodifiableList(new LinkedList<>(assertionErrors));
    }

    public void addPassed()
    {
        assertionsCount.incrementAndGet();
    }

    public void addFailed(SoftAssertionError error)
    {
        assertionsCount.incrementAndGet();
        assertionErrors.add(error);
    }

    public void clear()
    {
        assertionErrors.clear();
        assertionsCount.set(0);
    }
}
