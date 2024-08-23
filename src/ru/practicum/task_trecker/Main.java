package ru.practicum.task_trecker;

import ru.practicum.task_trecker.manager.Managers;
import ru.practicum.task_trecker.manager.TaskManager;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        test1(Managers.getDefault());
    }

    private static void test1(TaskManager taskManager) {

        System.out.println("Тест1.1: Создание Task");
        taskManager.createTask(new Task("Задача", "Пояснение", Status.NEW));
        taskManager.createTask(new Task("Задача", "Пояснение", Status.NEW));
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест1.2: Создание Epic");
        taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест1.2: Создание SubTask");
        taskManager.createSubTask(new Subtask(2, "Задача", "Пояснение", Status.NEW));
        taskManager.createSubTask(new Subtask(2, "Задача", "Пояснение", Status.NEW));
        taskManager.createSubTask(new Subtask(2, "Задача", "Пояснение", Status.NEW));
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());

        List<Integer> listSubTaskEpic = taskManager.getEpicById(2).getListIdSubTask();
        System.out.println("list SubTaskEpicId: " + listSubTaskEpic);
        System.out.println("Удаление одного SubTask: " + taskManager.deleteSubTaskById(4));
        List<Integer> listSubTaskEpic1 = taskManager.getEpicById(2).getListIdSubTask();
        System.out.println("list SubTaskEpicId: " + listSubTaskEpic1);
        System.out.println();

        System.out.println("Тест1.3: Создание Epic");
        taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Получение Task по id: " + taskManager.getTaskById(1));
        System.out.println("Получение Epic по id: " + taskManager.getEpicById(2));
        System.out.println("Получение Epic по id: " + taskManager.getEpicById(6));
        System.out.println("Получение SubTask по id: " + taskManager.getSubTaskById(4));
        System.out.println("Получение Task по id: " + taskManager.getTaskById(0));
        System.out.println("Получение SubTask по id: " + taskManager.getSubTaskById(3));
        System.out.println("Получение Task по id: " + taskManager.getTaskById(1));
        System.out.println("Получение Task по id: " + taskManager.getTaskById(0));
        System.out.println("Получение Epic по id: " + taskManager.getEpicById(6));
        System.out.println("Получение SubTask по id: " + taskManager.getSubTaskById(4));
        System.out.println("Получение Task по id: " + taskManager.getTaskById(0));
        System.out.println("Получение SubTask по id: " + taskManager.getSubTaskById(3));

        System.out.println("List History: " + taskManager.getTaskHistory());
        System.out.println();

        System.out.println("Тест1.4: Удаление Task");
        System.out.println("Удаление Task по id: " + taskManager.deleteTaskById(1));
        System.out.println("List History: " + taskManager.getTaskHistory());
        System.out.println();

        System.out.println("Тест1.5: Удаление Epic и SubTask");
        System.out.println("Удаление Epic по id: " + taskManager.deleteEpicById(2));
        System.out.println("List History: " + taskManager.getTaskHistory());
        System.out.println();

        System.out.println("List Task: " + taskManager.getAllTasks());
        System.out.println("List Epic: " + taskManager.getAllEpics());
        System.out.println("List SubTask: " + taskManager.getAllSubTasks());
        System.out.println();


    }
}
