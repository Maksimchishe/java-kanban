package ru.practicum.task_trecker;

import ru.practicum.task_trecker.manager.TaskManager;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

public class Main {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        TaskManager taskManager = new TaskManager();

///////////////////////////////////////////////////////TEST1//////////////////////////////////////////////////////////////

        System.out.println("Тест1.1: Создание Task");
        taskManager.createTask(new Task("Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка созданного Task: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест1.2: Обновление Task");
        taskManager.updateTask(new Task(0, "Обновленная задача", "Обновленное пояснение", Status.NEW));
        System.out.println("Проверка обновленнго Task в Map: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест1.3: Удаление Task");
        System.out.println("При успешном удалении true: " + taskManager.deleteTaskById(0));
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println();

///////////////////////////////////////////////////////TEST2//////////////////////////////////////////////////////////////

        System.out.println("Тест2.1: Создание Epic");
        taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка созданного Epic: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест2.2: Обновление Epic");
        taskManager.updateEpic(new Epic(1, "Обновленная задача", "Обновленное пояснение", Status.NEW));
        System.out.println("Проверка обновленнго Epic в Map: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест2.3: Удаление Epic");
        System.out.println("При успешном удалении Epic true: " + taskManager.deleteEpicById(1));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

///////////////////////////////////////////////////////TEST3//////////////////////////////////////////////////////////////

        System.out.println("Тест3.1: Создание SubTask");
        System.out.println("Создание тестового Epic");
        taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка тестового epicTest: " + taskManager.getAllEpics());
        System.out.println("Создание SubTask");
        taskManager.createSubTask(new Subtask(2, "Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка созданного SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(2));
        System.out.println();


        System.out.println("Тест3.2: Обновление SubTask");
        taskManager.updateSubTask(new Subtask(3, 2, "Обновленная задача", "Обновленное пояснение", Status.NEW));
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

        System.out.println("Тест3.3: Удаление SubTask");
        System.out.println("При успешном удалении subTask1 true: " + taskManager.deleteSubTaskById(3));
        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(2));
        System.out.println();

///////////////////////////////////////////////////////TEST4//////////////////////////////////////////////////////////////

        System.out.println("Тест4.1: Проверка статусов 1 NEW остальные DONE");
        System.out.println("Тестовый Epic: " + taskManager.getAllEpics());
        System.out.println("Создание пяти SubTask со статусом NEW(1шт.) остальные DONE");
        for (int i = 0; i < 5; i++) {
            Status status;
            if (i == 0) {
                status = Status.NEW;
            } else {
                status = Status.DONE;
            }
            taskManager.createSubTask(new Subtask(2, "Наименование", " Пояснение", status));
        }
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(2));
        System.out.println("Удаление SubTask со статусом NEW: " + taskManager.deleteSubTaskById(4));
        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
        System.out.println("Проверка статуса тестового epicTest DONE: " + taskManager.getAllEpics());
        System.out.println("Удаление всех SubTask по id Epic");
        taskManager.deleteAllSubTasksByEpicId(2);
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

        System.out.println("Тест4.2: Проверка статусов 1 DONE остальные NEW");
        System.out.println("Тестовый Epic: " + taskManager.getAllEpics());
        System.out.println("Создание пяти SubTask со статусом NEW(1шт.) остальные DONE");
        for (int i = 0; i < 5; i++) {
            Status status;
            if (i == 0) {
                status = Status.DONE;
            } else {
                status = Status.NEW;
            }
            taskManager.createSubTask(new Subtask(2, "Наименование", " Пояснение", status));
        }
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(2));
        System.out.println("Удаление SubTask со статусом DONE: " + taskManager.deleteSubTaskById(9));
        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
        System.out.println("Проверка статуса тестового epicTest NEW: " + taskManager.getAllEpics());
        System.out.println("Удаление всех SubTask по id Epic");
        taskManager.deleteAllSubTasksByEpicId(2);
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

