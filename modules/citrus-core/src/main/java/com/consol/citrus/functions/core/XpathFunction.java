/*
 * Copyright 2006-2016 the original author or authors.
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

package com.consol.citrus.functions.core;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.InvalidFunctionUsageException;
import com.consol.citrus.functions.Function;
import com.consol.citrus.util.XMLUtils;
import com.consol.citrus.xml.xpath.XPathUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.xml.namespace.SimpleNamespaceContext;

import java.util.List;

/**
 * @author Christoph Deppisch
 * @since 2.6.2
 */
public class XpathFunction implements Function {

    @Override
    public String execute(List<String> parameterList, TestContext context) {
        if (CollectionUtils.isEmpty(parameterList)) {
            throw new InvalidFunctionUsageException("Function parameters must not be empty");
        }

        if (parameterList.size() < 2) {
            throw new InvalidFunctionUsageException("Missing parameter for function - usage xpath('xmlSource', 'expression')");
        }

        String xmlSource = parameterList.get(0);
        String xpathExpression = parameterList.get(1);

        SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
        namespaceContext.setBindings(context.getNamespaceContextBuilder().getNamespaceMappings());
        return XPathUtils.evaluateAsString(XMLUtils.parseMessagePayload(context.replaceDynamicContentInString(xmlSource)),
                context.replaceDynamicContentInString(xpathExpression), namespaceContext);
    }
}
