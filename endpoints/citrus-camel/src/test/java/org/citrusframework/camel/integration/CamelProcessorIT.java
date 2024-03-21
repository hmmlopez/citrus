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

package org.citrusframework.camel.integration;

import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.message.MessageType;
import org.citrusframework.testng.spring.TestNGCitrusSpringSupport;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.citrusframework.actions.ReceiveMessageAction.Builder.receive;
import static org.citrusframework.actions.SendMessageAction.Builder.send;
import static org.citrusframework.camel.dsl.CamelSupport.camel;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.seda;

/**
 * @author Christoph Deppisch
 */
public class CamelProcessorIT extends TestNGCitrusSpringSupport {

    @Autowired
    private CamelContext camelContext;

    @Test
    @CitrusTest
    public void shouldProcessSentMessage() {
        when(send(camel().endpoint(seda("test")::getUri))
                .message()
                .body("Citrus rocks!")
                .process(camel(camelContext)
                    .process(exchange -> exchange
                            .getMessage()
                            .setBody(exchange.getMessage().getBody(String.class).toUpperCase())))
        );

        then(receive(camel().endpoint(seda("test")::getUri))
                .message()
                .type(MessageType.PLAINTEXT)
                .body("CITRUS ROCKS!"));
    }

    @Test
    @CitrusTest
    public void shouldProcessReceivedMessage() {
        when(send(camel().endpoint(seda("test")::getUri))
                .message()
                .body("Citrus rocks!"));

        then(receive(camel().endpoint(seda("test")::getUri))
                .process(camel(camelContext)
                        .process(exchange -> exchange
                                .getMessage()
                                .setBody(exchange.getMessage().getBody(String.class).toUpperCase())))
                .message()
                .type(MessageType.PLAINTEXT)
                .body("CITRUS ROCKS!"));
    }
}
