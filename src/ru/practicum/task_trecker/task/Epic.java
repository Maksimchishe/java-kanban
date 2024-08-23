package ru.practicum.task_trecker.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    List<Integer> listIdSubTasks = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                '}';
    }

    public void addListIdSubTask(List<Integer> listIdSubTasks) {
        this.listIdSubTasks.clear();
        this.listIdSubTasks.addAll(listIdSubTasks);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }
}
