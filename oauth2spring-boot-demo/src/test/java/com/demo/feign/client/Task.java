package com.demo.feign.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long taskId;

    private String taskName;

    private boolean completed;

    private String taskDescription;
}
