/*
 * Copyright 2006-2016 the original author or authors.
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

package org.citrusframework.cucumber.backend.spring;

import org.citrusframework.TestCaseRunner;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.config.CitrusSpringConfig;
import org.citrusframework.context.TestContext;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Christoph Deppisch
 * @since 2.6
 */
@CucumberContextConfiguration
@ContextConfiguration(classes = CitrusSpringConfig.class)
public class SpringRunnerSteps {

    @CitrusResource
    private TestCaseRunner testRunner;

    @CitrusResource
    private TestContext context;

    /**
     * Gets the value of the testRunner property.
     *
     * @return the testRunner
     */
    public TestCaseRunner getTestRunner() {
        return testRunner;
    }

    /**
     * Obtains the test context.
     *
     * @return
     */
    public TestContext getTestContext() {
        return context;
    }
}
