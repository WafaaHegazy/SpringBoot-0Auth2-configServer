package com.demo.Mokito;

import com.demo.controller.UserController;
import com.demo.dao.UserDao;
import com.demo.model.User;
import com.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private UserController userController;

    private MockMvc restUserMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    public void getVehicleWhenRequestingTextShouldReturnMakeAndModel() throws Exception {
        List<User> list = new ArrayList();
        list.add(new User("test",1,1));
        given(this.userService.findAll())
                .willReturn(list);
        this.restUserMockMvc.perform(get("/users/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].username").value("test"))
                .andExpect(jsonPath("$[*].salary").value(1))
                .andExpect(jsonPath("$[*].age").value(1));
        //assertj
    }
}
