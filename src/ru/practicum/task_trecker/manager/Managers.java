package ru.practicum.task_trecker.manager;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager();
        //return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
