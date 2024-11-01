package ru.practicum.task_trecker.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.manager.TaskManager;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class TaskHandler implements HttpHandler {

    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String uri = exchange.getRequestURI().getPath();
        String[] splitUri = uri.split("/");
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

        try {
            switch (requestType(exchange)) {
                case Request.GET: {
                    switch (splitUri[1]) {
                        case "tasks": {
                            tasksGet(exchange, splitUri);
                            break;
                        }
                        case "epics": {
                            epicsGet(exchange, splitUri);
                            break;
                        }
                        case "subtasks": {
                            subtasksGet(exchange, splitUri);
                            break;
                        }
                        case "history": {
                            historyGet(exchange, splitUri);
                            break;
                        }
                        case "prioritized": {
                            prioritizedGet(exchange, splitUri);
                            break;
                        }
                        default:
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                    }
                    break;
                }
                case Request.POST: {
                    switch (splitUri[1]) {
                        case "tasks": {
                            tasksPost(exchange, splitUri, body);
                            break;
                        }
                        case "epics": {
                            epicsPost(exchange, splitUri, body);
                            break;
                        }
                        case "subtasks": {
                            subtasksPost(exchange, splitUri, body);
                            break;
                        }
                        default:
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                    }
                    break;
                }
                case Request.DELETE: {
                    switch (splitUri[1]) {
                        case "tasks": {
                            tasksDelete(exchange, splitUri);
                            break;
                        }
                        case "epics": {
                            epicsDelete(exchange, splitUri);
                            break;
                        }
                        case "subtasks": {
                            subtasksDelete(exchange, splitUri);
                            break;
                        }
                        default:
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                    }
                    break;
                }
                default:
                    sendText(exchange, "Такого эндпоинта не существует", 404);
            }
        } catch (NotFoundException e) {
            if (e.getMessage().equals("Not Acceptable")) {
                sendText(exchange, e.getMessage(), 406);
            } else {
                sendText(exchange, e.getMessage(), 404);
            }
        }
    }

    protected void tasksGet(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {

        if (splitUri.length == 2) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getAllTasks()), 200);
            return;
        }
        if (splitUri.length == 3) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getTaskById(Integer.parseInt(splitUri[2]))), 200);
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует", 404);
    }

    protected void epicsGet(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {

        if (splitUri.length == 2) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getAllEpics()), 200);
            return;
        }
        if (splitUri.length == 3) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getEpicById(Integer.parseInt(splitUri[2]))), 200);
            return;
        }
        if (splitUri.length == 4 && splitUri[3].equals("subtasks")) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getAllSubTaskById(Integer.parseInt(splitUri[2]))), 200);
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует", 404);
    }

    protected void subtasksGet(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {

        if (splitUri.length == 2) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getAllSubTasks()), 200);
            return;
        }
        if (splitUri.length == 3) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getSubTaskById(Integer.parseInt(splitUri[2]))), 200);
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует", 404);
    }

    protected void historyGet(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {

        if (splitUri.length == 2) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getTaskHistory()), 200);
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует", 404);
    }

    protected void prioritizedGet(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {

        if (splitUri.length == 2) {
            sendText(exchange, HttpTaskServer.getGson().toJson(taskManager.getPrioritizedTasks()), 200);
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует", 404);
    }

    protected void tasksPost(HttpExchange exchange, String[] splitUri, String body) throws NotFoundException, IOException {

        Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
        if (splitUri.length == 2) {
            if (task.getId() == null) {
                taskManager.createTask(task);
                sendText(exchange, "Добавление успешно.", 201);
            } else {
                taskManager.updateTask(task);
                sendText(exchange, "Обновление успешно.", 201);
            }
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует.", 404);
    }

    protected void epicsPost(HttpExchange exchange, String[] splitUri, String body) throws NotFoundException, IOException {

        Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
        if (splitUri.length == 2) {
            if (epic.getId() == null) {
                taskManager.createEpic(epic);
                sendText(exchange, "Добавление успешно.", 201);
            } else {
                taskManager.updateEpic(epic);
                sendText(exchange, "Обновление успешно.", 201);
            }
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует.", 404);
    }

    protected void subtasksPost(HttpExchange exchange, String[] splitUri, String body) throws NotFoundException, IOException {

        Subtask subtask = HttpTaskServer.getGson().fromJson(body, Subtask.class);
        if (splitUri.length == 2) {
            if (subtask.getId() == null) {
                taskManager.createSubTask(subtask);
                sendText(exchange, "Добавление успешно.", 201);
            } else {
                taskManager.updateSubTask(subtask);
                sendText(exchange, "Обновление успешно.", 201);
            }
            return;
        }
        sendText(exchange, "Такого эндпоинта не существует.", 404);
    }

    protected void tasksDelete(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {
        if (splitUri.length == 3) {
            Integer id = Integer.parseInt(splitUri[2]);
            taskManager.deleteTaskById(id);
            sendText(exchange, "Удаление успешно.", 200);
        }
    }

    protected void epicsDelete(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {
        if (splitUri.length == 3) {
            Integer id = Integer.parseInt(splitUri[2]);
            taskManager.deleteEpicById(id);
            sendText(exchange, "Удаление успешно.", 200);
        }
    }

    protected void subtasksDelete(HttpExchange exchange, String[] splitUri) throws NotFoundException, IOException {
        if (splitUri.length == 3) {
            Integer id = Integer.parseInt(splitUri[2]);
            taskManager.deleteSubTaskById(id);
            sendText(exchange, "Удаление успешно.", 200);
        }
    }

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(rCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected Request requestType(HttpExchange exchange) {

        String req = exchange.getRequestMethod();

        return switch (req) {
            case "GET" -> Request.GET;
            case "POST" -> Request.POST;
            case "DELETE" -> Request.DELETE;
            default -> Request.NONE;
        };
    }

    enum Request { GET, POST, DELETE, NONE }


}

