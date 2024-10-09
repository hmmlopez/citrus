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

package org.citrusframework.config.xml;

import org.citrusframework.config.util.BeanDefinitionParserUtils;
import org.citrusframework.xml.XsdSchemaRepository;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class XsdSchemaRepositoryParser extends AbstractBeanDefinitionParser {
    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(XsdSchemaRepository.class);
        BeanDefinitionParserUtils.setPropertyReference(builder, element.getAttribute("schema-mapping-strategy"), "schemaMappingStrategy");
        SchemaRepositoryParser.addLocationsToBuilder(element, builder);
        SchemaRepositoryParser.parseSchemasElement(element, builder, parserContext);
        return builder.getBeanDefinition();
    }
}
