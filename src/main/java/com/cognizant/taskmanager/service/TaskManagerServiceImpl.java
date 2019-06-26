package com.cognizant.taskmanager.service;

import com.cognizant.taskmanager.dao.TaskManagerDao;
import com.cognizant.taskmanager.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskManagerServiceImpl implements TaskManagerService {

    private final TaskManagerDao taskManagerDao;

    @Autowired
    public TaskManagerServiceImpl(final TaskManagerDao taskManagerDao) {
        this.taskManagerDao = taskManagerDao;
    }

    @Override
    public List<Task> getTasks() {
        log.info("Retrieving all tasks");
        final List<Task> tasks = taskManagerDao.getTasks();

        log.info("Retrieved all tasks {}", tasks);
        return tasks;
    }

    @Override
    public Task getTaskById(final Integer taskId) {
        final Task task = taskManagerDao.getTaskById(taskId);
        if(task == null) {
            log.error("No task found for taskId {}", taskId);
        }
        return task;
    }

    @Override
    public Task saveTask(final Task task) {

        if(task.getTaskId() != null) {
            log.info("Task id is not null, updating task info {}", task);
            return taskManagerDao.updateTask(task);
        }

        log.info("Task id is empty, saving new task info {}", task);
        return taskManagerDao.saveTask(task);
    }

}
