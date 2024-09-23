package ru.practicum.task_trecker.task;

public class Task {
    private Integer id;
    private final String name;
    private final String description;
    private Status status;

    public Type getType() {
        return Type.TASK;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Integer getIdEpic() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 1;
        if (id != null) {
            hash = hash + id.hashCode();
        }
        hash = hash * 3;

        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 3;

        if (description != null) {
            hash = hash + description.hashCode();
        }
        hash = hash * 3;

        if (status != null) {
            hash = hash + status.hashCode();
        }

        return hash;
    }

}
