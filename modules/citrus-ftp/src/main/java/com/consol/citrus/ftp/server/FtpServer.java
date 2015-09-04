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

package com.consol.citrus.ftp.server;

import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.server.AbstractServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christoph Deppisch
 * @since 2.0
 */
public class FtpServer extends AbstractServer implements InitializingBean {

    /** Apache ftp server */
    private FtpServerFactory serverFactory;
    private ListenerFactory listenerFactory;
    private UserManager userManager;
    private org.apache.ftpserver.FtpServer ftpServer;

    /** Server port */
    private int port = 22222;

    /** Property file holding ftp user information */
    private Resource userManagerProperties;

    /** Do only start one instance after another so we need a static lock object */
    private static Object serverLock = new Object();

    @Override
    protected void startup() {
        synchronized (serverLock) {
            if (ftpServer == null) {
                listenerFactory.setPort(port);
                serverFactory.addListener("default", listenerFactory.createListener());

                if (userManager != null) {
                    serverFactory.setUserManager(userManager);
                } else if (userManagerProperties != null) {
                    PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
                    try {
                        userManagerFactory.setFile(userManagerProperties.getFile());
                    } catch (IOException e) {
                        throw new CitrusRuntimeException("Failed to load user manager properties", e);
                    }
                    serverFactory.setUserManager(userManagerFactory.createUserManager());
                }

                NativeFileSystemFactory fileSystemFactory = new NativeFileSystemFactory();
                fileSystemFactory.setCreateHome(true);
                serverFactory.setFileSystem(fileSystemFactory);

                Map<String, Ftplet> ftpLets = new HashMap<String, Ftplet>();
                ftpLets.put("citrusFtpLet", new FtpServerFtpLet(getEndpointAdapter()));
                serverFactory.setFtplets(ftpLets);

                ftpServer =serverFactory.createServer();
            }

            try {
                ftpServer.start();
            } catch (FtpException e) {
                throw new CitrusRuntimeException(e);
            }
        }
    }

    @Override
    protected void shutdown() {
        if (ftpServer != null) {
            try {
                synchronized (serverLock) {
                    ftpServer.stop();
                }
            } catch (Exception e) {
                throw new CitrusRuntimeException(e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (ftpServer == null) {
            if (serverFactory == null) {
                serverFactory = new FtpServerFactory();
            }

            if (listenerFactory == null) {
                listenerFactory = new ListenerFactory();
            }
        }

        super.afterPropertiesSet();
    }

    /**
     * Sets the server port.
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the server port.
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets custom ftp server instance.
     * @param ftpServer
     */
    public void setFtpServer(org.apache.ftpserver.FtpServer ftpServer) {
        this.ftpServer = ftpServer;
    }

    /**
     * Gets ftp server instance.
     * @return
     */
    public org.apache.ftpserver.FtpServer getFtpServer() {
        return ftpServer;
    }

    /**
     * Sets custom user manager.
     * @param userManager
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Gets the user manager.
     * @return
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the user manager properties.
     * @param userManagerProperties
     */
    public void setUserManagerProperties(Resource userManagerProperties) {
        this.userManagerProperties = userManagerProperties;
    }

    /**
     * Gets the user manager properties.
     * @return
     */
    public Resource getUserManagerProperties() {
        return userManagerProperties;
    }

    /**
     * Sets custom listener factory.
     * @param listenerFactory
     */
    public void setListenerFactory(ListenerFactory listenerFactory) {
        this.listenerFactory = listenerFactory;
    }

    /**
     * Gets the listener factory.
     * @return
     */
    public ListenerFactory getListenerFactory() {
        return listenerFactory;
    }
}
