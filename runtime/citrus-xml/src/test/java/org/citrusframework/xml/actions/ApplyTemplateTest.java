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

package org.citrusframework.xml.actions;

import org.citrusframework.TestCase;
import org.citrusframework.TestCaseMetaInfo;
import org.citrusframework.container.Template;
import org.citrusframework.xml.XmlTestLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.citrusframework.actions.EchoAction.Builder.echo;

public class ApplyTemplateTest extends AbstractXmlActionTest {

    @Test
    public void shouldLoadApplyTemplate() {
        XmlTestLoader testLoader = createTestLoader("classpath:org/citrusframework/xml/actions/apply-template-test.xml");

        context.getReferenceResolver().bind("myTemplate", new Template.Builder()
                .actions(echo().message("Hello from Citrus!"))
                .build());

        Template printTemplate = new Template.Builder()
                .parameter("text", "Should be overwritten")
                .actions(echo().message("${text}"), echo().message("${message}"))
                .build();
        context.getReferenceResolver().bind("print", printTemplate);

        testLoader.load();
        TestCase result = testLoader.getTestCase();
        Assert.assertEquals(result.getName(), "ApplyTemplateTest");
        Assert.assertEquals(result.getMetaInfo().getAuthor(), "Christoph");
        Assert.assertEquals(result.getMetaInfo().getStatus(), TestCaseMetaInfo.Status.FINAL);
        Assert.assertEquals(result.getActionCount(), 3L);
        Assert.assertEquals(result.getTestAction(0).getClass(), Template.class);

        Template action = (Template) result.getTestAction(0);
        Assert.assertEquals(action.getName(), "template:myTemplate");
        Assert.assertEquals(action.getTemplateName(), "myTemplate");

        action = (Template) result.getTestAction(1);
        Assert.assertEquals(action.getName(), "template:print");
        Assert.assertEquals(action.getTemplateName(), "print");
        Assert.assertEquals(action.getParameter().size(), 2);
        Assert.assertEquals(action.getParameter().get("text"), "Hello from Citrus!");
        Assert.assertTrue(action.getParameter().get("message").contains("<Text>Hello from Citrus!</Text>"));

        Assert.assertEquals(printTemplate.getParameter().size(), 1L);
        Assert.assertEquals(printTemplate.getParameter().get("text"), "Should be overwritten");

        action = (Template) result.getTestAction(2);
        Assert.assertEquals(action.getName(), "template:echo");
        Assert.assertEquals(action.getTemplateName(), "echo");

        Assert.assertFalse(context.getReferenceResolver().isResolvable("echo", Template.class));
        Assert.assertFalse(context.getReferenceResolver().isResolvable("echo", Template.Builder.class));
    }
}
