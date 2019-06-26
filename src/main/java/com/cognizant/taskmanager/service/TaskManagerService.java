package com.cognizant.taskmanager.service;

import com.cognizant.taskmanager.model.Task;

import java.util.List;

public interface TaskManagerService {

    List<Task> getTasks();

    Task getTaskById(final Integer taskId);

    Task saveTask(final Task task);
}
