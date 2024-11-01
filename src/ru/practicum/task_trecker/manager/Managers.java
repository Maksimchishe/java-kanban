package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.exception.NotFoundException;

public class Managers {

    public static TaskManager getDefault() throws NotFoundException {
        return new FileBackedTaskManager();

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
