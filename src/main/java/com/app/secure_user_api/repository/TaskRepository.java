package com.app.secure_user_api.repository;

import com.app.secure_user_api.entity.Task;
import com.app.secure_user_api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    Page<Task> findByUser(User user, Pageable pageable);

    Optional<Object> findByIdAndUser(Long id, User user);

    Page<Task> findByUserAndCompleted(User user, boolean completed, Pageable pageable);

    Page<Task> findByUserAndDueDateBeforeAndCompletedFalse(
            User user,
            LocalDate date,
            Pageable pageable
    );


}

