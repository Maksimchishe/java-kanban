package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

}
