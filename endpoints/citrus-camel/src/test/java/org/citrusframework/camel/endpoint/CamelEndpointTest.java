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

package org.citrusframework.camel.endpoint;

import org.citrusframework.camel.message.CamelMessageHeaders;
import org.citrusframework.exceptions.CitrusRuntimeException;
import org.citrusframework.message.Message;
import org.citrusframework.report.MessageListeners;
import org.citrusframework.testng.AbstractTestNGUnitTest;
import org.apache.camel.CamelExchangeException;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.engine.AbstractCamelContext;
import org.apache.camel.impl.engine.DefaultHeadersMapFactory;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;
import org.apache.camel.support.SimpleUuidGenerator;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

/**
 * @since 1.4.1
 */
public class CamelEndpointTest extends AbstractTestNGUnitTest {

    private final AbstractCamelContext camelContext = Mockito.mock(AbstractCamelContext.class);
    private final ExtendedCamelContext extendedCamelContext = Mockito.mock(ExtendedCamelContext.class);
    private final ProducerTemplate producerTemplate = Mockito.mock(ProducerTemplate.class);
    private final ConsumerTemplate consumerTemplate = Mockito.mock(ConsumerTemplate.class);
    private final Exchange exchange = Mockito.mock(Exchange.class);

    private final MessageListeners messageListeners = Mockito.mock(MessageListeners.class);

    @Test
    public void testCamelEndpointProducer() {
        String endpointUri = "direct:news-feed";
        CamelEndpointConfiguration endpointConfiguration = new CamelEndpointConfiguration();
        endpointConfiguration.setCamelContext(camelContext);
        endpointConfiguration.setEndpointUri(endpointUri);

        CamelEndpoint camelEndpoint = new CamelEndpoint(endpointConfiguration);

        Message requestMessage = new org.citrusframework.message.DefaultMessage("Hello from Citrus!");

        reset(camelContext, producerTemplate, exchange);

        when(camelContext.createProducerTemplate()).thenReturn(producerTemplate);
        when(camelContext.getCamelContextExtension()).thenReturn(extendedCamelContext);
        when(extendedCamelContext.getHeadersMapFactory()).thenReturn(new DefaultHeadersMapFactory());
        when(producerTemplate.send(eq(endpointUri), any(Processor.class))).thenReturn(exchange);
        when(exchange.getException()).thenReturn(null);

        camelEndpoint.createProducer().send(requestMessage, context);
    }

    @Test(expectedExceptions = CitrusRuntimeException.class)
    public void testCamelEndpointProducerWithInternalException() {
        String endpointUri = "direct:news-feed";
        CamelEndpointConfiguration endpointConfiguration = new CamelEndpointConfiguration();
        endpointConfiguration.setCamelContext(camelContext);
        endpointConfiguration.setEndpointUri(endpointUri);

        CamelEndpoint camelEndpoint = new CamelEndpoint(endpointConfiguration);

        Message requestMessage = new org.citrusframework.message.DefaultMessage("Hello from Citrus!");

        CamelExchangeException exchangeException = new CamelExchangeException("Failed", exchange);

        reset(camelContext, producerTemplate, exchange);

        when(camelContext.createProducerTemplate()).thenReturn(producerTemplate);
        when(camelContext.getCamelContextExtension()).thenReturn(extendedCamelContext);
        when(extendedCamelContext.getHeadersMapFactory()).thenReturn(new DefaultHeadersMapFactory());
        when(producerTemplate.send(eq(endpointUri), any(Processor.class))).thenReturn(exchange);
        when(exchange.getException()).thenReturn(exchangeException);

        camelEndpoint.createProducer().send(requestMessage, context);
    }

    @Test
    public void testCamelEndpointConsumer() {
        String endpointUri = "direct:news-feed";
        CamelEndpointConfiguration endpointConfiguration = new CamelEndpointConfiguration();
        endpointConfiguration.setCamelContext(camelContext);
        endpointConfiguration.setEndpointUri(endpointUri);

        CamelEndpoint camelEndpoint = new CamelEndpoint(endpointConfiguration);

        reset(camelContext, consumerTemplate);

        when(camelContext.createConsumerTemplate()).thenReturn(consumerTemplate);
        when(camelContext.getCamelContextExtension()).thenReturn(extendedCamelContext);
        when(extendedCamelContext.getHeadersMapFactory()).thenReturn(new DefaultHeadersMapFactory());
        when(camelContext.getUuidGenerator()).thenReturn(new SimpleUuidGenerator());

        DefaultMessage message = new DefaultMessage(camelContext);
        message.setBody("Hello from Camel!");
        message.setHeader("operation", "newsFeed");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.setIn(message);

        when(consumerTemplate.receive(endpointUri, endpointConfiguration.getTimeout())).thenReturn(exchange);

        Message receivedMessage = camelEndpoint.createConsumer().receive(context, endpointConfiguration.getTimeout());
        Assert.assertEquals(receivedMessage.getPayload(), "Hello from Camel!");
        Assert.assertEquals(receivedMessage.getHeader("operation"), "newsFeed");
        Assert.assertNotNull(receivedMessage.getHeader(CamelMessageHeaders.EXCHANGE_ID));
        Assert.assertNotNull(receivedMessage.getHeader(CamelMessageHeaders.EXCHANGE_PATTERN));
        Assert.assertNotNull(receivedMessage.getHeader(CamelMessageHeaders.EXCHANGE_FAILED));
    }

    @Test
    public void testCamelEndpointWithMessageListeners() {
        String endpointUri = "direct:news-feed";
        CamelEndpointConfiguration endpointConfiguration = new CamelEndpointConfiguration();
        endpointConfiguration.setCamelContext(camelContext);
        endpointConfiguration.setEndpointUri(endpointUri);

        CamelEndpoint camelEndpoint = new CamelEndpoint(endpointConfiguration);

        Message requestMessage = new org.citrusframework.message.DefaultMessage("Hello from Citrus!");

        DefaultMessage message = new DefaultMessage(camelContext);
        message.setBody("Hello from Camel!");
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.setIn(message);

        context.setMessageListeners(messageListeners);

        reset(camelContext, producerTemplate, consumerTemplate, messageListeners);

        when(camelContext.createProducerTemplate()).thenReturn(producerTemplate);
        when(camelContext.getCamelContextExtension()).thenReturn(extendedCamelContext);
        when(extendedCamelContext.getHeadersMapFactory()).thenReturn(new DefaultHeadersMapFactory());
        when(producerTemplate.send(eq(endpointUri), any(Processor.class))).thenReturn(exchange);

        when(camelContext.createConsumerTemplate()).thenReturn(consumerTemplate);
        when(camelContext.getUuidGenerator()).thenReturn(new SimpleUuidGenerator());
        when(consumerTemplate.receive(endpointUri, endpointConfiguration.getTimeout())).thenReturn(exchange);

        when(messageListeners.isEmpty()).thenReturn(false);
        doAnswer(invocation -> {
            Message inboundMessage = (Message) invocation.getArguments()[0];
            Assert.assertTrue(inboundMessage.getPayload(String.class).contains("Hello from Camel!"));
            return null;
        }).when(messageListeners).onInboundMessage(any(Message.class), eq(context));

        camelEndpoint.createProducer().send(requestMessage, context);
        camelEndpoint.createConsumer().receive(context, 5000L);

        verify(messageListeners).onOutboundMessage(requestMessage, context);
    }
}
