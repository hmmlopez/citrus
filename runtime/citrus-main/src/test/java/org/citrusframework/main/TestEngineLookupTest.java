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

package org.citrusframework.main;

import org.citrusframework.cucumber.CucumberTestEngine;
import org.citrusframework.exceptions.CitrusRuntimeException;
import org.citrusframework.junit.JUnit4TestEngine;
import org.citrusframework.junit.jupiter.JUnitJupiterEngine;
import org.citrusframework.testng.TestNGEngine;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestEngineLookupTest {

    @Test
    public void shouldResolveJUnit5Engine() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setEngine("junit5");
        Assert.assertEquals(TestEngine.lookup(configuration).getClass(), JUnitJupiterEngine.class);
    }

    @Test
    public void shouldResolveJUnitJupiterEngine() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setEngine("junit-jupiter");
        Assert.assertEquals(TestEngine.lookup(configuration).getClass(), JUnitJupiterEngine.class);
    }

    @Test
    public void shouldResolveJUnit4Engine() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setEngine("junit4");
        Assert.assertEquals(TestEngine.lookup(configuration).getClass(), JUnit4TestEngine.class);
    }

    @Test
    public void shouldResolveTestNGEngine() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setEngine("testng");
        Assert.assertEquals(TestEngine.lookup(configuration).getClass(), TestNGEngine.class);
    }

    @Test
    public void shouldResolveCucumberEngine() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setEngine("cucumber");
        Assert.assertEquals(TestEngine.lookup(configuration).getClass(), CucumberTestEngine.class);
    }

    @Test(expectedExceptions = CitrusRuntimeException.class,
            expectedExceptionsMessageRegExp = "Failed to resolve Citrus engine from resource.*")
    public void shouldHandleUnknownEngine() {
        TestRunConfiguration configuration = new TestRunConfiguration();
        configuration.setEngine("unknown");
        TestEngine.lookup(configuration);
    }
}
