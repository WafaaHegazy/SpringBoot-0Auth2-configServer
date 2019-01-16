package com.demo.mockMVCStandaloneMode;

import com.demo.controller.TaskController;
import com.demo.dao.TaskRepository;
import com.demo.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    @InjectMocks
    TaskController taskController;

    @Mock
    TaskRepository taskRepository;

    private JacksonTester<Task> jsonTask;

    private MockMvc mvc;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(taskController)
                .build();
    }
    @Test
    public void getAllTasksTest() throws Exception {

        //given
        List<Task> list = new ArrayList();
        list.add(new Task(1,"test",true,"test"));
        given(this.taskRepository.findAll())
                .willReturn(list);

        // when
        MockHttpServletResponse response = mvc.perform(
                get("/todo/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo(
//                jsonTask.write(new Task(1,"test",true,"test")).getJson());
    }

}
