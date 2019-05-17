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

package com.consol.citrus.xml;

import com.consol.citrus.message.*;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 * @since 2.6.2
 */
public class XmlFormattingMessageInterceptorTest extends AbstractTestNGUnitTest {

    private XmlFormattingMessageInterceptor messageInterceptor = new XmlFormattingMessageInterceptor();

    @Test
    public void testInterceptMessage() throws Exception {
        Message message = new DefaultMessage("<root>"
                    + "<element attribute='attribute-value'>"
                        + "<sub-element>text-value</sub-element>"
                    + "</element>"
                + "</root>");

        messageInterceptor.interceptMessageConstruction(message, MessageType.XML.name(), context);

        Assert.assertTrue(message.getPayload(String.class).contains(System.lineSeparator()));
    }

    @Test
    public void testInterceptNonXmlMessage() throws Exception {
        Message message = new DefaultMessage("This is plaintext");
        messageInterceptor.interceptMessageConstruction(message, MessageType.PLAINTEXT.name(), context);
        Assert.assertEquals(message.getPayload(String.class), "This is plaintext");
    }

}