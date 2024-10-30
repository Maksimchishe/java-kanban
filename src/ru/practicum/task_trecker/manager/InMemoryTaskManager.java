package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.task.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private static int id;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();

    HistoryManager history = Managers.getDefaultHistory();

    private int getNextId() {
        return ++id;
    }

    public void setId(Integer idMax) throws NotFoundException {
        if (idMax == null) {
            throw NotFoundException.notFound();
        }

        id = idMax;
    }

    public int getId() {
        return id;
    }

    private void selectStatus(Integer idEpic) throws NotFoundException {
        if (idEpic == null) {
            throw NotFoundException.notFound();
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

        updateSubTaskToEpic(idEpic);
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        List<Task> listTask = new ArrayList<>();
        listTask.addAll(tasks.values());
        listTask.addAll(subTasks.values());
        return listTask.stream()
                .filter(t -> t.getStartTime() != null)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
    }

    @Override
    public boolean validationTimeTask(Task task) throws NotFoundException {
        if (task == null) {
            throw NotFoundException.objectNull();
        }

        if (task.getStartTime() == null) {
            return false;
        }

        return getPrioritizedTasks().stream().anyMatch(t -> task.getStartTime().isBefore(t.getEndTimeTask())
                && task.getEndTimeTask().isAfter(t.getStartTime()));
    }

    @Override
    public void updateSubTaskToEpic(Integer idEpic) throws NotFoundException {
        if (idEpic == null) {
            throw NotFoundException.notFound();
        }

        Comparator<Subtask> comparator = Comparator.comparing(Subtask::getStartTime);
        TreeSet<Subtask> setSubTask = new TreeSet<>(comparator);
        List<Duration> listDuration = new ArrayList<>();

        setSubTask.addAll(subTasks.values().stream()
                .filter(t -> t.getIdEpic().equals(idEpic))
                .filter(t -> t.getStartTime() != null)
                .peek(t -> listDuration.add(t.getDuration()))
                .collect(Collectors.toSet()));

        if (!setSubTask.isEmpty()) {
            epics.get(idEpic).setStartTime(setSubTask.first().getStartTime());
            epics.get(idEpic).setDuration(listDuration.stream().reduce(Duration.ZERO, Duration::plus));
            epics.get(idEpic).setEndTime(setSubTask.last().getEndTimeEpic());
        }
    }

    @Override
    public Task createTask(Task task) throws NotFoundException {

        if (task == null) {
            throw NotFoundException.objectNull();
        }

        if (task.getStartTime() != null && validationTimeTask(task)) {
            throw NotFoundException.notAcceptable();
        }

        task.setId(getNextId());
        Integer id = task.getId();
        tasks.put(id, task);
        return tasks.get(id);
    }

    @Override
    public Epic createEpic(Epic epic) throws NotFoundException {
        if (epic == null) {
            throw NotFoundException.objectNull();
        }

        epic.setId(getNextId());
        Integer id = epic.getId();
        epics.put(id, epic);
        return epics.get(id);
    }

    @Override
    public Subtask createSubTask(Subtask subTask) throws NotFoundException {
        if (subTask == null) {
            throw NotFoundException.objectNull();
        }

        if (validationTimeTask(subTask)) {
            throw NotFoundException.notAcceptable();
        }

        subTask.setId(getNextId());
        Integer id = subTask.getId();
        subTasks.put(id, subTask);
        selectStatus(subTasks.get(id).getIdEpic());
        return subTasks.get(id);
    }

    @Override
    public Task updateTask(Task task) throws NotFoundException {
        if (task == null) {
            throw NotFoundException.objectNull();
        }

        Integer taskId = task.getId();
        if (!tasks.containsKey(taskId)) {
            throw NotFoundException.NotContainsKey();
        }

        if ((tasks.get(taskId).getStartTime() !=null && tasks.get(taskId).getDuration() !=null)
                && !(tasks.get(taskId).getStartTime().equals(task.getStartTime())
                && tasks.get(taskId).getDuration().equals(task.getDuration()))
                && validationTimeTask(task)) {
            throw NotFoundException.notAcceptable();
        }

        tasks.put(taskId, task);
        return tasks.get(taskId);
    }

    @Override
    public Epic updateEpic(Epic epic) throws NotFoundException {
        if (epic == null) {
            throw NotFoundException.objectNull();
        }

        Integer epicId = epic.getId();

        if (!epics.containsKey(epicId)) {
            throw NotFoundException.NotContainsKey();
        }

        epics.put(epicId, epic);
        updateSubTaskToEpic(epic.getId());

        return epics.get(epicId);
    }

    @Override
    public Subtask updateSubTask(Subtask subTask) throws NotFoundException {
        if (subTask == null) {
            throw NotFoundException.objectNull();
        }

        Integer subTaskId = subTask.getId();
        Integer idEpic = subTask.getIdEpic();

        if (!subTasks.containsKey(subTaskId)) {
            throw NotFoundException.NotContainsKey();
        }

        if ((subTasks.get(subTaskId).getStartTime() !=null && subTasks.get(subTaskId).getDuration() !=null)
                && !(subTasks.get(subTaskId).getStartTime().equals(subTask.getStartTime())
                && subTasks.get(subTaskId).getDuration().equals(subTask.getDuration()))
                && validationTimeTask(subTask)) {
            throw NotFoundException.notAcceptable();
        }

        subTasks.put(subTaskId, subTask);
        selectStatus(idEpic);
        return subTasks.get(subTaskId);
    }

    @Override
    public <T> void saveInHistory(T task) throws NotFoundException {
        if (task == null) {
            throw NotFoundException.objectNull();
        }
        history.add((Task) task);
    }

    @Override
    public List<Task> getTaskHistory() {
        return history.getHistory();
    }

    @Override
    public Task getTaskById(Integer id) throws NotFoundException {
        if (id == null) {
            throw NotFoundException.notFound();
        }
        saveInHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) throws NotFoundException {
        if (id == null) {
            throw NotFoundException.notFound();
        }

        saveInHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubTaskById(Integer id) throws NotFoundException {
        if (id == null) {
            throw NotFoundException.notFound();
        }
        saveInHistory(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public List<Subtask> getAllSubTaskById(Integer idEpic) throws NotFoundException {

        if (idEpic == null) {
            throw NotFoundException.notFound();
        }

        if (!epics.containsKey(idEpic)) {
            throw NotFoundException.NotContainsKey();
        }

        return subTasks.values().stream()
                .filter(s -> s.getIdEpic().equals(idEpic))
                .toList();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public boolean deleteTaskById(Integer id) throws NotFoundException {
        if (id == null) {
            throw NotFoundException.notFound();
        }

        if (!tasks.containsKey(id)) {
            throw NotFoundException.NotContainsKey();
        }

        history.remove(id);
        return tasks.remove(id) != null;
    }

    @Override
    public boolean deleteEpicById(Integer id) throws NotFoundException {
        if (id == null) {
            throw NotFoundException.notFound();
        }

        if (!epics.containsKey(id)) {
            throw NotFoundException.NotContainsKey();
        }

        deleteAllSubTasksByEpicId(id);
        history.remove(id);
        return epics.remove(id) != null;
    }

    @Override
    public boolean deleteSubTaskById(Integer id) throws NotFoundException {

        if (id == null) {
            throw NotFoundException.notFound();
        }

        if (!subTasks.containsKey(id)) {
            throw NotFoundException.NotContainsKey();
        }

        Integer idEpic = subTasks.get(id).getIdEpic();

        boolean del = subTasks.remove(id) != null;
        selectStatus(idEpic);
        history.remove(id);
        return del;
    }

    @Override
    public void delAllTask() {
        tasks.clear();
    }

    @Override
    public void delAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasksByEpicId(Integer id) throws NotFoundException {

        if (id == null) {
            throw NotFoundException.notFound();
        }

        if (!epics.containsKey(id)) {
            throw NotFoundException.NotContainsKey();
        }

        subTasks.values().stream()
                .filter(s -> s.getIdEpic().equals(id))
                .peek(s -> history.remove(s.getId()))
                .forEach(s -> {
                    try {
                        deleteSubTaskById(s.getId());
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

        selectStatus(id);
    }

    @Override
    public void delAllSubTask() {
        subTasks.clear();
        epics.values().forEach(e -> {
            e.setStatus(Status.NEW);
            try {
                updateSubTaskToEpic(e.getId());
            } catch (NotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void loadFromFile() throws NotFoundException {
    }

}

