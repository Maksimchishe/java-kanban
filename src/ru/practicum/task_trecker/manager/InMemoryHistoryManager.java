package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> memoryHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (memoryHistory.size() == 10) {
            memoryHistory.removeFirst();
        }
        memoryHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return memoryHistory;
    }
}
