package br.com.dio.persistence;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private List<Task> tasks = new ArrayList<>();
    private Long idCounter = 1L;

    public Task save(String title, String description) {
        Task task = new Task(idCounter++, title, description, "PENDENTE");
        tasks.add(task);
        return task;
    }

    public List<Task> findAll() {
        return tasks;
    }

    public Task findById(Long id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void delete(Long id) {
        tasks.removeIf(t -> t.getId().equals(id));
    }
}