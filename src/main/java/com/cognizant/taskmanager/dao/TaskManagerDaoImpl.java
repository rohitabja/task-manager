package com.cognizant.taskmanager.dao;

import com.cognizant.taskmanager.model.ParentTask;
import com.cognizant.taskmanager.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.cognizant.taskmanager.util.TaskManagerUtil.*;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Repository
@Slf4j
public class TaskManagerDaoImpl implements TaskManagerDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskManagerDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Task saveTask(final Task request) {
        final Optional<Integer> parentTaskId = getParentTaskId(request);
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        log.info("Saving new task {}", request);
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "insert into task (nm_task, id_parent_task, priority, dt_start, dt_end, is_completed, dt_updated) " +
                                    "values (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, request.getTaskName().trim());
                    ps.setObject(2, parentTaskId.orElse(null), Types.INTEGER);
                    ps.setInt(3, request.getPriority());
                    ps.setDate(4, Date.valueOf(request.getStartDate()));
                    ps.setDate(5, Date.valueOf(request.getEndDate()));
                    ps.setString(6, "N");
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                    return ps;
                }
                , keyHolder);

        final Task createdTask = Task.builder()
                .taskId(keyHolder.getKey().intValue())
                .taskName(request.getTaskName())
                .parentTaskName(request.getParentTaskName())
                .priority(request.getPriority())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .completed(request.isCompleted())
                .build();

        log.info("New task created in db {}", createdTask);

        return createdTask;
    }

    @Override
    public Task getTaskById(final Integer taskId) {
        return jdbcTemplate.queryForObject("select * from task t " +
                        " left outer join parent_task pt " +
                        " on pt.id_parent_task = t.id_parent_task " +
                        " where id_task = ?",
                (resultSet, i) ->
                        Task.builder()
                                .taskId(resultSet.getInt("id_task"))
                                .taskName(resultSet.getString("nm_task"))
                                .parentTaskName(resultSet.getString("nm_parent_task"))
                                .priority(resultSet.getInt("priority"))
                                .startDate(getDate(resultSet.getDate("dt_start")))
                                .endDate(getDate(resultSet.getDate("dt_end")))
                                .completed(getBoolean(resultSet.getString("is_completed")))
                                .build(), taskId);
    }

    private Optional<Integer> getParentTaskId(final Task task) {
        log.info("Retrieving parent task id for {}", task);
        if (isBlank(task.getParentTaskName())) {
            log.info("Parent task is empty");
            return empty();
        }

        final List<ParentTask> parentTask = jdbcTemplate.query(
                "select * from parent_task where nm_parent_task = ?"
                , new Object[]{task.getParentTaskName().trim()}
                , (rs, i) ->
                        ParentTask.builder()
                                .parentTaskId(rs.getInt("id_parent_task"))
                                .parentTaskName(rs.getString("nm_parent_task"))
                                .build());

        if (isEmpty(parentTask)) {
            log.info("Parent task not found in database, creating new parent task {}", task);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement("insert into parent_task (nm_parent_task) values (?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, task.getParentTaskName().trim());
                return ps;
            }, keyHolder);

            log.info("Parent task is created for {}, parent task id {}", task, keyHolder.getKey().intValue());
            return ofNullable(keyHolder.getKey().intValue());
        }

        log.info("Parent task found in database {}", parentTask);
        return ofNullable(parentTask.get(0).getParentTaskId());
    }

    @Override
    @Transactional
    public Task updateTask(final Task request) {
        final Optional<Integer> parentTaskId = getParentTaskId(request);
        log.info("Updating new task {}", request);

        jdbcTemplate.update("update task set " +
                        " nm_task=?, id_parent_task=?, priority=?, dt_start=?, dt_end=?, is_completed=?, dt_updated=? " +
                        " where id_task=?",
                request.getTaskName(), parentTaskId.orElse(null), request.getPriority(),
                Date.valueOf(request.getStartDate()), Date.valueOf(request.getEndDate()),
                getStrFromBoolean(request.isCompleted()), Timestamp.valueOf(LocalDateTime.now()), request.getTaskId());

        return request;
    }

    @Override
    public List<Task> getTasks() {
        return jdbcTemplate.query(
                "select id_task, nm_task, pt.nm_parent_task, priority, dt_start, dt_end, is_completed from task t" +
                        " left join parent_task pt " +
                        " on pt.id_parent_task = t.id_parent_task " +
                        " order by t.dt_updated desc",
                (resultSet, i) -> Task.builder()
                        .taskId(resultSet.getInt("id_task"))
                        .taskName(resultSet.getString("nm_task"))
                        .parentTaskName(resultSet.getString("nm_parent_task"))
                        .priority(resultSet.getInt("priority"))
                        .startDate(getDate(resultSet.getDate("dt_start")))
                        .endDate(getDate(resultSet.getDate("dt_end")))
                        .completed(getBoolean(resultSet.getString("is_completed")))
                        .build());
    }

}
