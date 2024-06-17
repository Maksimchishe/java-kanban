package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);

    ArrayList<Task> getHistory();

}
