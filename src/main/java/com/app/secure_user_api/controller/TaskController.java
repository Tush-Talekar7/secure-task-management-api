package com.app.secure_user_api.controller;

import com.app.secure_user_api.dto.TaskRequestDTO;
import com.app.secure_user_api.entity.Task;
import com.app.secure_user_api.service.TaskService;
import com.app.secure_user_api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create new task")
    @PostMapping("/create-task")
    public ResponseEntity<Task> create(@Valid @RequestBody TaskRequestDTO task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping("/admin/all")
    public ResponseEntity<ApiResponse<Page<Task>>> getAllTasksAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Task> tasks = taskService.getAllTasks(page, size);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "All tasks fetched", tasks)
        );
    }


    @GetMapping("/get-overdue-task")
    public ResponseEntity<ApiResponse<Page<Task>>> getOverdueTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

       Page<Task> tasks =  taskService.getOverdueTasks(page, size);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Tasks fetched successfully",
                tasks
        ));
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<ApiResponse<Page<Task>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Boolean completed) {

        Page<Task> tasks = taskService.getMyTasks(page, size, completed);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Tasks fetched successfully",
                        tasks
                )
        );
    }

    @PutMapping("/get-task/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id,
                                       @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Deleted");
    }
}

