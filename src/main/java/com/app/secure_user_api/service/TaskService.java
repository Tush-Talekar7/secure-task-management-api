package com.app.secure_user_api.service;

import com.app.secure_user_api.dto.TaskRequestDTO;
import com.app.secure_user_api.entity.Task;
import com.app.secure_user_api.entity.User;
import com.app.secure_user_api.repository.TaskRepository;
import com.app.secure_user_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getLoggedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Task createTask(TaskRequestDTO taskRequestDTO) {
        User user = getLoggedUser();
        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setPriority(taskRequestDTO.getPriority());
        task.setDueDate(taskRequestDTO.getDueDate());
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Page<Task> getMyTasks(int page, int size, Boolean completed) {
        User user = getLoggedUser();
        Pageable pageable = PageRequest.of(page, size);

        if (completed != null) {
            return taskRepository.findByUserAndCompleted(user, completed, pageable);
        }

        return taskRepository.findByUser(user, pageable);
    }



    public Task updateTask(Long id, Task updatedTask) {
        User user = getLoggedUser();

        Task task = (Task) taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(task);
    }

    public Page<Task> getOverdueTasks(int page, int size) {
        User user = getLoggedUser();
        Pageable pageable = PageRequest.of(page, size);

        return taskRepository
                .findByUserAndDueDateBeforeAndCompletedFalse(
                        user,
                        LocalDate.now(),
                        pageable
                );
    }


    public void deleteTask(Long id) {
        User user = getLoggedUser();

        Task task = (Task) taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(task);
    }

    public Page<Task> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable);
    }
}

