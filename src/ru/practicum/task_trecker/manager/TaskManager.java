package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.util.*;

public class TaskManager {
    private static int id; // Уникальный Id
    private final Map<Integer, Task> tasks = new HashMap<>(); // Map для хранения Task
    private final Map<Integer, Epic> epics = new HashMap<>(); // Map для хранения Epic
    private final Map<Integer, Subtask> subTasks = new HashMap<>(); // Map для хранения SubTask

    private int getNextId() { // Формирование нового Id
        return id++;
    }

    void selectStatus(Integer idEpic) { // Метод для формирования статуса
        if (idEpic == null) {
            return;
        }

        Status status;
        boolean statusNew = false;
        boolean statusDone = false;
        boolean statusInProgress = false;

        for (Subtask subtask : subTasks.values()) {
            if (epics.containsKey(idEpic) && subtask.getIdEpic().equals(idEpic)) {
                if (subtask.getStatus() == Status.NEW) {
                    statusNew = true;
                } else if (subtask.getStatus() == Status.DONE) {
                    statusDone = true;
                } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                    statusInProgress = true;
                }
            }
        }

        if (statusNew && !statusDone && !statusInProgress) {
            status = Status.NEW;
        } else if (!statusNew && statusDone && !statusInProgress) {
            status = Status.DONE;
        } else if (!statusNew && !statusDone && !statusInProgress) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }

        epics.get(idEpic).setStatus(status);
    }

    public Task createTask(Task task) { // Создание Task
        task.setId(getNextId());
        Integer id = task.getId();
        tasks.put(id, task);
        return tasks.get(id);
    }

    public Epic createEpic(Epic epic) { // Создание Epic
        epic.setId(getNextId());
        Integer id = epic.getId();
        epics.put(id, epic);
        return epics.get(id);
    }

    public Subtask createSubTask(Subtask subTask) { // Создание SubTask
        subTask.setId(getNextId());
        Integer id = subTask.getId();
        subTasks.put(id, subTask);
        selectStatus(subTasks.get(id).getIdEpic());
        return subTasks.get(id);
    }

    public Task updateTask(Task task) { // Обновление Task
        Integer taskId = task.getId();
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, task);
        }
        return tasks.get(taskId);
    }

    public Epic updateEpic(Epic epic) { // Обновление Epic
        Integer epicId = epic.getId();
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
        }
        return epics.get(epicId);
    }

    public Subtask updateSubTask(Subtask subTask) { // Обновление Sub Task
        Integer subTaskId = subTask.getId();
        if (subTasks.containsKey(subTaskId)) {
            subTasks.put(subTaskId, subTask);
        }
        return subTasks.get(subTaskId);
    }

    public boolean delIdTask(Integer id) { // Удаление Task по Id
        if (id != null && tasks.containsKey(id)) {
            return tasks.remove(id) != null;
        }
        return false;
    }

    public boolean delIdEpic(Integer id) { // Удаление Epic по Id
        if (id == null || !epics.containsKey(id)) {
            return false;
        }
        delAllSubTaskIdEpic(id);
        return epics.remove(id) != null;
    }

    public boolean delIdSubTask(Integer id) { // Удаление SubTask по Id
        Integer idEpic = subTasks.get(id).getIdEpic();
        if (id == null || !subTasks.containsKey(id)) {
            return false;
        }
        boolean del = subTasks.remove(id) != null;
        selectStatus(idEpic);
        return del;
    }

    public boolean delAllTask() { // Очистка Map tasks
        tasks.clear();
        return true;
    }

    public boolean delAllEpic() { // Очистка Map epics
        epics.clear();
        subTasks.clear();
        return true;
    }

    public void delAllSubTaskIdEpic(Integer id) { // Удаление всех SubTask по Id Epic
        if (id == null || !epics.containsKey(id)) {
            return;
        }
        Map<Integer, Subtask> map = new HashMap<>();
        for (Subtask subMap : subTasks.values()) {
            if (!subMap.getIdEpic().equals(id)) {
                map.put(subMap.getId(), subMap);
            }
        }
        subTasks.clear();
        subTasks.putAll(map);
        selectStatus(id);
    }

    public void delAllSubTask() { // Очистка Map subTasks
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
    }

    public List<Task> getIdTask(Integer id) { // Вывод списка Task по Id
        if (id == null || !tasks.containsKey(id)) {
            return null;
        }
        List<Task> listOut = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getId().equals(id)) {
                listOut.add(task);
            }
        }
        return listOut;
    }

    public List<Epic> getIdEpic(Integer id) { // Вывод списка Epic по Id
        if (id == null || !epics.containsKey(id)) {
            return null;
        }
        List<Epic> listOut = new ArrayList<>();
        for (Epic epic : epics.values()) {
            if (epic.getId().equals(id)) {
                listOut.add(epic);
            }
        }
        return listOut;
    }

    public List<Subtask> getIdSubTasks(Integer id) { // Вывод списка SubTask по Id
        if (id == null || !subTasks.containsKey(id)) {
            return null;
        }
        List<Subtask> listOut = new ArrayList<>();
        for (Subtask subtask : subTasks.values()) {
            if (subtask.getId().equals(id)) {
                listOut.add(subtask);
            }
        }
        return listOut;
    }

    public List<Task> getAllTasks() { // Вывод всей Map tasks
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() { // Вывод всей Map epics
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubTasks() { // Вывод всей Map subTasks
        return new ArrayList<>(subTasks.values());
    }


}
