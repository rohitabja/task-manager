package com.cognizant.taskmanager.dao;

import com.cognizant.taskmanager.model.Task;

import java.util.List;

public interface TaskManagerDao {

    Task saveTask(final Task task);

    Task getTaskById(final Integer taskId);

    Task updateTask(final Task task);

    List<Task> getTasks();
}
