/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus;

import com.consol.citrus.actions.EchoAction;
import com.consol.citrus.actions.FailAction;
import com.consol.citrus.container.*;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.report.TestSuiteListener;
import com.consol.citrus.report.TestSuiteListeners;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.easymock.EasyMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Collections;

import static org.easymock.EasyMock.*;

/**
 * @author Christoph Deppisch
 */
public class TestSuiteTest extends AbstractTestNGUnitTest {
    @Autowired
    private TestSuiteListeners testSuiteListeners;

    @Autowired
    @Qualifier("mockListener")
    private TestSuiteListener testSuiteListener;
    
    @BeforeClass
    public void setupTest() {
        testSuiteListeners.addTestSuiteListener(testSuiteListener);
    }

    @AfterClass
    public void cleanUpTest() {
        reset(testSuiteListener);
    }
    
    @Test
    public void testBeforeSuite() {
        SequenceBeforeSuite beforeActions = new SequenceBeforeSuite();
        
        beforeActions.setTestSuiteListener(testSuiteListeners);
        
        reset(testSuiteListener);
        
        testSuiteListener.onStart();
        expectLastCall().once();
        
        testSuiteListener.onStartSuccess();
        expectLastCall().once();
        
        replay(testSuiteListener);
        
        beforeActions.addTestAction(new EchoAction());
        
        beforeActions.execute(createTestContext());
        
        verify(testSuiteListener);
    }
    
    @Test
    public void testFailBeforeSuite() {
        SequenceBeforeSuite beforeActions = new SequenceBeforeSuite();
        
        beforeActions.setTestSuiteListener(testSuiteListeners);
        
        reset(testSuiteListener);
        
        testSuiteListener.onStart();
        expectLastCall().once();
        
        testSuiteListener.onStartFailure((Throwable)anyObject());
        expectLastCall().once();
        
        replay(testSuiteListener);
        
        beforeActions.addTestAction(new FailAction());
        
        try {
            beforeActions.execute(createTestContext());
        } catch (CitrusRuntimeException e) {
            verify(testSuiteListener);
            return;
        }
        
        Assert.fail("Missing CitrusRuntimeException due to failing before suite action");
    }
    
    @Test
    public void testFailBeforeSuiteWithAfterSuite() {
        SequenceBeforeSuite beforeActions = new SequenceBeforeSuite();
        SequenceAfterSuite afterActions = new SequenceAfterSuite();
        
        beforeActions.setTestSuiteListener(testSuiteListeners);
        beforeActions.setAfterSuiteActions(Collections.singletonList(afterActions));
        
        afterActions.setTestSuiteListener(testSuiteListeners);

        TestAction afterSuiteAction = EasyMock.createMock(TestAction.class);
        afterActions.addTestAction(afterSuiteAction);
        
        reset(testSuiteListener, afterSuiteAction);
        
        testSuiteListener.onStart();
        expectLastCall().once();
        
        testSuiteListener.onStartFailure((Throwable)anyObject());
        expectLastCall().once();
        
        testSuiteListener.onFinish();
        expectLastCall().once();
        
        testSuiteListener.onFinishSuccess();
        expectLastCall().once();
        
        afterSuiteAction.execute((TestContext)anyObject());
        expectLastCall().once();
        
        replay(testSuiteListener, afterSuiteAction);
        
        beforeActions.addTestAction(new FailAction());
        
        try {
            beforeActions.execute(createTestContext());
        } catch (CitrusRuntimeException e) {
            verify(testSuiteListener, afterSuiteAction);
            return;
        }
        
        Assert.fail("Missing CitrusRuntimeException due to failing before suite action");
    }
    
    @Test
    public void testAfterSuite() {
        SequenceAfterSuite afterActions = new SequenceAfterSuite();
        
        afterActions.setTestSuiteListener(testSuiteListeners);
        
        reset(testSuiteListener);
        
        testSuiteListener.onFinish();
        expectLastCall().once();
        
        testSuiteListener.onFinishSuccess();
        expectLastCall().once();
        
        replay(testSuiteListener);
        
        afterActions.addTestAction(new EchoAction());
        
        afterActions.execute(createTestContext());
        
        verify(testSuiteListener);
    }
    
    @Test
    public void testFailAfterSuite() {
        SequenceAfterSuite afterActions = new SequenceAfterSuite();
        
        afterActions.setTestSuiteListener(testSuiteListeners);
        
        reset(testSuiteListener);
        
        testSuiteListener.onFinish();
        expectLastCall().once();
        
        testSuiteListener.onFinishFailure((Throwable)anyObject());
        expectLastCall().once();
        
        replay(testSuiteListener);
        
        afterActions.addTestAction(new FailAction());
        
        try {
            afterActions.execute(createTestContext());
        } catch (CitrusRuntimeException e) {
            verify(testSuiteListener);
            return;
        }
        
        Assert.fail("Missing CitrusRuntimeException due to failing after suite action");
    }
    
    @Test
    public void testBeforeTest() {
        SequenceBeforeTest beforeTestActions = new SequenceBeforeTest();
        
        beforeTestActions.addTestAction(new EchoAction());
        
        beforeTestActions.execute(createTestContext());
    }
    
    @Test(expectedExceptions = CitrusRuntimeException.class)
    public void testBeforeTestFail() {
        SequenceBeforeTest beforeTestActions = new SequenceBeforeTest();
        
        beforeTestActions.addTestAction(new FailAction());
        
        beforeTestActions.execute(createTestContext());
    }
}
