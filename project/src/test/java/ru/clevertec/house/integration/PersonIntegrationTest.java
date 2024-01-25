package ru.clevertec.house.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.clevertec.house.config.TestConfig;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.util.RequestPersonTest;
import ru.clevertec.house.util.TestConstant;


import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private final RequestPersonTest requestPersonTest = new RequestPersonTest();

    @Test
    void testGetPerson() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/persons/5c3a267c-3175-4826-a7d1-488782a62d98")
                        .contentType("application/json"))
                .andReturn();
        ResponsePerson actual = objectMapper.readValue(result.getResponse().getContentAsString(), ResponsePerson.class);

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.name()).isEqualTo(TestConstant.NAME);
        assertThat(actual.surname()).isEqualTo(TestConstant.SURNAME);
        assertThat(actual.uuid()).isEqualTo(TestConstant.PERSON_UUID);
        assertThat(actual.sex()).isEqualTo(TestConstant.SEX);
        assertThat(actual.passport().getSeries()).isEqualTo("AB");
        assertThat(actual.passport().getNumber()).isEqualTo(TestConstant.PASSPORT_NUMBER);
        assertThat(actual.createDate()).isEqualTo(TestConstant.DATE_TIME);
    }

    @Test
    void testCreatePerson() throws Exception {
        RequestPerson expected = requestPersonTest.build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/persons")
                        .contentType("application/json")
                        .param("houseUuid", "e96346e9-9239-44f8-be11-fcbc86a541eb")
                        .content(objectMapper.writeValueAsString(expected)))
                .andReturn();
        RequestPerson actual = objectMapper.readValue(result.getResponse().getContentAsString(), RequestPerson.class);

        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }

    @Test
    void testUpdatePerson() throws Exception {

        String jsonForTest = "{\n" +
                "    \"name\": \"John1\",\n" +
                "    \"surname\": \"Doe1\",\n" +
                "    \"sex\": \"MALE\",\n" +
                "    \"passport\": {\n" +
                "        \"series\": \"A2\",\n" +
                "        \"number\": \"123456\"\n" +
                "    },\n" +
                "    \"house_id\": 1\n" +
                "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/persons/5ec11abf-be3d-440d-893b-d6471c2d4c2b")
                        .contentType("application/json")
                        .content(jsonForTest))
                .andReturn();
        ResponsePerson actual = objectMapper.readValue(result.getResponse().getContentAsString(), ResponsePerson.class);

        assertThat(actual.name()).isEqualTo("John1");
        assertThat(actual.surname()).isEqualTo("Doe1");
        assertThat(actual.sex().name()).isEqualTo("MALE");
        assertThat(actual.passport().getNumber()).isEqualTo("123456");
        assertThat(actual.passport().getSeries()).isEqualTo("A2");
        assertThat(actual.uuid()).isEqualTo(UUID.fromString("5ec11abf-be3d-440d-893b-d6471c2d4c2b"));
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void testDeletePerson() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/persons/9eeec6ea-a762-42e2-b472-02a5e11f6dd5")
                        .contentType("application/json"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);

        result = mockMvc.perform(MockMvcRequestBuilders.get("/persons/9eeec6ea-a762-42e2-b472-02a5e11f6dd5")
                        .contentType("application/json"))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    void testGetAllPersons() throws Exception {
        int pageSize = 3;
        int numberPage = 2;
        String expected = "{\"pageNumber\":2,\"countPage\":4,\"content\":[{\"uuid\":\"118745f9-1abd-4a9e-b006-4083ec51f4c7" +
                "\",\"name\":\"Emily\",\"surname\":\"Johnson\",\"sex\":\"FEMALE\",\"passport\":{\"series\":\"DD\"," +
                "\"number\":\"222222\"},\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":" +
                "\"2024-01-16T14:18:08.537\"},{\"uuid\":\"07d6cc4a-ef44-4d69-9d36-54303c04d675\",\"name\":\"William\"," +
                "\"surname\":\"Brown\",\"sex\":\"MALE\",\"passport\":{\"series\":\"EE\",\"number\":\"333333\"}," +
                "\"createDate\"" +
                ":\"2024-01-16T14:18:08.537\",\"updateDate\":\"2024-01-16T14:18:08.537\"},{\"uuid\":" +
                "\"209fd31f-dbf2-4e5a-87b6-1d874e9d6753\",\"name\":\"Emma\",\"surname\":\"Martinez\",\"sex\":\"FEMALE\"," +
                "\"passport\":{\"series\":\"FF\",\"number\":\"444444\"},\"createDate\":\"2024-01-16T14:18:08.537\"," +
                "\"updateDate\":\"2024-01-16T14:18:08.537\"}]}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/persons/all")
                        .contentType("application/json")
                        .param("pageSize", String.valueOf(pageSize))
                        .param("numberPage", String.valueOf(numberPage)))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void testGetPersonByUUidHouse() throws Exception {
        String expected = "[{\"uuid\":\"5c3a267c-3175-4826-a7d1-488782a62d98\",\"name\":\"John\",\"surname\":" +
                "\"Doe\",\"sex\":\"MALE\",\"passport\":{\"series\":\"AB\",\"number\":\"123456\"}," +
                "\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":\"2024-01-16T14:18:08.537\"}," +
                "{\"uuid\":\"209fd31f-dbf2-4e5a-87b6-1d874e9d6753\",\"name\":\"Emma\",\"surname\":\"Martinez\"," +
                "\"sex\":\"FEMALE\",\"passport\":{\"series\":\"FF\",\"number\":\"444444\"},\"createDate\":" +
                "\"2024-01-16T14:18:08.537\",\"updateDate\":\"2024-01-16T14:18:08.537\"}]";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/persons")
                        .contentType("application/json")
                        .param("house_uuid", TestConstant.HOUSE_UUID.toString()))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void testPatchPerson() throws Exception {
        Map<String, Object> updates = Map.of("name", "John", "surname", "Bing");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/persons/80a08efa-20d5-487e-af10-a33dc3f53834")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updates)))
                .andReturn();
        ResponsePerson actual = objectMapper.readValue(result.getResponse().getContentAsString(), ResponsePerson.class);

        assertThat(actual.name()).isEqualTo("John");
        assertThat(actual.surname()).isEqualTo("Bing");
        assertThat(actual.uuid()).isEqualTo(UUID.fromString("80a08efa-20d5-487e-af10-a33dc3f53834"));
        assertThat(actual.sex().name()).isEqualTo("MALE");
        assertThat(actual.passport().getSeries()).isEqualTo("GG");
        assertThat(actual.passport().getNumber()).isEqualTo("555555");
        assertThat(actual.createDate()).isEqualTo("2024-01-16T14:18:08.537");
    }
}
