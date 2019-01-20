package com.demo.controller;

import com.demo.dao.TaskRepository;
import com.demo.model.Task;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController

@Api(value = "/api", description = "task", produces = "application/json")
@RequestMapping("/api")
public class TaskController {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);

    private TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * POST  /tasks : Create a new task.
     *
     * @param task the task to create
     * @return the ResponseEntity with status 201 (Created) and with body the new task, or with status 400 (Bad Request) if the task has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        if (task.getTaskId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task result = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    /**
     * PUT  /tasks : Updates an existing task.
     *
     * @param task the task to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated task,
     * or with status 400 (Bad Request) if the task is not valid,
     * or with status 500 (Internal Server Error) if the task couldn't be updated
     */
    @PutMapping("/tasks")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        log.debug("REST request to update Task : {}", task);
        if (task.getTaskId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task result = taskRepository.save(task);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * GET  /tasks : get all the tasks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @ApiOperation(value = "get all tasks", response = Task.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ToDo List Retrieved", response = Task.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        taskRepository.findAll().forEach(taskList::add);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }


    /**
     * GET  /tasks/:id : get the "id" task.
     *
     * @param id the id of the task to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the task, or with status 404 (Not Found)
     */
    @ApiOperation(value = "get task by id", response = Task.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ToDo List Retrieved", response = Task.class),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 404, message = "Task not found")
    })

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskRepository.findOne(id);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE  /tasks/:id : delete the "id" task.
     *
     * @param id the id of the task to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);

        taskRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
