package com.demo.controller;

import com.demo.model.Task;
import com.demo.repository.TaskRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController

@Api(value="/todo",description="task",produces ="application/json")
@RequestMapping("/todo")
public class TaskController {
    private TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @ApiOperation(value="get all tasks",response=Task.class)
    @ApiResponses(value={
            @ApiResponse(code=200,message="ToDo List Retrieved",response=Task.class),
            @ApiResponse(code=500,message="Internal Server Error"),
            @ApiResponse(code=404,message="Task not found")
    })

    @RequestMapping(value="/{tasks}", method= RequestMethod.GET)
    public List<Task> readersBooks() {
        List<Task> taskList = new ArrayList<>();
        taskRepository.findAll().forEach(taskList::add);
        return taskList;
    }
}
