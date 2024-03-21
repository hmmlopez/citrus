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

package org.citrusframework.restdocs.config.xml;

import org.citrusframework.restdocs.http.RestDocClientInterceptor;
import org.citrusframework.restdocs.http.RestDocRequestConverter;
import org.citrusframework.restdocs.http.RestDocResponseConverter;
import org.citrusframework.restdocs.soap.RestDocSoapClientInterceptor;
import org.citrusframework.restdocs.soap.RestDocSoapRequestConverter;
import org.citrusframework.restdocs.soap.RestDocSoapResponseConverter;
import org.citrusframework.util.StringUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.w3c.dom.Element;

/**
 * @author Christoph Deppisch
 * @since 2.6
 */
public class RestDocClientInterceptorParser extends AbstractBeanDefinitionParser {

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder;

        String identifier = element.getAttribute("identifier");
        String docType = element.getAttribute("type");
        if (StringUtils.hasText(docType) && docType.equals("soap")) {
            builder = BeanDefinitionBuilder.rootBeanDefinition(RestDocSoapClientInterceptor.class);
            builder.addConstructorArgValue(new RestDocumentationGenerator<>(identifier,
                    new RestDocSoapRequestConverter(), new RestDocSoapResponseConverter()));
        } else {
            builder = BeanDefinitionBuilder.rootBeanDefinition(RestDocClientInterceptor.class);
            builder.addConstructorArgValue(new RestDocumentationGenerator<>(identifier,
                    new RestDocRequestConverter(), new RestDocResponseConverter()));
        }

        return builder.getBeanDefinition();
    }
}
