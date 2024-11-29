/*
 * Copyright the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.citrusframework.config.xml;

import org.citrusframework.container.Iterate;
import org.citrusframework.testng.AbstractActionParserTest;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class IterateParserTest extends AbstractActionParserTest<Iterate> {

    @Test
    public void testActionParser() {
        assertActionCount(3);
        assertActionClassAndName(Iterate.class, "iterate");

        Iterate action = getNextTestActionFromTest();
        assertEquals(action.getCondition(), "i lt 3");
        assertEquals(action.getIndexName(), "i");
        assertEquals(action.getStart(), 1);
        assertEquals(action.getStep(), 1);
        assertEquals(action.getActionCount(), 1);
        assertNull(action.getTimeout());

        action = getNextTestActionFromTest();
        assertEquals(action.getCondition(), "index lt= 2");
        assertEquals(action.getIndexName(), "index");
        assertEquals(action.getStart(), 1);
        assertEquals(action.getStep(), 1);
        assertEquals(action.getActionCount(), 1);
        assertNull(action.getTimeout());

        action = getNextTestActionFromTest();
        assertEquals(action.getCondition(), "i lt= 10");
        assertEquals(action.getIndexName(), "i");
        assertEquals(action.getStart(), 0);
        assertEquals(action.getStep(), 5);
        assertEquals(action.getActionCount(), 2);
        assertEquals(action.getTimeout(), Duration.ofSeconds(3));
    }
}
