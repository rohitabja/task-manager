package com.cognizant.taskmanager.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by hp on 19-06-2019.
 */
@Builder
@Getter
public class ParentTask {

    private Integer parentTaskId;
    private String parentTaskName;

}
