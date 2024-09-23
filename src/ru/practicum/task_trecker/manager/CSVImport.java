package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.*;

public class CSVImport {

    private final Integer id;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private final Type type;


    public CSVImport(Integer id, Task task, Type type) {
        this.id = id;
        this.task = task;
        this.type = type;
    }

    public CSVImport(Integer id, Epic epic, Type type) {
        this.id = id;
        this.epic = epic;
        this.type = type;
    }

    public CSVImport(Integer id, Subtask subtask, Type type) {
        this.id = id;
        this.subtask = subtask;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public Task getTask() {
        return task;
    }

    public Epic getEpic() {
        return epic;
    }

    public Subtask getSubtask() {
        return subtask;
    }

    public Type getType() {
        return type;
    }
}
