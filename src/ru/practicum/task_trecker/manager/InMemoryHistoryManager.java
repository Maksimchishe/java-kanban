package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> memoryHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        memoryHistory.add(task);
    }

    @Override
    public void removeFirst() {
        memoryHistory.removeFirst();
    }

    @Override
    public int size() {
        return memoryHistory.size();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return memoryHistory;
    }

}
