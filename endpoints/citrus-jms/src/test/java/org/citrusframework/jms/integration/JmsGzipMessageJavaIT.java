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

package org.citrusframework.jms.integration;

import java.io.IOException;

import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.DefaultMessage;
import org.citrusframework.message.MessageType;
import org.citrusframework.testng.spring.TestNGCitrusSpringSupport;
import org.citrusframework.util.FileUtils;
import org.testng.annotations.Test;

import static org.citrusframework.actions.ReceiveMessageAction.Builder.receive;
import static org.citrusframework.actions.SendMessageAction.Builder.send;
import static org.citrusframework.validation.interceptor.GzipMessageProcessor.Builder.toGzip;

@Test
public class JmsGzipMessageJavaIT extends TestNGCitrusSpringSupport {

    @CitrusTest
    public void jmsByteMessage() throws IOException {
        when(send("jms:queue:jms.gzip.queue")
                .message(new DefaultMessage(
                        FileUtils.copyToByteArray(
                                FileUtils.getFileResource("org/citrusframework/jms/integration/button.png")
                                 .getInputStream())))
                .process(toGzip()));

        then(receive("jms:queue:jms.gzip.queue")
                .message()
                .type(MessageType.GZIP_BASE64)
                .body("citrus:readFile('classpath:org/citrusframework/jms/integration/button.png', true)"));
    }
}
