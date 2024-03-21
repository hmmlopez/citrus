/*
 * Copyright the original author or authors.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.citrusframework.camel.message;

import org.citrusframework.CitrusSettings;
import org.citrusframework.message.DefaultMessage;
import org.citrusframework.message.Message;
import org.citrusframework.testng.AbstractTestNGUnitTest;
import org.citrusframework.util.TypeConversionUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.TransformProcessor;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.citrusframework.camel.message.CamelMessageProcessor.Builder.process;

/**
 * @author Christoph Deppisch
 */
public class CamelMessageProcessorTest extends AbstractTestNGUnitTest {

    private final CamelContext camelContext = new DefaultCamelContext();

    @BeforeClass
    public void setupTypeConverter() {
        System.setProperty(CitrusSettings.TYPE_CONVERTER_PROPERTY, "camel");
        TypeConversionUtils.loadDefaultConverter();
    }

    @AfterClass(alwaysRun = true)
    public void restoreTypeConverterDefault() {
        System.setProperty(CitrusSettings.TYPE_CONVERTER_PROPERTY, CitrusSettings.TYPE_CONVERTER_DEFAULT);
        TypeConversionUtils.loadDefaultConverter();
    }

    @Test
    public void shouldSetBodyFromProcessor() {
        Processor processor = new TransformProcessor(ExpressionBuilder.constantExpression("Hello from Camel!"));
        CamelMessageProcessor messageProcessor = process(processor)
                .camelContext(camelContext)
                .build();

        Message in = new DefaultMessage("Hello from Citrus!")
                .setHeader("operation", "sayHello");
        messageProcessor.process(in, context);

        Assert.assertEquals(in.getPayload(String.class), "Hello from Camel!");
        Assert.assertEquals(in.getHeader("operation"), "sayHello");
    }

    @Test
    public void shouldSetBodyFromHeaderExpression() {
        Processor processor = new TransformProcessor(ExpressionBuilder.headerExpression("operation"));
        CamelMessageProcessor messageProcessor = process(processor)
                .camelContext(camelContext)
                .build();

        Message in = new DefaultMessage()
                .setHeader("operation", "sayHello");
        messageProcessor.process(in, context);

        Assert.assertEquals(in.getPayload(String.class), "sayHello");
        Assert.assertEquals(in.getHeader("operation"), "sayHello");
    }
}
