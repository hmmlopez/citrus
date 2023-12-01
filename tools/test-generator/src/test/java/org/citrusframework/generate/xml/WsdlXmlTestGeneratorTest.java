/*
 * Copyright 2006-2018 the original author or authors.
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

package org.citrusframework.generate.xml;

import java.io.File;
import java.io.IOException;

import org.citrusframework.CitrusSettings;
import org.citrusframework.generate.UnitFramework;
import org.citrusframework.util.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 * @since 2.7.4
 */
public class WsdlXmlTestGeneratorTest {

    @Test
    public void testCreateTest() throws IOException {
        WsdlXmlTestGenerator generator = new WsdlXmlTestGenerator();

        generator.withAuthor("Christoph")
                .withDescription("This is a sample test")
                .usePackage("org.citrusframework")
                .withFramework(UnitFramework.TESTNG);

        generator.withWsdl("org/citrusframework/wsdl/BookStore.wsdl");

        generator.create();

        verifyTest("BookStore_addBook_IT", "book:addBook", "book:addBookResponse");
        verifyTest("BookStore_addBookAudio_IT", "aud:addBookAudio", "aud:addBookAudioResponse");
        verifyTest("BookStore_deleteBook_IT", "book:deleteBook", "book:deleteBookResponse");
    }

    private void verifyTest(String name, String requestName, String responseName) throws IOException {
        File javaFile = new File(CitrusSettings.DEFAULT_TEST_SRC_DIRECTORY + "java/org/citrusframework/" +
                name + FileUtils.FILE_EXTENSION_JAVA);
        Assert.assertTrue(javaFile.exists());

        File xmlFile = new File(CitrusSettings.DEFAULT_TEST_SRC_DIRECTORY + "resources/org/citrusframework/" +
                name + FileUtils.FILE_EXTENSION_XML);
        Assert.assertTrue(xmlFile.exists());

        String javaContent = FileUtils.readToString(javaFile);
        Assert.assertTrue(javaContent.contains("@author Christoph"));
        Assert.assertTrue(javaContent.contains("public class " + name));
        Assert.assertTrue(javaContent.contains("* This is a sample test"));
        Assert.assertTrue(javaContent.contains("package org.citrusframework;"));
        Assert.assertTrue(javaContent.contains("extends TestNGCitrusSupport"));

        String xmlContent = FileUtils.readToString(xmlFile);
        Assert.assertTrue(xmlContent.contains("<author>Christoph</author>"));
        Assert.assertTrue(xmlContent.contains("<description>This is a sample test</description>"));
        Assert.assertTrue(xmlContent.contains("<testcase name=\"" + name + "\">"));
        Assert.assertTrue(xmlContent.contains("<data>&lt;" + requestName));
        Assert.assertTrue(xmlContent.contains("<data>&lt;" + responseName));
    }

}
