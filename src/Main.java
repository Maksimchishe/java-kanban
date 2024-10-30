import ru.practicum.task_trecker.HttpTaskServer;
import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.manager.Managers;
import ru.practicum.task_trecker.manager.TaskManager;


import java.io.IOException;


public static void main() throws NotFoundException, IOException {

    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);

    server.start();
}
