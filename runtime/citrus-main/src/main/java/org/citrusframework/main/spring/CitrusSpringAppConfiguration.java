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

package org.citrusframework.main.spring;

import java.util.Map;
import java.util.Optional;

import org.citrusframework.CitrusSpringSettings;
import org.citrusframework.main.CitrusAppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christoph Deppisch
 */
public class CitrusSpringAppConfiguration extends CitrusAppConfiguration {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(CitrusSpringAppConfiguration.class);

    @Override
    public void setDefaultProperties() {
        for (Map.Entry<String, String> entry : getDefaultProperties().entrySet()) {
            logger.debug(String.format("Setting application property %s=%s", entry.getKey(), entry.getValue()));
            System.setProperty(entry.getKey(), Optional.ofNullable(entry.getValue()).orElse(""));
        }

        if (getConfigClass() != null) {
            System.setProperty(CitrusSpringSettings.DEFAULT_APPLICATION_CONTEXT_CLASS_PROPERTY, getConfigClass());
        }
    }
}
