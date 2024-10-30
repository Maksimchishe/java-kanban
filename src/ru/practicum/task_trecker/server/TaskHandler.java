package ru.practicum.task_trecker.server;

import com.google.gson.Gson;
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

        Gson gson = HttpTaskServer.getGson();

        String uri = exchange.getRequestURI().getPath();
        String[] splitUri = uri.split("/");

        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);

        try {
            switch (exchange.getRequestMethod()) {
                case "GET": {
                    switch (splitUri[1]) {
                        case "tasks": {
                            if (splitUri.length == 2) {
                                sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200);
                                break;
                            }
                            if (splitUri.length == 3) {
                                sendText(exchange, gson.toJson(taskManager.getTaskById(Integer.parseInt(splitUri[2]))), 200);
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                            break;
                        }
                        case "epics": {
                            if (splitUri.length == 2) {
                                sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
                                break;
                            }
                            if (splitUri.length == 3) {
                                sendText(exchange, gson.toJson(taskManager.getEpicById(Integer.parseInt(splitUri[2]))), 200);
                                break;
                            }
                            if (splitUri.length == 4 && splitUri[3].equals("subtasks")) {
                                sendText(exchange, gson.toJson(taskManager.getAllSubTaskById(Integer.parseInt(splitUri[2]))), 200);
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                            break;
                        }
                        case "subtasks": {
                            if (splitUri.length == 2) {
                                sendText(exchange, gson.toJson(taskManager.getAllSubTasks()), 200);
                                break;
                            }
                            if (splitUri.length == 3) {
                                sendText(exchange, gson.toJson(taskManager.getSubTaskById(Integer.parseInt(splitUri[2]))), 200);
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                            break;
                        }
                        case "history": {
                            if (splitUri.length == 2) {
                                sendText(exchange, gson.toJson(taskManager.getTaskHistory()), 200);
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                            break;
                        }
                        case "prioritized": {
                            if (splitUri.length == 2) {
                                sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                            break;
                        }
                        default:
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                    }
                    break;
                }
                case "POST": {
                    switch (splitUri[1]) {
                        case "tasks": {
                            Task task = gson.fromJson(body, Task.class);
                            if (splitUri.length == 2) {
                                if (task.getId() == null) {
                                    taskManager.createTask(task);
                                    sendText(exchange, "Добавление успешно.", 201);
                                } else {
                                    taskManager.updateTask(task);
                                    sendText(exchange, "Обновление успешно.", 201);
                                }
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует.", 404);
                            break;
                        }
                        case "epics": {
                            Epic epic = gson.fromJson(body, Epic.class);
                            if (splitUri.length == 2) {
                                if (epic.getId() == null) {
                                    taskManager.createEpic(epic);
                                    sendText(exchange, "Добавление успешно.", 201);
                                } else {
                                    taskManager.updateEpic(epic);
                                    sendText(exchange, "Обновление успешно.", 201);
                                }
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует.", 404);
                            break;
                        }
                        case "subtasks": {
                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            if (splitUri.length == 2) {
                                if (subtask.getId() == null) {
                                    taskManager.createSubTask(subtask);
                                    sendText(exchange, "Добавление успешно.", 201);
                                } else {
                                    taskManager.updateSubTask(subtask);
                                    sendText(exchange, "Обновление успешно.", 201);
                                }
                                break;
                            }
                            sendText(exchange, "Такого эндпоинта не существует.", 404);
                            break;
                        }
                        default:
                            sendText(exchange, "Такого эндпоинта не существует", 404);
                    }
                    break;
                }
                case "DELETE": {
                    switch (splitUri[1]) {
                        case "tasks": {
                            Integer id = Integer.parseInt(splitUri[2]);
                            taskManager.deleteTaskById(id);
                            sendText(exchange, "Удаление успешно.", 200);
                            break;
                        }
                        case "epics": {
                            Integer id = Integer.parseInt(splitUri[2]);
                            taskManager.deleteEpicById(id);
                            sendText(exchange, "Удаление успешно.", 200);
                            break;
                        }
                        case "subtasks": {
                            Integer id = Integer.parseInt(splitUri[2]);
                            taskManager.deleteSubTaskById(id);
                            sendText(exchange, "Удаление успешно.", 200);
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

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(rCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
