package br.com.dio.ui;

import br.com.dio.persistence.Task;
import br.com.dio.service.TaskService;

import java.util.Scanner;

public class TaskUI {

    private TaskService service = new TaskService();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        int option;

        do {
            System.out.println("\n1 - Criar tarefa");
            System.out.println("2 - Listar tarefas");
            System.out.println("3 - Atualizar status");
            System.out.println("4 - Deletar tarefa");
            System.out.println("0 - Sair");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> create();
                case 2 -> list();
                case 3 -> update();
                case 4 -> delete();
            }

        } while (option != 0);
    }

    private void create() {
        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Descrição: ");
        String desc = scanner.nextLine();

        service.createTask(title, desc);
    }

    private void list() {
        for (Task t : service.listTasks()) {
            System.out.println(t.getId() + " - " + t.getTitle() + " [" + t.getStatus() + "]");
        }
    }

    private void update() {
        System.out.print("ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Novo status: ");
        String status = scanner.nextLine();

        service.updateStatus(id, status);
    }

    private void delete() {
        System.out.print("ID: ");
        Long id = scanner.nextLong();
        service.deleteTask(id);
    }
}