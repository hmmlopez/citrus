package com.consol.citrus.validation.json;

import com.consol.citrus.json.JsonSchemaRepository;
import com.consol.citrus.json.schema.SimpleJsonSchema;
import com.consol.citrus.message.DefaultMessage;
import com.consol.citrus.message.Message;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonSchemaValidationTest {

    private JsonSchemaValidation validator = new JsonSchemaValidation();

    @Test
    public void testValidJsonMessageSuccessfullyValidated() throws Exception{

        //GIVEN
        JsonSchemaRepository jsonSchemaRepository = new JsonSchemaRepository();

        jsonSchemaRepository.setBeanName("schemaRepository1");
        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/ProductsSchema.json");
        SimpleJsonSchema schema = new SimpleJsonSchema(schemaResource);
        schema.afterPropertiesSet();

        jsonSchemaRepository.getSchemas().add(schema);




        Message receivedMessage = new DefaultMessage("[\n" +
                "              {\n" +
                "                \"id\": 2,\n" +
                "                \"name\": \"An ice sculpture\",\n" +
                "                \"price\": 12.50,\n" +
                "                \"tags\": [\"cold\", \"ice\"],\n" +
                "                \"dimensions\": {\n" +
                "                \"length\": 7.0,\n" +
                "                \"width\": 12.0,\n" +
                "                \"height\": 9.5\n" +
                "                 },\n" +
                "                 \"warehouseLocation\": {\n" +
                "                   \"latitude\": -78.75,\n" +
                "                   \"longitude\": 20.4\n" +
                "                 }\n" +
                "              }\n" +
                "            ]");


        //WHEN
        ProcessingReport report = validator.validate(receivedMessage, jsonSchemaRepository);


        //THEN
        Assert.assertTrue(report.isSuccess());
    }

    @Test
    public void testInvalidJsonMessageValidationIsNotSuccessful() throws Exception {

        //GIVEN
        JsonSchemaRepository jsonSchemaRepository = new JsonSchemaRepository();
        jsonSchemaRepository.setBeanName("schemaRepository1");
        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/ProductsSchema.json");
        SimpleJsonSchema schema = new SimpleJsonSchema(schemaResource);
        schema.afterPropertiesSet();
        jsonSchemaRepository.getSchemas().add(schema);

        Message receivedMessage = new DefaultMessage("[\n" +
                "              {\n" +
                "                \"name\": \"An ice sculpture\",\n" +
                "                \"price\": 12.50,\n" +
                "                \"tags\": [\"cold\", \"ice\"],\n" +
                "                \"dimensions\": {\n" +
                "                \"length\": 7.0,\n" +
                "                \"width\": 12.0,\n" +
                "                \"height\": 9.5\n" +
                "                 },\n" +
                "                 \"warehouseLocation\": {\n" +
                "                   \"latitude\": -78.75,\n" +
                "                   \"longitude\": 20.4\n" +
                "                 }\n" +
                "              }\n" +
                "            ]");


        //WHEN
        ProcessingReport report = validator.validate(receivedMessage, jsonSchemaRepository);


        //THEN
        Assert.assertFalse(report.isSuccess());
    }

    @Test
    public void testValidationIsSuccessfulIfOneSchemaMatches() throws Exception {

        //GIVEN
        JsonSchemaRepository jsonSchemaRepository = new JsonSchemaRepository();
        jsonSchemaRepository.setBeanName("schemaRepository1");

        Resource schemaResource = new ClassPathResource("com/consol/citrus/validation/BookSchema.json");
        SimpleJsonSchema schema = new SimpleJsonSchema(schemaResource);
        schema.afterPropertiesSet();
        jsonSchemaRepository.getSchemas().add(schema);

        schemaResource = new ClassPathResource("com/consol/citrus/validation/ProductsSchema.json");
        schema = new SimpleJsonSchema(schemaResource);
        schema.afterPropertiesSet();
        jsonSchemaRepository.getSchemas().add(schema);

        Message receivedMessage = new DefaultMessage("[\n" +
                "              {\n" +
                "                \"id\": 2,\n" +
                "                \"name\": \"An ice sculpture\",\n" +
                "                \"price\": 12.50,\n" +
                "                \"tags\": [\"cold\", \"ice\"],\n" +
                "                \"dimensions\": {\n" +
                "                \"length\": 7.0,\n" +
                "                \"width\": 12.0,\n" +
                "                \"height\": 9.5\n" +
                "                 },\n" +
                "                 \"warehouseLocation\": {\n" +
                "                   \"latitude\": -78.75,\n" +
                "                   \"longitude\": 20.4\n" +
                "                 }\n" +
                "              }\n" +
                "            ]");


        //WHEN
        ProcessingReport report = validator.validate(receivedMessage, jsonSchemaRepository);


        //THEN
        Assert.assertTrue(report.isSuccess());
    }

}