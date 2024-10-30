package ru.practicum.task_trecker;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.manager.TaskManager;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.*;


public class HttpTaskServer {
    final int PORT = 8080;
    HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/", new TaskHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.printf("HTTP-сервер запущен на %d порту!%n", PORT);
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

}


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
                            Integer Id = Integer.parseInt(splitUri[2]);
                            taskManager.deleteTaskById(Id);
                            sendText(exchange, "Удаление успешно.", 200);
                            break;
                        }
                        case "epics": {
                            Integer Id = Integer.parseInt(splitUri[2]);
                            taskManager.deleteEpicById(Id);
                            sendText(exchange, "Удаление успешно.", 200);
                            break;
                        }
                        case "subtasks": {
                            Integer Id = Integer.parseInt(splitUri[2]);
                            taskManager.deleteSubTaskById(Id);
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

class LocalTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localTime) throws IOException {
        if (localTime == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(localTime.format(ISO_LOCAL_DATE_TIME));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return LocalDateTime.parse(jsonReader.nextString(), ISO_LOCAL_DATE_TIME);
    }
}

class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(duration.toMinutes());
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        return Duration.ofMinutes(Integer.parseInt(jsonReader.nextString()));
    }
}
