package com.demo.mockMVCStandaloneMode;

import com.demo.controller.TaskController;
import com.demo.dao.TaskRepository;
import com.demo.model.Task;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {
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
    @InjectMocks
    TaskController taskController;

    @Mock
    TaskRepository taskRepository;

    private MockMvc mvc;

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

    @Before
    public void setup() {
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(taskController)
                .build();
    }


    @Test
    public void createTask() throws Exception {
        // Create the Task
        mvc.perform(post("/api/tasks")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(createEntity())))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllTasksTest() throws Exception {
        //given
        List<Task> list = new ArrayList();
        list.add(createEntity());
        given(this.taskRepository.findAll())
                .willReturn(list);
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void FindTaskById() throws Exception {
        //given
        given(this.taskRepository.findOne(1l))
                .willReturn(createEntity());
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/api/tasks/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void FindTaskByNotFoundId() throws Exception {
        // when
        MockHttpServletResponse response = mvc.perform(
                get("/api/tasks/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
