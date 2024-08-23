package ru.practicum.task_trecker.task;

public class Subtask extends Task {

    private final Integer idEpic;

    public Subtask(Integer idEpic, String name, String description, Status status) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    public Subtask(Integer id, Integer idEpic, String name, String description, Status status) {
        super(id, name, description, status);
        this.idEpic = idEpic;
    }

    @Override
    public Integer getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id='" + this.getId() + '\'' +
                ", idEpic='" + this.getIdEpic() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus() + '\'' + "}";
    }

    @Override
    public Type getType() {
        return Type.SUB;
    }

}

