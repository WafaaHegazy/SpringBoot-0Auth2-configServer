package com.demo.mockMVCStandaloneMode;
import com.demo.controller.UserController;
import com.demo.model.User;
import com.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {
    @InjectMocks
    UserController business;

    @Mock
    UserService service;


    @Test
    public void getAllusersTest() {
        ArrayList<User> list = new ArrayList<User>();
        list.add(new User());
        when(service.findAll()).thenReturn(list);
        assertEquals(1, business.listUser().size());
    }

    @Test
    public void getAllemptylist() {
        List <User> list = new ArrayList<>();
        when(service.findAll()).thenReturn(list);
        assertEquals(true, business.listUser().isEmpty());
    }


}
