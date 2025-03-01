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

package org.citrusframework.testcontainers;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import org.citrusframework.TestActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.shaded.com.github.dockerjava.core.DefaultDockerClientConfig;
import org.testcontainers.shaded.com.github.dockerjava.core.DockerClientConfig;
import org.testcontainers.shaded.com.github.dockerjava.core.DockerClientImpl;

/**
 * Test actor disabled when running a host where no Docker compatible engine is available.
 */
public class TestcontainersActor extends TestActor {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(TestcontainersActor.class);

    /** Docker's connection state, checks connectivity to Docker engine */
    private static AtomicBoolean connected;

    public TestcontainersActor() {
        this(null);
    }

    public TestcontainersActor(DockerClient dockerClient) {
        super("testcontainers");

        synchronized (logger) {
            if (connected == null) {
                if (dockerClient != null) {
                    connected = new AtomicBoolean(verifyConnected(dockerClient));
                } else {
                    DockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
                    DockerClient tempClient = DockerClientImpl.getInstance(clientConfig,
                            new OkDockerHttpClient.Builder().dockerHost(clientConfig.getDockerHost()).build()
                    );
                    connected = new AtomicBoolean(verifyConnected(tempClient));
                }
            }
        }
    }

    @Override
    public boolean isDisabled() {
        if (!TestContainersSettings.isEnabled()) {
            return true;
        }

        return !connected.get() || super.isDisabled();
    }

    public static boolean verifyConnected(DockerClient dockerClient) {
        try {
            Future<Boolean> future = Executors.newSingleThreadExecutor().submit(() -> {
                dockerClient.pingCmd().exec();
                return true;
            });

            return (future.get(TestContainersSettings.getConnectTimeout(), TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            logger.warn("Skipping Docker test execution as no proper Docker environment is available on host system!", e);
            return false;
        }
    }

    public static void resetConnectionState() {
        synchronized (logger) {
            connected = null;
        }
    }
}
