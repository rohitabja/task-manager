package com.cognizant.taskmanager.controller;

import com.cognizant.taskmanager.model.Task;
import com.cognizant.taskmanager.service.TaskManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin
public class TaskManagerController {

    private final TaskManagerService taskManagerService;

    @Autowired
    public TaskManagerController(final TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    @GetMapping("/task")
    public List<Task> getTasks() {
        log.info("Retrieving all tasks");
        return taskManagerService.getTasks();
    }

    @GetMapping("/task/{taskId}")
    public Task getTaskById(@PathVariable("taskId") final Integer taskId) {
        log.info("Retrieving task for taskId {}", taskId);
        return taskManagerService.getTaskById(taskId);
    }

    @PostMapping("/task")
    public Task saveTask(@RequestBody @Valid final Task task) {
        log.info("Saving task {}", task);
        return taskManagerService.saveTask(task);
    }
}
