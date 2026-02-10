package com.taskmanager.backend.repository;

import com.taskmanager.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserEmailOrderByCreatedAtAsc(String email);
    List<Task> findByUserEmail(String email, Sort sort);

    default List<Task> findAllByUserOrdered(String email) {
        return findByUserEmail(email, Sort.by(Sort.Direction.ASC, "createdAt"));
    }
}
