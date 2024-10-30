package ru.practicum.task_trecker.exception;

public class NotFoundException extends Throwable {
    public NotFoundException(String msg) {
        super(msg);
    }

    public static NotFoundException notAcceptable() {
        return new NotFoundException("Not Acceptable");
    }

    public static NotFoundException notFound() {
        return new NotFoundException("Not Found");
    }

    public static NotFoundException objectNull() {
        return new NotFoundException("object Null");
    }

    public static NotFoundException NotContainsKey() {
        return new NotFoundException("Not Contains Key");
    }
}
