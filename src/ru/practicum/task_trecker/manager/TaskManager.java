package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getTaskHistory();

    <T> void saveInHistory(T task);

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubTask(Subtask subTask);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubTask(Subtask subTask);

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubTaskById(Integer id);

    List<Subtask> getAllSubTaskById(Integer idEpic);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubTasks();

    boolean deleteTaskById(Integer id);

    boolean deleteEpicById(Integer id);

    boolean deleteSubTaskById(Integer id);

    void delAllTask();

    void delAllEpic();

    void deleteAllSubTasksByEpicId(Integer id);

    void delAllSubTask();

    void loadFromFile();
}

