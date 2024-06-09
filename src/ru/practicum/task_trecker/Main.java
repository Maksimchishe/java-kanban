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
        Task task1 = taskManager.createTask(new Task("Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка созданного Task: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест1.2: Обновление Task");
        Task task2 = taskManager.updateTask(new Task(task1.getId(), "Обновленная задача", "Обновленное пояснение", Status.NEW));
        System.out.println("Проверка обновленнго Task: " + task2);
        System.out.println("Проверка обновленнго Task в Map: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест1.3: Удаление Task");
        System.out.println("При успешном удалении true: " + taskManager.delIdTask(task1.getId()));
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println();

///////////////////////////////////////////////////////TEST2//////////////////////////////////////////////////////////////

        System.out.println("Тест2.1: Создание Epic");
        Epic epic1 = taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка созданного Epic: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест2.2: Обновление Epic");
        Task epic2 = taskManager.updateEpic(new Epic(epic1.getId(), "Обновленная задача", "Обновленное пояснение", Status.NEW));
        System.out.println("Проверка обновленнго Epic: " + epic2);
        System.out.println("Проверка обновленнго Epic в Map: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест2.3: Удаление Epic");
        System.out.println("При успешном удалении Epic true: " + taskManager.delIdEpic(epic1.getId()));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

///////////////////////////////////////////////////////TEST3//////////////////////////////////////////////////////////////

        System.out.println("Тест3.1: Создание SubTask");

        System.out.println("Создание тестового Epic");
        Epic epicTest = taskManager.createEpic(new Epic("Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка тестового epicTest: " + taskManager.getAllEpics());

        Subtask subTask1 = taskManager.createSubTask(new Subtask(epicTest.getId(), "Задача", "Пояснение", Status.NEW));
        System.out.println("Проверка созданного SubTask: " + taskManager.getAllSubTasks());
        System.out.println();


        System.out.println("Тест3.2: Обновление SubTask");
        Subtask subTask2 = taskManager.updateSubTask(new Subtask(subTask1.getId(), subTask1.getIdEpic(), "Обновленная задача", "Обновленное пояснение", Status.NEW));
        System.out.println("Проверка обновленного SubTask: " + subTask2);
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

        System.out.println("Тест3.3: Удаление SubTask");
        System.out.println("При успешном удалении subTask1 true: " + taskManager.delIdSubTask(subTask1.getId()));
        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
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

            System.out.println("SubTask: " + i + " = " + taskManager.createSubTask(new Subtask(epicTest.getId(), "Наименование"
                    , " Пояснение", status)));
        }

        System.out.println("Удаление SubTask со статусом NEW: " + taskManager.delIdSubTask(4));
        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
        System.out.println("Проверка статуса тестового epicTest DONE: " + taskManager.getAllEpics());
        System.out.println("Удаление всех SubTask по id Epic");
        taskManager.delAllSubTaskIdEpic(epicTest.getId());
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

            System.out.println("SubTask: " + i + " = " + taskManager.createSubTask(new Subtask(epicTest.getId(), "Наименование"
                    , " Пояснение", status)));
        }

        System.out.println("Удаление SubTask со статусом DONE: " + taskManager.delIdSubTask(9));
        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
        System.out.println("Проверка статуса тестового epicTest NEW: " + taskManager.getAllEpics());
        System.out.println("Удаление всех SubTask по id Epic");
        taskManager.delAllSubTaskIdEpic(epicTest.getId());
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

        System.out.println("Тест4.3: Проверка статусов NEW, IN_PROGRESS, DONE");
        System.out.println("Тестовый Epic: " + taskManager.getAllEpics());
        System.out.println("Создание пяти SubTask со статусом NEW(1шт.) остальные DONE");
        for (int i = 0; i < 5; i++) {
            Status status;
            if (i == 0) {
                status = Status.DONE;
            } else if (i == 1) {
                status = Status.IN_PROGRESS;
            } else {
                status = Status.NEW;
            }

            System.out.println("SubTask: " + i + " = " + taskManager.createSubTask(new Subtask(epicTest.getId(), "Наименование"
                    , " Пояснение", status)));
        }

        System.out.println("Список SubTasks: " + taskManager.getAllSubTasks());
        System.out.println("Проверка статуса тестового epicTest IN_PROGRESS: " + taskManager.getAllEpics());
        System.out.println("Удаление всех SubTask по id Epic");
        taskManager.delAllSubTaskIdEpic(epicTest.getId());
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

