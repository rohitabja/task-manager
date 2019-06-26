package com.cognizant.taskmanager.service;

import com.cognizant.taskmanager.dao.TaskManagerDao;
import com.cognizant.taskmanager.model.Task;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hp on 26-06-2019.
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskManagerServiceImplTest {

    @Mock
    private TaskManagerDao taskManagerDao;

    @InjectMocks
    private TaskManagerServiceImpl taskManagerService;

    @Test
    public void testGetTaskById() {
        Task task = Task.builder().build();
        when(taskManagerDao.getTaskById(1)).thenReturn(task);
        assertEquals(task, taskManagerService.getTaskById(1));
    }

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = ImmutableList.of(Task.builder().build());
        when(taskManagerDao.getTasks()).thenReturn(tasks);
        assertEquals(tasks, taskManagerService.getTasks());
    }

    @Test
    public void testSaveTask() {
        Task task = Task.builder().build();
        taskManagerService.saveTask(task);
        verify(taskManagerDao).saveTask(task);
    }

    @Test
    public void testUpdateTask() {
        Task task = Task.builder().taskId(1).build();
        taskManagerService.saveTask(task);
        verify(taskManagerDao).updateTask(task);
    }

}