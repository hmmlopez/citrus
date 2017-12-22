/*
 * Copyright 2006-2017 the original author or authors.
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

package com.consol.citrus.jdbc.server;

import com.consol.citrus.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;

/**
 * @author Christoph Deppisch
 * @since 2.7.3
 */
public interface RemoteDriver extends Remote, RemoteConnection, RemoteStatement {

    /**
     * Get connection from db server.
     * @param properties
     * @return
     * @throws RemoteException
     */
    RemoteConnection getConnection(Properties properties) throws RemoteException;

    /**
     * Process request message and create response.
     * @param request
     * @return
     * @throws RemoteException
     */
    Message process(Message request) throws RemoteException;
}