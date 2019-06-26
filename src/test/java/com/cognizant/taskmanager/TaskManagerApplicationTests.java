package com.cognizant.taskmanager;

import com.cognizant.taskmanager.controller.TaskManagerController;
import com.cognizant.taskmanager.dao.TaskManagerDao;
import com.cognizant.taskmanager.service.TaskManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
		, classes = TaskManagerApplication.class)
public class TaskManagerApplicationTests {

	@Autowired
	private TaskManagerController taskManagerController;

	@Autowired
	private TaskManagerService taskManagerService;

	@Autowired
	private TaskManagerDao taskManagerDao;

	@Test
	public void contextLoads() {
		assertThat(taskManagerController).isNotNull();
		assertThat(taskManagerService).isNotNull();
		assertThat(taskManagerDao).isNotNull();
	}

}
