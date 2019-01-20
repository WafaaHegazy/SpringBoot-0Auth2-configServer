package com.demo.feign.client;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@FeignClient()
public interface TaskResource {
    @RequestLine("GET /tasks")
    List<Task> findAll();

    @RequestLine("GET /tasks/{id}")
    Task findOne(@Param("id") Long id);

}
