package ru.practicum.task_trecker.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private Integer id;
    private final String name;
    private final String description;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

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

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Integer id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTimeTask() {
        return startTime.plus(duration);
    }

    public LocalDateTime getEndTimeEpic() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Type getType() {
        return Type.TASK;
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

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Task{id='%d', name='%s', description='%s', status='%s', startTime='%s', duration='%s'}".formatted(id, name, description, status, startTime, duration);
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
