package ru.practicum.task_trecker.manager;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.practicum.task_trecker.HttpTaskServer;
import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpServerTest extends NewTaskManagerTest {

    @Nested
    class HttpTaskManagerTasksTest {

        // создаём экземпляр InMemoryTaskManager
        TaskManager manager = new InMemoryTaskManager();
        // передаём его в качестве аргумента в конструктор HttpTaskServer
        HttpTaskServer taskServer = new HttpTaskServer(manager);
        Gson gson = HttpTaskServer.getGson();

        public HttpTaskManagerTasksTest() throws IOException {
        }

        @BeforeEach
        public void setUp() {
            manager.delAllTask();
            manager.delAllEpic();
            manager.delAllSubTask();
            taskServer.start();
        }

        @AfterEach
        public void shutDown() {
            taskServer.stop();
        }

        @Test
        public void testAddTask() throws IOException, InterruptedException {
            // создаём задачу
            Task task = new Task("Test", "Testing task 2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
            // конвертируем её в JSON
            String taskJson = gson.toJson(task);
            System.out.println(taskJson);
            // создаём HTTP-клиент и запрос
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            System.out.println(request.toString());
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // проверяем код ответа
            assertEquals(201, response.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Task> tasksFromManager = manager.getAllTasks();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }

        @Test
        public void testAddEpic() throws IOException, InterruptedException {
            // создаём задачу
            Epic epic = new Epic("Test", "Testing task", Status.NEW);
            // конвертируем её в JSON
            String taskJson = gson.toJson(epic);
            System.out.println(taskJson);
            // создаём HTTP-клиент и запрос
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            System.out.println(request.toString());
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // проверяем код ответа
            assertEquals(201, response.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Epic> tasksFromManager = manager.getAllEpics();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }

        @Test
        public void testAddSubTask() throws IOException, InterruptedException, NotFoundException {
            // создаём задачу
            Epic epic = manager.createEpic(new Epic("Test", "Testing task 2", Status.NEW));
            Subtask subtask = new Subtask(epic.getId(), "Test", "Testing task 2", Status.NEW);
            // конвертируем её в JSON
            String taskJson = gson.toJson(subtask);
            System.out.println(taskJson);
            // создаём HTTP-клиент и запрос
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            System.out.println(request.toString());
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // проверяем код ответа
            assertEquals(201, response.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Subtask> tasksFromManager = manager.getAllSubTasks();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }

        @Test
        public void testUpdateTask() throws IOException, InterruptedException {
            // создаём задачу
            Task taskNew = new Task("TestNew", "Testing task New", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));

            Task task = new Task(taskNew.getId(), "Test", "Testing task 2", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
            // конвертируем её в JSON
            String taskJson = gson.toJson(task);
            System.out.println(taskJson);
            // создаём HTTP-клиент и запрос
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            System.out.println(request.toString());
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // проверяем код ответа
            assertEquals(201, response.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Task> tasksFromManager = manager.getAllTasks();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }

        @Test
        public void testUpdateEpic() throws IOException, InterruptedException {
            // создаём задачу
            Epic epicNew = new Epic("TestNew", "Testing task New", Status.NEW);

            Epic epic = new Epic(epicNew.getId(), "Test", "Testing task 2", Status.NEW);
            // конвертируем её в JSON
            String taskJson = gson.toJson(epic);
            System.out.println(taskJson);
            // создаём HTTP-клиент и запрос
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            System.out.println(request.toString());
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // проверяем код ответа
            assertEquals(201, response.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Epic> tasksFromManager = manager.getAllEpics();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }

        @Test
        public void testUpdateSubTask() throws IOException, InterruptedException, NotFoundException {
            // создаём задачу
            Epic epic = manager.createEpic(new Epic("Test", "Testing task 2", Status.NEW));
            Subtask newSubtask = manager.createSubTask(new Subtask(epic.getId(), "Test", "Testing task 2", Status.NEW));

            Subtask subtask = new Subtask(newSubtask.getId(), epic.getId(), "Test", "Testing task 2", Status.NEW);
            // конвертируем её в JSON
            String taskJson = gson.toJson(subtask);
            System.out.println(taskJson);
            // создаём HTTP-клиент и запрос
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                    .build();
            System.out.println(request.toString());
            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // проверяем код ответа
            assertEquals(201, response.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Subtask> tasksFromManager = manager.getAllSubTasks();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
            assertEquals("Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
        }
    }
}
