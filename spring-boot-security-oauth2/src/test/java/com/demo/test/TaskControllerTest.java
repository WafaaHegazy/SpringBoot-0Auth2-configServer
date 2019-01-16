package com.demo.test;

import com.demo.Application;
import com.demo.controller.TaskController;
import com.demo.dao.TaskRepository;
import com.demo.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TaskControllerTest {
    private static final String description = "AAAAAAAAAA";
    private static final String name = "AAAAAAAAAA";
    private static final boolean completed = true;

    @Autowired
    private TaskRepository taskRepository;

    private MockMvc restTaskMockMvc;

    private Task task;

    @Autowired
    private EntityManager em;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskController taskController = new TaskController(taskRepository);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .build();
    }

    public static Task createEntity (EntityManager em){
        Task task = new Task(1,name,completed,description);
        return task;
    }
    @Before
    public void initTest() {
        task = createEntity(em);
    }


    @Test
    @Transactional
    public void getAllTasksTest() throws Exception {
        taskRepository.save(task);
        restTaskMockMvc.perform(get("/todo/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].taskName").value(task.getTaskName()))
                .andExpect(jsonPath("$[*].taskDescription").value(task.getTaskDescription()))
                .andExpect(jsonPath("$[*].completed").value(task.isCompleted()));
    }
}
