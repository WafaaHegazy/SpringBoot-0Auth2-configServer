package com.demo.test;

import com.demo.Application;
import com.demo.controller.UserController;
import com.demo.dao.UserDao;
import com.demo.model.User;
import com.demo.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UsersCotrollerTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    private User user;
    @Autowired
    private EntityManager em;

    private MockMvc restUserMockMvc;

    public static User createEntity(EntityManager em) {
        return new User("test", 1, 1);

    }

    @Before
    public void initTest() {
        user = createEntity(em);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserController userController = new UserController(userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    @Transactional
    public void getAllUsersTest() throws Exception {
        userDao.save(user);
        restUserMockMvc.perform(get("/users/user"))
                .andExpect(status().isOk());
    }
}
