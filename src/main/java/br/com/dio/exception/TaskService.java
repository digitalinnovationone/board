package br.com.dio.service;

import br.com.dio.persistence.Task;
import br.com.dio.persistence.TaskRepository;

import java.util.List;

public class TaskService {

    private TaskRepository repository = new TaskRepository();

    public Task createTask(String title, String description) {
        return repository.save(title, description);
    }

    public List<Task> listTasks() {
        return repository.findAll();
    }

    public void updateStatus(Long id, String status) {
        Task task = repository.findById(id);
        if (task != null) {
            task.setStatus(status);
        }
    }

    public void deleteTask(Long id) {
        repository.delete(id);
    }
}