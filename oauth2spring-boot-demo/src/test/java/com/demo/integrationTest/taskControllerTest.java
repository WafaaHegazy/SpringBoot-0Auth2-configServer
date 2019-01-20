package com.demo.integrationTest;

import com.demo.dao.TaskRepository;
import com.demo.model.Task;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class taskControllerTest {
    private static final String DEFAULT_TASK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TASK_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TASK_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    /**
     * MediaType for JSON UTF8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskRepository taskRepository;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method for creating task entity, as tests for other entities might also need it,
     */
    public static Task createEntity() {
        Task task = new Task();
        task.setTaskName(DEFAULT_TASK_NAME);
        task.setTaskDescription(DEFAULT_TASK_DESCRIPTION);
        task.setCompleted(DEFAULT_COMPLETED);
        return task;
    }


    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert
     * @return the JSON byte array
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.writeValueAsBytes(object);
    }
    @Test
    public void createTask() throws Exception {
        String accessToken = obtainAccessToken();
        // Create the Task
        mockMvc.perform(post("/api/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(createEntity())))
                .andExpect(status().isCreated());
    }

    @Test
    public void createTaskWithExistingId() throws Exception {
        String accessToken = obtainAccessToken();
        // Create the Task
        mockMvc.perform(post("/api/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(new Task(1,"test",true,"des"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllTasksTest() throws Exception {
        String accessToken = obtainAccessToken();
        MvcResult result = mockMvc.perform(get("/api/tasks")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getTaskById() throws Exception {
        String accessToken = obtainAccessToken();
        given(taskRepository.findOne(1l)).willReturn(new Task(1, "TEST", true, "test"));
        MvcResult result = mockMvc.perform(get("/api/tasks/1")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("TEST"))
                .andExpect(jsonPath("$.taskDescription").value("test"))
                .andExpect(jsonPath("$.completed").value(true))
                .andReturn();
    }

    @Test
    public void getTaskByNotFoundId() throws Exception {
        String accessToken = obtainAccessToken();
        MvcResult result = mockMvc.perform(get("/api/tasks/2")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    private String obtainAccessToken() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("scope", "read");
        String base64ClientCredentials = new String(Base64.encodeBase64("devglan-client:devglan-secret".getBytes()));
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .header("Authorization", "Basic " + base64ClientCredentials)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