        System.out.println("Тест4.3: Проверка статусов NEW, IN_PROGRESS, DONE");
        System.out.println("Тестовый Epic: " + taskManager.getAllEpics());
        System.out.println("Создание пяти SubTask со статусом NEW(1шт.), IN_PROGRESS(1шт.), остальные DONE");
        for (int i = 0; i < 5; i++) {
            Status status;
            if (i == 0) {
                status = Status.DONE;
            } else if (i == 1) {
                status = Status.IN_PROGRESS;
            } else {
                status = Status.NEW;
            }
            taskManager.createSubTask(new Subtask(2, "Наименование", " Пояснение", status));
        }
        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(2));
        System.out.println("Проверка статуса тестового epicTest IN_PROGRESS: " + taskManager.getAllEpics());
        System.out.println("Удаление всех SubTask по id Epic");
        taskManager.deleteAllSubTasksByEpicId(2);
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

///////////////////////////////////////////////////////TEST5//////////////////////////////////////////////////////////////

        System.out.println("Тест5.1: Проверка удаления всех SubTask Эпиком");
        System.out.println("Тестовый Epic: " + taskManager.getAllEpics());
        System.out.println("Создание пяти SubTask");
        for (int i = 0; i < 5; i++) {
            taskManager.createSubTask(new Subtask(2, "Наименование", " Пояснение", Status.NEW));
        }
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(2));
        System.out.println("Удаление Epic true: " + taskManager.deleteEpicById(2));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

///////////////////////////////////////////////////////TEST6//////////////////////////////////////////////////////////////
        System.out.println("Тест6.1: Проверка ввода, вывода, удаления по id Task");
        System.out.println("Создание Task");
        taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW));
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println("Удаление Task true: " + taskManager.deleteTaskById(24));
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест6.2: Проверка ввода, вывода, удаления по id Epic");
        System.out.println("Создание Epic");
        taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Удаление Epic true: " + taskManager.deleteEpicById(25));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест6.3: Проверка ввода, вывода, удаления по id SubTask");
        System.out.println("Создание Epic");
        taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Создание SubTask");
        taskManager.createSubTask(new Subtask(26, "Наименование", "Пояснение", Status.NEW));
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(26));
        System.out.println("Вывод SubTask" + taskManager.getIdSubTasks(27));
        System.out.println("Удаление SubTask true: " + taskManager.deleteSubTaskById(27));
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Удаление Epic true: " + taskManager.deleteEpicById(26));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

///////////////////////////////////////////////////////TEST7//////////////////////////////////////////////////////////////
        System.out.println("Тест7.1: Проверка удаления всей Map Task");
        System.out.println("Создание Task");
        for (int i = 0; i < 5; i++) {
            taskManager.createTask(new Task("Наименование", " Пояснение", Status.NEW));
        }
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println("Удаление всех Task");
        taskManager.delAllTask();
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест7.2: Проверка удаления всей Map Epic");
        System.out.println("Создание Epic");
        for (int i = 0; i < 5; i++) {
            taskManager.createEpic(new Epic("Наименование", " Пояснение", Status.NEW));
        }
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Удаление всех Epic");
        taskManager.delAllEpic();
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест7.3: Проверка удаления всей Map SubTask");
        System.out.println("Создание Epic");
        for (int i = 0; i < 5; i++) {
            taskManager.createEpic(new Epic("Наименование", " Пояснение", Status.NEW));
        }
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Создание SubTask");
        for (int i = 38; i < 43; i++) {
            taskManager.createSubTask(new Subtask(i, "Наименование", " Пояснение", Status.NEW));
        }
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(38));
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(39));
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(40));
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(41));
        System.out.println("Проверка ListId в Epic: " + taskManager.testIdEpic(42));
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Удаление всех SubTask");
        taskManager.delAllSubTask();
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Удаление всех Epic");
        taskManager.delAllEpic();
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();


    }
}