///////////////////////////////////////////////////////TEST5//////////////////////////////////////////////////////////////

        System.out.println("Тест5.1: Проверка удаления всех SubTask Эпиком");
        System.out.println("Тестовый Epic: " + taskManager.getAllEpics());
        System.out.println("Создание пяти SubTask");
        for (int i = 0; i < 5; i++) {
            System.out.println("SubTask: " + i + " = " + taskManager.createSubTask(new Subtask(epicTest.getId(), "Наименование"
                    , " Пояснение", Status.NEW)));
        }
        System.out.println("Удаление Epic true: " + taskManager.delIdEpic(2));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println();

///////////////////////////////////////////////////////TEST6//////////////////////////////////////////////////////////////

        System.out.println("Тест6.1: Проверка вывода, вывода, удаления по id");
        System.out.println("Создание Task" + taskManager.createTask(new Task("Наименование", "Пояснение", Status.NEW)));
        System.out.println("Вывод Task" + taskManager.getIdTask(24));
        System.out.println("Удаление Task true: " + taskManager.delIdTask(24));
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println();
        System.out.println("Создание Epic" + taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW)));
        System.out.println("Вывод Epic" + taskManager.getIdEpic(25));
        System.out.println("Удаление Epic true: " + taskManager.delIdEpic(25));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();
        System.out.println("Создание Epic" + taskManager.createEpic(new Epic("Наименование", "Пояснение", Status.NEW)));
        System.out.println("Создание SubTask" + taskManager.createSubTask(new Subtask(26, "Наименование", "Пояснение", Status.NEW)));
        System.out.println("Вывод SubTask" + taskManager.getIdSubTasks(27));
        System.out.println("Удаление SubTask true: " + taskManager.delIdSubTask(27));
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Удаление Epic true: " + taskManager.delIdEpic(26));
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

///////////////////////////////////////////////////////TEST7//////////////////////////////////////////////////////////////
        System.out.println("Тест7: Проверка удаления всей Map");
        System.out.println("Тест7.1: Проверка удаления всей Map Task");
        System.out.println("Создание Task");
        for (int i = 0; i < 5; i++) {
            System.out.println("Task: " + i + " = " + taskManager.createTask(new Task("Наименование"
                    , " Пояснение", Status.NEW)));
        }
        System.out.println("Удаление всех Task true: " + taskManager.delAllTask());
        System.out.println("Список Task: " + taskManager.getAllTasks());
        System.out.println();

        System.out.println("Тест7.2: Проверка удаления всей Map Epic");
        System.out.println("Создание Epic");
        for (int i = 0; i < 5; i++) {
            System.out.println("Epic: " + i + " = " + taskManager.createEpic(new Epic("Наименование"
                    , " Пояснение", Status.NEW)));
        }
        System.out.println("Удаление всех Epic true: " + taskManager.delAllEpic());
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();

        System.out.println("Тест7.3: Проверка удаления всей Map SubTask");
        System.out.println("Создание Epic");
        for (int i = 0; i < 5; i++) {
            System.out.println("Epic: " + i + " = " + taskManager.createEpic(new Epic("Наименование"
                    , " Пояснение", Status.NEW)));
        }
        System.out.println("Создание SubTask");
        for (int i = 38; i < 43; i++) {
            System.out.println("SubTask: " + i + " = " + taskManager.createSubTask(new Subtask(i, "Наименование"
                    , " Пояснение", Status.NEW)));
        }
        System.out.println("Удаление всех SubTask");
        taskManager.delAllSubTask();
        System.out.println("Список SubTask: " + taskManager.getAllSubTasks());
        System.out.println("Удаление всех Epic true: " + taskManager.delAllEpic());
        System.out.println("Список Epic: " + taskManager.getAllEpics());
        System.out.println();


    }
}
