package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    TreeSet<Task> getPrioritizedTasks();

    boolean validationTimeTask(Task task) throws NotFoundException;

    List<Task> getTaskHistory();

    <T> void saveInHistory(T task) throws NotFoundException;

    Task createTask(Task task) throws NotFoundException;

    Epic createEpic(Epic epic) throws NotFoundException;

    Subtask createSubTask(Subtask subTask) throws NotFoundException;

    Task updateTask(Task task) throws NotFoundException;

    Epic updateEpic(Epic epic) throws NotFoundException;

    Subtask updateSubTask(Subtask subTask) throws NotFoundException;

    Task getTaskById(Integer id) throws NotFoundException;

    Epic getEpicById(Integer id) throws NotFoundException;

    Subtask getSubTaskById(Integer id) throws NotFoundException;

    List<Subtask> getAllSubTaskById(Integer idEpic) throws NotFoundException;

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubTasks();

    boolean deleteTaskById(Integer id) throws NotFoundException;

    boolean deleteEpicById(Integer id) throws NotFoundException;

    boolean deleteSubTaskById(Integer id) throws NotFoundException;

    void delAllTask();

    void delAllEpic();

    void deleteAllSubTasksByEpicId(Integer id) throws NotFoundException;

    void delAllSubTask();

    void loadFromFile() throws NotFoundException;

    void updateSubTaskToEpic(Integer idEpic) throws NotFoundException;

}

