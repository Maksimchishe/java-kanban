package ru.practicum.task_trecker;

import ru.practicum.task_trecker.manager.Managers;
import ru.practicum.task_trecker.manager.TaskManager;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        taskManager.loadFromFile();

        taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));
        taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        taskManager.createSubTask(new Subtask(1,"Наименование", "Пояснение", Status.NEW));

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());
    }

}
