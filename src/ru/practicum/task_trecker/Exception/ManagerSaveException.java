package ru.practicum.task_trecker.Exception;

public class ManagerSaveException extends RuntimeException {

    private static final String MSG_SAVE = "Error occurred while saving";
    private static final String MSG_LOAD = "Error occurred while load";

    public static ManagerSaveException managerSaveException(Exception e) {
        return new ManagerSaveException(MSG_SAVE, e);
    }

    public static ManagerSaveException managerLoadException(Exception e) {
        return new ManagerSaveException(MSG_LOAD, e);
    }

    public ManagerSaveException(String msg, Exception e) {
        super(msg, e);
    }

}
