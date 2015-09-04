/*
 * Copyright 2006-2014 the original author or authors.
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

package com.consol.citrus.container;

import com.consol.citrus.TestAction;
import com.consol.citrus.context.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * Sequence of test actions executed after a test case. Container execution can be restricted according to test name ,
 * package and test groups.
 *
 * @author Christoph Deppisch
 */
public class SequenceAfterTest extends AbstractTestBoundaryActionContainer {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(SequenceAfterTest.class);

    @Override
    public void doExecute(TestContext context) {
        if (CollectionUtils.isEmpty(actions)) {
            return;
        }
        
        log.info("Executing " + actions.size() + " actions after test");
        log.info("");

        for(TestAction action: actions)  {
            action.execute(context);
        }
    }
}
