package ru.practicum.task_trecker;

import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.manager.Managers;
import ru.practicum.task_trecker.manager.TaskManager;

import java.io.IOException;

public class Main {

    public static void main() throws IOException, NotFoundException {

        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);

        server.start();
    }
}
