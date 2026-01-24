package com.taskmanager.backend.repository;

import com.taskmanager.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {

    default List<Task> findAllOrdered() {
        return findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
    }
}