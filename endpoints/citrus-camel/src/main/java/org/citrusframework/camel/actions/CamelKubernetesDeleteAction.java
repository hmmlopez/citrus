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

package org.citrusframework.camel.actions;

import org.citrusframework.context.TestContext;
import org.citrusframework.spi.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Delete kubernetes resources deployed from a Camel project or Camel integration with Camel JBang tooling.
 */
public class CamelKubernetesDeleteAction extends AbstractCamelJBangAction {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(CamelKubernetesDeleteAction.class);

    /**
     * Camel integration resource
     */
    private final Resource integrationResource;

    /**
     * Camel integration name
     */
    private final String integrationName;

    /**
     * Camel Jbang cluster type target
     */
    private final String clusterType;

    /**
     * Project directory
     */
    private final String workingDir;

    /**
     * Kubernetes Namespace
     */
    private final String namespace;

    /**
     * Default constructor.
     */
    public CamelKubernetesDeleteAction(Builder builder) {
        super("delete-kubernetes", builder);

        this.integrationResource = builder.integrationResource;
        this.integrationName = builder.integrationName;
        this.clusterType = builder.clusterType;
        this.workingDir = builder.workingDir;
        this.namespace = builder.namespace;

    }

    @Override
    public void doExecute(TestContext context) {
        logger.info("Deleting integration deployed from a Camel Kubernetes project  ...");
        List<String> fullArgs = new ArrayList<>();
        fullArgs.add("delete");
        if (integrationResource != null) {
            fullArgs.add(integrationResource.getFile().toPath().toAbsolutePath().toString());
        }
        if (integrationName != null) {
            fullArgs.add("--name");
            fullArgs.add(integrationName);
        }
        if (clusterType != null) {
            fullArgs.add("--cluster-type");
            fullArgs.add(clusterType);
        }
        if (workingDir != null) {
            fullArgs.add("--working-dir");
            fullArgs.add(workingDir);
        }
        if (namespace != null) {
            fullArgs.add("--namespace");
            fullArgs.add(namespace);
        }
        camelJBang().camelApp().run("kubernetes", fullArgs.toArray(String[]::new));

    }

    public Resource getIntegrationResource() {
        return integrationResource;
    }

    public String getIntegrationName() {
        return integrationName;
    }

    public String getClusterType() {
        return clusterType;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public String getNamespace() {
        return namespace;
    }

    /**
     * Action builder.
     */
    public static final class Builder extends AbstractCamelJBangAction.Builder<CamelKubernetesDeleteAction, CamelKubernetesDeleteAction.Builder> {

        private Resource integrationResource;
        private String integrationName;
        private String clusterType;
        private String workingDir;
        private String namespace;


        /**
         * Delete Camel JBang kubernetes resources integration resource.
         *
         * @param resource
         * @return
         */
        public Builder integration(Resource resource) {
            this.integrationResource = resource;
            return this;
        }

        /**
         * Delete Camel JBang kubernetes resources for this integration.
         *
         * @param name
         * @return
         */
        public Builder integration(String name) {
            this.integrationName = name;
            return this;
        }

        /**
         * Set cluster type target.
         *
         * @param clusterType
         * @return
         */
        public Builder clusterType(String clusterType) {
            this.clusterType = clusterType;
            return this;
        }

        /**
         * The working directory where to find exported project sources.
         *
         * @param dir directory path
         * @return
         */
        public Builder workingDir(String dir) {
            this.workingDir = dir;
            return this;
        }

        /**
         * The Namespace wherethe kubernetes resources are deployed.
         *
         * @param namespace namespace
         * @return
         */
        public Builder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        @Override
        public CamelKubernetesDeleteAction build() {
            return new CamelKubernetesDeleteAction(this);
        }
    }
}
