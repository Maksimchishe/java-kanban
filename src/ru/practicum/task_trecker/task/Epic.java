package ru.practicum.task_trecker.task;

public class Epic extends Task {

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{id='%d', name='%s', description='%s', status='%s', startTime='%s', endTime='%s', duration='%s'}".formatted(this.getId(), this.getName(), this.getDescription(), this.getStatus(), super.getStartTime(), super.getEndTimeEpic(), super.getDuration());
    }
}
