package ru.practicum.task_trecker.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ALL")
public class NewTaskManagerTest extends TaskManagerTest {

    @Test
    void TestStatusEpicToAllSubtasksWithTheNewStatus() {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
        }

        assertEquals(Status.NEW, newEpic.getStatus(), "Неверный Status Epic.");
    }

    @Test
    void TestStatusEpicToAllSubtasksWithTheDoneStatus() {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.DONE));
        }

        assertEquals(Status.DONE, newEpic.getStatus(), "Неверный Status Epic.");
    }

    @Test
    void TestStatusEpicToAllSubtasksWithTheNewAndDoneStatus() {
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
    void TestStatusEpicToAllSubtasksWithTheInProgressStatus() {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.IN_PROGRESS));
        }

        assertEquals(Status.IN_PROGRESS, newEpic.getStatus(), "Неверный Status Epic.");
    }

    @Test
    void TestAvailabilityOfEpicForSubTask() {
        TaskManager taskManager = Managers.getDefault();

        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Наименование", "Пояснение", Status.IN_PROGRESS));
        }

        assertNotNull(taskManager.getEpicById(newEpic.getId()), "Epic не найден.");
    }

    @Test
    void TestThePresenceOfTheIntersectionOfTimeSegments() {
        TaskManager taskManager = Managers.getDefault();

        Task newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 0, 0), 10));
        assertNotNull(newTask1, "Наличие пересечения временных отрезков.");

        Task newTask2 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 0, 55, 0), 10));
        assertNull(newTask2, "Ошибка определения пересечения временных отрезков StartTime существующего Task.");

        Task newTask3 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 30, 0), 10));
        assertNotNull(newTask3, "Наличие пересечения временных отрезков.");

        Task newTask4 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 35, 0), 10));
        assertNull(newTask4, "Ошибка определения пересечения временных отрезков EndTime существующего Task.");

        Task newTask5 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 15, 0), 10));
        assertNotNull(newTask5, "Наличие пересечения временных отрезков.");

    }

    @Test
    void TestEmptyTaskHistory() {
        TaskManager taskManager = Managers.getDefault();

        assertTrue(taskManager.getTaskHistory().isEmpty(), "История задач не пуста.");

    }

    @Test
    void TestDuplicationInTheTaskHistory() {
        TaskManager taskManager = Managers.getDefault();

        Task newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, 1, 0, 0), 10));
        taskManager.getTaskById(newTask1.getId());
        taskManager.getTaskById(newTask1.getId());
        taskManager.getTaskById(newTask1.getId());

        long count = taskManager.getTaskHistory().stream()
                .filter(t -> t.getId().equals(newTask1.getId()))
                .count();

        assertFalse(count > 1, "Дублирование в истории задач.");

    }

    @Test
    void TestDeletingTheFirstItemInTheTaskHistory() {
        TaskManager taskManager = Managers.getDefault();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task newTask1 = null;
        for (int i = 1; i <= 5; i++) {
            newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, i, 0, 0), 10));
            historyManager.add(newTask1);
        }
        assertEquals(5, historyManager.getHistory().size(), "Неверное количество задач.");

        historyManager.remove(newTask1.getId() - 4);

        assertEquals(4, historyManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void TestDeletingTheLastItemInTheTaskHistory() {
        TaskManager taskManager = Managers.getDefault();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task newTask1 = null;
        for (int i = 1; i <= 5; i++) {
            newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, i, 0, 0), 10));
            historyManager.add(newTask1);
        }
        assertEquals(5, historyManager.getHistory().size(), "Неверное количество задач.");

        historyManager.remove(newTask1.getId());

        assertEquals(4, historyManager.getHistory().size(), "Неверное количество задач.");
    }

    @Test
    void TestDeletingTheNextItemInTheTaskHistory() {
        TaskManager taskManager = Managers.getDefault();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task newTask1 = null;
        for (int i = 1; i <= 5; i++) {
            newTask1 = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW, LocalDateTime.of(2024, 10, 8, i, 0, 0), 10));
            historyManager.add(newTask1);
        }
        assertEquals(5, historyManager.getHistory().size(), "Неверное количество задач.");

        historyManager.remove(newTask1.getId() - 2);

        assertEquals(4, historyManager.getHistory().size(), "Неверное количество задач.");
    }


    @Test
    public void TestExceptionLoadFromFile() {
        TaskManager taskManager = Managers.getDefault();

        assertDoesNotThrow(taskManager::loadFromFile, "Ошибка loadFromFile()");
    }


}
