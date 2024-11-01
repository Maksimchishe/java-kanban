package ru.practicum.task_trecker.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ALL")
public class NewTaskManagerTest extends TaskManagerTest {

    @Test
    void TestStatusEpicToAllSubtasksWithTheNewStatus() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
        }

        assertEquals(Status.NEW, newEpic.getStatus(), "Неверный Status Epic.");
    }

    @Test
    void TestStatusEpicToAllSubtasksWithTheDoneStatus() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.DONE));
        }

        assertEquals(Status.DONE, newEpic.getStatus(), "Неверный Status Epic.");
    }

    @Test
    void TestStatusEpicToAllSubtasksWithTheNewAndDoneStatus() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            if (i % 2 == 0) {
                taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            } else {
                taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.DONE));
            }
        }

        assertEquals(Status.IN_PROGRESS, newEpic.getStatus(), "Неверный Status Epic.");
    }

    @Test
    void TestStatusEpicToAllSubtasksWithTheInProgressStatus() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.IN_PROGRESS));
        }

        assertEquals(Status.IN_PROGRESS, newEpic.getStatus(), "Неверный Status Epic.");
    }

    @Test
    void TestAvailabilityOfEpicForSubTask() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.IN_PROGRESS));
        }

        assertNotNull(taskManager.getEpicById(newEpic.getId()), "Epic не найден.");
    }

    @Test
    void TestThePresenceOfTheIntersectionOfTimeSegments() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        taskManager.delAllTask();

        Task newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 0, 0), Duration.ofMinutes(10)));
        assertNotNull(newTask1, "Наличие пересечения временных отрезков.");

        assertThrows(NotFoundException.class, () -> {
            Task newTask2 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 0, 55, 0), Duration.ofMinutes(10)));
        });

        Task newTask3 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 30, 0), Duration.ofMinutes(10)));

        assertThrows(NotFoundException.class, () -> {
            Task newTask4 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 35, 0), Duration.ofMinutes(10)));
        });
    }

    @Test
    void TestEmptyTaskHistory() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        assertTrue(taskManager.getTaskHistory().isEmpty(), "История задач не пуста.");

    }

    @Test
    void TestDuplicationInTheTaskHistory() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        taskManager.delAllTask();

        Task newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 0, 0), Duration.ofMinutes(10)));
        taskManager.getTaskById(newTask1.getId());
        taskManager.getTaskById(newTask1.getId());
        taskManager.getTaskById(newTask1.getId());

        long count = taskManager.getTaskHistory().stream()
                .filter(t -> t.getId().equals(newTask1.getId()))
                .count();

        assertFalse(count > 1, "Дублирование в истории задач.");

    }

    @Test
    void TestDeletingTheFirstItemInTheTaskHistory() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        taskManager.delAllTask();

        Task newTask1 = null;
        for (int i = 1; i <= 5; i++) {
            newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, i, 0, 0), Duration.ofMinutes(10)));
            historyManager.add(newTask1);
        }
        assertEquals(5, historyManager.getHistory().size(), "Неверное количество задач.");

        historyManager.remove(newTask1.getId() - 4);

        assertEquals(4, historyManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void TestDeletingTheLastItemInTheTaskHistory() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        taskManager.delAllTask();

        Task newTask1 = null;
        for (int i = 1; i <= 5; i++) {
            newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, i, 0, 0), Duration.ofMinutes(10)));
            historyManager.add(newTask1);
        }
        assertEquals(5, historyManager.getHistory().size(), "Неверное количество задач.");

        historyManager.remove(newTask1.getId());

        assertEquals(4, historyManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void TestDeletingTheNextItemInTheTaskHistory() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task newTask1 = null;
        for (int i = 1; i <= 5; i++) {
            newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, i, 0, 0), Duration.ofMinutes(10)));
            historyManager.add(newTask1);
        }
        assertEquals(5, historyManager.getHistory().size(), "Неверное количество задач.");

        historyManager.remove(newTask1.getId() - 2);

        assertEquals(4, historyManager.getHistory().size(), "Неверное количество задач.");
    }


    @Test
    public void TestExceptionLoadFromFile() throws NotFoundException {
        TaskManager taskManager = Managers.getDefault();

        assertDoesNotThrow(taskManager::loadFromFile, "Ошибка loadFromFile()");
    }


}
