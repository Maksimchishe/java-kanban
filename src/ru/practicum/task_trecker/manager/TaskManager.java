package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.Status;
import ru.practicum.task_trecker.task.Subtask;
import ru.practicum.task_trecker.task.Task;

import java.util.*;

public class TaskManager {
    private static int id;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subTasks = new HashMap<>();

    private int getNextId() {
        return id++;
    }

    void selectStatus(Integer idEpic) {
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

        //Добавление/Обновление списка id SubTask в Epic
        List<Integer> listIdSubTasks = new ArrayList<>();
        for (Subtask subtask : subTasks.values()) {
            if (subtask.getIdEpic().equals(idEpic)) {
                listIdSubTasks.add(subtask.getId());
            }
        }
        epics.get(idEpic).addListIdSubTask(listIdSubTasks);
    }

    public void createTask(Task task) {
        task.setId(getNextId());
        Integer id = task.getId();
        tasks.put(id, task);
    }

    public void createEpic(Epic epic) {
        epic.setId(getNextId());
        Integer id = epic.getId();
        epics.put(id, epic);
    }

    public void createSubTask(Subtask subTask) {
        subTask.setId(getNextId());
        Integer id = subTask.getId();
        subTasks.put(id, subTask);
        selectStatus(subTasks.get(id).getIdEpic());
    }

    public void updateTask(Task task) {
        Integer taskId = task.getId();
        if (tasks.containsKey(taskId)) {
            tasks.put(taskId, task);
        }
    }

    public void updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
        }
    }

    public void updateSubTask(Subtask subTask) {
        Integer subTaskId = subTask.getId();
        Integer idEpic = subTask.getIdEpic();
        if (subTasks.containsKey(subTaskId)) {
            subTasks.put(subTaskId, subTask);
        }
        selectStatus(idEpic);
    }

    public boolean deleteTaskById(Integer id) {
        if (id != null && tasks.containsKey(id)) {
            return tasks.remove(id) != null;
        }
        return false;
    }

    public boolean deleteEpicById(Integer id) {
        if (id == null || !epics.containsKey(id)) {
            return false;
        }

        deleteAllSubTasksByEpicId(id);

        return epics.remove(id) != null;
    }

    public boolean deleteSubTaskById(Integer id) {
        Integer idEpic = subTasks.get(id).getIdEpic();
        if (id == null || !subTasks.containsKey(id)) {
            return false;
        }
        boolean del = subTasks.remove(id) != null;
        selectStatus(idEpic);
        return del;
    }

    public void delAllTask() {
        tasks.clear();
    }

    public void delAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasksByEpicId(Integer id) {
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

    public void delAllSubTask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
        }
    }

    public List<Subtask> getIdSubTasks(Integer id) {
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

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public List<Integer> testIdEpic(Integer idEpic) {
        return epics.get(idEpic).getListIdSubTask();
    }


}
