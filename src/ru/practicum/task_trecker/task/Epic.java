package ru.practicum.task_trecker.task;

public class Epic extends Task {

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
                ", name='" + this.getDescription() + '\'' +
                ", description='" + this.getName() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                '}';
    }

}
