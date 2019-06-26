package com.cognizant.taskmanager.controller;

import com.cognizant.taskmanager.model.Task;
import com.cognizant.taskmanager.service.TaskManagerService;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hp on 26-06-2019.
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskManagerControllerTest {

    @Mock
    private TaskManagerService taskManagerService;

    @InjectMocks
    private TaskManagerController taskManagerController;

    @Test
    public void testRetrieveTasks() {
        when(taskManagerService.getTasks()).thenReturn(ImmutableList.of(
                Task.builder().build()
        ));
        assertTrue(taskManagerController.getTasks().size() == 1);
    }

    @Test
    public void testRetrieveTaskById() {
        when(taskManagerService.getTaskById(1)).thenReturn(Task.builder().build());
        assertNotNull(taskManagerController.getTaskById(1));
    }

    @Test
    public void testSaveTask() {
        Task task = Task.builder().build();
        taskManagerController.saveTask(task);
        verify(taskManagerService).saveTask(task);
    }

}