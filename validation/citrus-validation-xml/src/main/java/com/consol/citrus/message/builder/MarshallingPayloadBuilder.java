/*
 * Copyright 2020 the original author or authors.
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

package com.consol.citrus.message.builder;

import java.io.IOException;
import java.util.Map;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.xml.StringResult;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

/**
 * @author Christoph Deppisch
 */
public class MarshallingPayloadBuilder extends DefaultPayloadBuilder {

    private final Marshaller marshaller;
    private final String marshallerName;

    /**
     * Default constructor using just model object.
     * @param model
     */
    public MarshallingPayloadBuilder(Object model) {
        super(model);

        this.marshaller = null;
        this.marshallerName = null;
    }

    /**
     * Default constructor using object marshaller and model object.
     * @param model
     * @param marshaller
     */
    public MarshallingPayloadBuilder(Object model, Marshaller marshaller) {
        super(model);

        this.marshaller = marshaller;
        this.marshallerName = null;
    }

    /**
     * Default constructor using object marshaller name and model object.
     * @param model
     * @param marshallerName
     */
    public MarshallingPayloadBuilder(Object model, String marshallerName) {
        super(model);

        this.marshallerName = marshallerName;
        this.marshaller = null;
    }

    @Override
    public Object buildPayload(TestContext context) {
        if (getPayload() == null || getPayload() instanceof String) {
            return super.buildPayload(context);
        }

        if (marshaller != null) {
            return buildPayload(marshaller, getPayload());
        }

        if (marshallerName != null) {
            if (context.getReferenceResolver().isResolvable(marshallerName)) {
                Marshaller objectMapper = context.getReferenceResolver().resolve(marshallerName, Marshaller.class);
                return buildPayload(objectMapper, getPayload());
            } else {
                throw new CitrusRuntimeException(String.format("Unable to find proper object marshaller for name '%s'", marshallerName));
            }
        }

        Map<String, Marshaller> marshallerMap = context.getReferenceResolver().resolveAll(Marshaller.class);
        if (marshallerMap.size() == 1) {
            return buildPayload(marshallerMap.values().iterator().next(), getPayload());
        } else {
            throw new CitrusRuntimeException(String.format("Unable to auto detect object marshaller - " +
                    "found %d matching marshaller instances in reference resolver", marshallerMap.size()));
        }
    }

    private Object buildPayload(Marshaller marshaller, Object model) {
        final StringResult result = new StringResult();

        try {
            marshaller.marshal(model, result);
        } catch (final XmlMappingException | IOException e) {
            throw new CitrusRuntimeException("Failed to marshal object graph for message payload", e);
        }

        return result.toString();
    }


}
