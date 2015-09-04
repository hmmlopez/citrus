/*
 * Copyright 2006-2013 the original author or authors.
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

package com.consol.citrus.admin.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;

/**
 * Logging WebSocket
 *
 * @author Martin.Maher@consol.de
 * @since 1.3
 */
public class LoggingWebSocketServlet extends WebSocketServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingWebSocketServlet.class);

    private WebSocketCreator webSocketCreator;

    @Override
    public void configure(WebSocketServletFactory factory) {
        LOG.info("Configuring the websocket");
        factory.setCreator(webSocketCreator);
    }

    @Override
    public void init() throws ServletException {
        LOG.info("Initialising the websocket");
        webSocketCreator = getWebSocketCreator();
        super.init();
    }

    private WebSocketCreator getWebSocketCreator() {
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        return springContext.getBean(LoggingWebSocketCreator.class);
    }
}
