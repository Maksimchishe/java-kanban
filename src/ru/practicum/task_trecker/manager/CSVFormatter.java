package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.*;

import java.time.LocalDateTime;

public class CSVFormatter {

    public static CSVImport fromString(String csvRow) {

        String[] split = csvRow.split(",");

        try {
            if (Type.valueOf(split[5]) == Type.TASK) {
                if (split[6].equals("null")) {
                    return new CSVImport(Integer.parseInt(split[0]), new Task(Integer.parseInt(split[0]), split[1], split[2], Status.valueOf(split[3]), null, Integer.parseInt(split[7])), Type.valueOf(split[5]));
                }
                return new CSVImport(Integer.parseInt(split[0]), new Task(Integer.parseInt(split[0]), split[1], split[2], Status.valueOf(split[3]), LocalDateTime.parse(split[6]), Integer.parseInt(split[7])), Type.valueOf(split[5]));
            } else if (Type.valueOf(split[5]) == Type.EPIC) {
                return new CSVImport(Integer.parseInt(split[0]), new Epic(Integer.parseInt(split[0]), split[1], split[2], Status.valueOf(split[3])), Type.valueOf(split[5]));
            } else if (Type.valueOf(split[5]) == Type.SUB) {
                if (split[6].equals("null")) {
                    return new CSVImport(Integer.parseInt(split[0]), new Subtask(Integer.parseInt(split[0]), Integer.parseInt(split[4]), split[1], split[2], Status.valueOf(split[3]), null, Integer.parseInt(split[7])), Type.valueOf(split[5]));
                }
                return new CSVImport(Integer.parseInt(split[0]), new Subtask(Integer.parseInt(split[0]), Integer.parseInt(split[4]), split[1], split[2], Status.valueOf(split[3]), LocalDateTime.parse(split[6]), Integer.parseInt(split[7])), Type.valueOf(split[5]));
            }
        } catch (EnumConstantNotPresentException e) {
            System.out.println(e.constantName());
        }
        return null;
    }

    public static String toString(Task task) {

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddThh:mm");

        return task.getId() + "," +
                task.getName() + "," +
                task.getDescription() + "," +
                task.getStatus().name() + "," +
                task.getIdEpic() + "," +
                task.getType().name() + "," +
                task.getStartTime() + "," +
                task.getDuration().toMinutes();
    }

    public static String getHeader() {
        return "id,name, description,status,epic,type,start,duration";
    }


}
