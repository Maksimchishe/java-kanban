package ru.practicum.task_trecker.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.task_trecker.manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    final int port = 8080;
    HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", new TaskHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.printf("HTTP-сервер запущен на %d порту!%n", port);
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


