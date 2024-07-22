package test;

import org.junit.jupiter.api.Test;
import ru.practicum.task_trecker.manager.*;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    @Test
    void managersTest() {
        TaskManager taskManager = Managers.getDefault();
        assertInstanceOf(InMemoryTaskManager.class, taskManager, "Класс не является экземпляром менеджера.");

        HistoryManager historyManager = Managers.getDefaultHistory();
        assertInstanceOf(InMemoryHistoryManager.class, historyManager, "Класс не является экземпляром менеджера.");
    }

    @Test
    void usesAllTypeClassTest() {
        TaskManager taskManager = Managers.getDefault();

        for (int i = 0; i < 5; i++) {
            Task newTask = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));
            taskManager.getTaskById(newTask.getId());
            Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
            taskManager.getEpicById(newEpic.getId());
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.getSubTaskById(newSubTask.getId());
        }

        assertEquals(15, taskManager.getTaskHistory().size(), "Неверное количество задач.");

        boolean typeEpic = false;
        boolean typeSubTask = false;
        for (Task task : taskManager.getTaskHistory()) {
            if (task instanceof Epic) {
                typeEpic = true;
            }
            if (task instanceof Subtask) {
                typeSubTask = true;
            }
        }
        assertTrue(typeEpic, "В History отсутствует тип Epic");
        assertTrue(typeSubTask, "В History отсутствует тип SubTask");
    }

    @Test
    void savePreviousVersionTaskTest() {
        TaskManager taskManager = Managers.getDefault();

        Task newTask = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));
        taskManager.getTaskById(newTask.getId());
        taskManager.updateTask(new Task(newTask.getId(), "Обновленное наименование", "Обновленное пояснение", Status.NEW));
        taskManager.getTaskById(newTask.getId());

        assertEquals(1, taskManager.getTaskHistory().size(), "Неверное количество задач.");
        assertEquals(taskManager.getTaskHistory().getFirst().getId(), taskManager.getTaskHistory().getLast().getId(), "Id задач неравны.");

        assertEquals(taskManager.getTaskHistory().getFirst().getName(), "Обновленное наименование", "Поле name не совпадает у дочернего экземпляра.");
        assertEquals(taskManager.getTaskHistory().getFirst().getDescription(), "Обновленное пояснение", "Поле description не совпадает у дочернего экземпляра.");
        assertEquals(taskManager.getTaskHistory().getFirst().getStatus(), Status.NEW, "Поле status не совпадает у дочернего экземпляра.");
    }

    @Test
    void createAndUpdateTaskTest() {
        TaskManager taskManager = Managers.getDefault();
        Task newTask = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));
        assertNotNull(newTask, "Задача не найдена.");
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач.");
        Task taskById = taskManager.getTaskById(newTask.getId());
        assertEquals(taskById, newTask, "Задачи не совпадают.");

        int idNewTask = newTask.getId();
        Task updateTask = taskManager.updateTask(new Task(newTask.getId(), "Обновленное наименование", "Обновленное пояснение", Status.NEW));
        assertNotNull(updateTask, "Задача не найдена.");
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач.");
        assertEquals(idNewTask, updateTask.getId(), "id newTask и updateTask не совпадают.");
    }

    @Test
    void createAndUpdateEpicTest() {
        TaskManager taskManager = Managers.getDefault();
        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        assertNotNull(newEpic, "Задача не найдена.");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество задач.");
        Epic epicById = taskManager.getEpicById(newEpic.getId());
        assertEquals(epicById, newEpic, "Задачи не совпадают.");

        int idNewEpic = newEpic.getId();
        Epic updateEpic = taskManager.updateEpic(new Epic(newEpic.getId(), "Обновленное наименование", "Обновленное пояснение", Status.NEW));
        assertNotNull(updateEpic, "Задача не найдена.");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество задач.");
        assertEquals(idNewEpic, updateEpic.getId(), "id newTask и updateTask не совпадают.");
    }

    @Test
    void createAndUpdateSubTaskTest() {
        TaskManager taskManager = Managers.getDefault();
        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
        assertNotNull(newSubTask, "Задача не найдена.");
        assertEquals(1, taskManager.getAllSubTasks().size(), "Неверное количество задач.");
        Subtask subTaskById = taskManager.getSubTaskById(newSubTask.getId());
        assertEquals(subTaskById, newSubTask, "Задачи не совпадают.");

        int idNewSubTask = newSubTask.getId();
        Subtask updateSubTask = taskManager.updateSubTask(new Subtask(newSubTask.getId(), newEpic.getId(), "Обновленное наименование", "Обновленное пояснение", Status.NEW));

        assertNotNull(updateSubTask, "Задача не найдена.");
        assertEquals(1, taskManager.getAllSubTasks().size(), "Неверное количество задач.");
        assertEquals(idNewSubTask, updateSubTask.getId(), "id newSubTask и updateSubTask не совпадают.");
    }

    @Test
    void createAndGetSubTaskById() {
        TaskManager taskManager = Managers.getDefault();
        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        assertNotNull(newEpic, "Задача не найдена.");
        for (int i = 1; i < 11; i++) {
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            assertNotNull(newSubTask, "Задача не найдена.");

            Subtask subTaskById = taskManager.getSubTaskById(newSubTask.getId());
            assertNotNull(subTaskById, "Задача не найдена.");
            assertEquals(newSubTask, subTaskById, "newSubTask и subTaskById не совпадают.");
        }
        assertEquals(10, taskManager.getAllSubTasks().size(), "Неверное количество задач.");
    }

    @Test
    void checkingForMatchingFieldsForTask() {
        TaskManager taskManager = Managers.getDefault();
        Task newTask = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));

        assertEquals(newTask.getName(), "Наименование", "Поле Name не совпадает.");
        assertEquals(newTask.getDescription(), "Пояснение", "Поле Description не совпадает.");
        assertEquals(newTask.getStatus(), Status.NEW, "Поле Status не совпадает.");
    }

    @Test
    void checkingForMatchingFieldsForEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));

        assertEquals(newEpic.getName(), "Наименование", "Поле Name не совпадает.");
        assertEquals(newEpic.getDescription(), "Пояснение", "Поле Description не совпадает.");
        assertEquals(newEpic.getStatus(), Status.NEW, "Поле Status не совпадает.");
    }

    @Test
    void checkingForMatchingFieldsForSubTask() {
        TaskManager taskManager = Managers.getDefault();
        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));

        assertEquals(newSubTask.getIdEpic(), newEpic.getId(), "Поле idEpic не совпадает.");
        assertEquals(newSubTask.getName(), "Наименование", "Поле Name не совпадает.");
        assertEquals(newSubTask.getDescription(), "Пояснение", "Поле Description не совпадает.");
        assertEquals(newSubTask.getStatus(), Status.NEW, "Поле Status не совпадает.");
    }

    @Test
    void deleteTaskByIdTest() {
        TaskManager taskManager = Managers.getDefault();
        Task newTask = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));
        assertNotNull(newTask, "Задача не найдена.");
        assertTrue(taskManager.deleteTaskById(newTask.getId()), "Задача не удалена.");
    }

    @Test
    void deleteEpicByIdTest() {
        TaskManager taskManager = Managers.getDefault();
        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        assertNotNull(newEpic, "Задача не найдена.");
        assertTrue(taskManager.deleteEpicById(newEpic.getId()), "Задача не удалена.");
    }

    @Test
    void deleteSubTaskByIdTest() {
        TaskManager taskManager = Managers.getDefault();
        Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
        assertNotNull(newSubTask, "Задача не найдена.");
        assertTrue(taskManager.deleteSubTaskById(newSubTask.getId()), "Задача не удалена.");

    }

    @Test
    void deleteAllTaskTest() {
        TaskManager taskManager = Managers.getDefault();

        for (int i = 0; i < 5; i++) {
            Task newTask = taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));
            taskManager.getTaskById(newTask.getId());
        }
        assertEquals(5, taskManager.getAllTasks().size(), "Неверное количество задач.");
        taskManager.delAllTask();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задачи не удалены.");
    }

    @Test
    void deleteAllSubEpicTest() {
        TaskManager taskManager = Managers.getDefault();

        for (int i = 0; i < 5; i++) {
            Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
            taskManager.getEpicById(newEpic.getId());
        }
        assertEquals(5, taskManager.getAllEpics().size(), "Неверное количество задач.");
        taskManager.delAllEpic();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Задачи не удалены.");
    }

    @Test
    void deleteAllSubTaskTest() {
        TaskManager taskManager = Managers.getDefault();

        for (int i = 0; i < 5; i++) {
            Epic newEpic = taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
            taskManager.getEpicById(newEpic.getId());
            Subtask newSubTask = taskManager.createSubTask(new Subtask(newEpic.getId(), "Наименование", "Пояснение", Status.NEW));
            taskManager.getSubTaskById(newSubTask.getId());
        }
        assertEquals(5, taskManager.getAllSubTasks().size(), "Неверное количество задач.");
        taskManager.delAllSubTask();
        taskManager.delAllEpic();

        assertTrue(taskManager.getAllSubTasks().isEmpty(), "Задачи не удалены.");
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void historyAddAndDeleteTaskTest() {
        TaskManager taskManager = Managers.getDefault();

        for (int i = 0; i < 5; i++) {
            taskManager.createTask(new Task("Задача", "Пояснение", Status.NEW));
        }

        List<Task> listTask = taskManager.getAllTasks();
        assertEquals(5, listTask.size(), "Неверное количество задач.");

        int current = 0;

        for (Task task : listTask) {
            taskManager.getTaskById(task.getId());
            current++;
            assertEquals(current, taskManager.getTaskHistory().size(), "Неверное количество задач.");
        }

        assertEquals(5, taskManager.getTaskHistory().size(), "Неверное количество задач.");

        for (Task task : listTask) {
            taskManager.deleteTaskById(task.getId());
            current--;
            assertEquals(current, taskManager.getTaskHistory().size(), "Неверное количество задач.");
        }

        assertEquals(0, taskManager.getTaskHistory().size(), "Неверное количество задач.");

    }

    @Test
    void historyAddAndDeleteEpicTest() {
        TaskManager taskManager = Managers.getDefault();

        for (int i = 0; i < 5; i++) {
            taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));
        }

        List<Epic> listEpic = taskManager.getAllEpics();
        assertEquals(5, listEpic.size(), "Неверное количество задач.");

        int current = 0;

        for (Epic epic : listEpic) {
            taskManager.getEpicById(epic.getId());
            current++;
            assertEquals(current, taskManager.getTaskHistory().size(), "Неверное количество задач.");
        }

        assertEquals(5, taskManager.getTaskHistory().size(), "Неверное количество задач.");

        for (Epic epic : listEpic) {
            taskManager.deleteEpicById(epic.getId());
            current--;
            assertEquals(current, taskManager.getTaskHistory().size(), "Неверное количество задач.");
        }

        assertEquals(0, taskManager.getTaskHistory().size(), "Неверное количество задач.");

    }

    @Test
    void historyAddAndDeleteSubTaskTest() {
        TaskManager taskManager = Managers.getDefault();

        Epic testEpic = taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));

        for (int i = 0; i < 5; i++) {
            taskManager.createSubTask(new Subtask(testEpic.getId(), "Задача", "Пояснение", Status.NEW));
        }

        List<Subtask> listSubTaskEpic = taskManager.getAllSubTaskById(testEpic.getId());
        assertEquals(5, listSubTaskEpic.size(), "Неверное количество задач.");

        int current = 0;

        for (Subtask subTask : listSubTaskEpic) {
            taskManager.getSubTaskById(subTask.getId());
            current++;
            assertEquals(current, taskManager.getTaskHistory().size(), "Неверное количество задач.");
        }

        assertEquals(5, taskManager.getTaskHistory().size(), "Неверное количество задач.");

        for (Subtask subTask : listSubTaskEpic) {
            taskManager.deleteSubTaskById(subTask.getId());
            current--;
            assertEquals(current, taskManager.getTaskHistory().size(), "Неверное количество задач.");
        }

        assertEquals(0, taskManager.getTaskHistory().size(), "Неверное количество задач.");
    }


}