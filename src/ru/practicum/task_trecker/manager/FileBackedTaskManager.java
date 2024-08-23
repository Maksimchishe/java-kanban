package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final static String PATH_TO_FILE = "src/path/file.csv";
    private final static Path path = Path.of(PATH_TO_FILE);

    @Override
    public void loadFromFile() {

        try {

            if (!Files.isDirectory(path)) {
                File newFolder = new File("src/path/");
                if (newFolder.mkdir()) {
                    System.out.println("Создана директория.");
                }
            }

            if (!Files.exists(path)) {
                Files.createFile(path);
            }


            List<String> lines = Files.readAllLines(path);

            int maxId = 0;

            for (int i = 1; i < lines.size(); i++) {
                CSVImport csvImport = CSVFormatter.fromString(lines.get(i));

                if (csvImport != null) {
                    if (csvImport.getType() == Type.TASK) {
                        super.tasks.put(csvImport.getId(), csvImport.getTask());
                    } else if (csvImport.getType() == Type.EPIC) {
                        super.epics.put(csvImport.getId(), csvImport.getEpic());
                    } else if (csvImport.getType() == Type.SUB) {
                        super.subTasks.put(csvImport.getId(), csvImport.getSubtask());
                    }
                    if (csvImport.getId() > maxId) {
                        maxId = csvImport.getId();
                    }
                } else {
                    System.out.println("Line = null!!!");
                    break;
                }
            }

            super.setId(maxId);

        } catch (IOException e) {
            throw ManagerSaveException.managerLoadException(e);
        }
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_TO_FILE))) {
            //header
            bw.write(CSVFormatter.getHeader());
            bw.newLine();

            for (Task task : super.tasks.values()) {
                bw.write(CSVFormatter.toString(task));
                bw.newLine();
            }

            for (Epic epic : super.epics.values()) {
                bw.write(CSVFormatter.toString(epic));
                bw.newLine();
            }

            for (Subtask subtask : super.subTasks.values()) {
                bw.write(CSVFormatter.toString(subtask));
                bw.newLine();
            }

        } catch (Exception e) {
            throw ManagerSaveException.managerSaveException(e);
        }
    }

    @Override
    public Task createTask(Task task) {
        Task createTask = super.createTask(task);
        save();
        return createTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createEpic = super.createEpic(epic);
        save();
        return createEpic;
    }

    @Override
    public Subtask createSubTask(Subtask subTask) {
        Subtask createSubTask = super.createSubTask(subTask);
        save();
        return createSubTask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updateTask = super.updateTask(task);
        save();
        return updateTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updateEpic = super.updateEpic(epic);
        save();
        return updateEpic;
    }

    @Override
    public Subtask updateSubTask(Subtask subTask) {
        Subtask updateSubTask = super.updateSubTask(subTask);
        save();
        return updateSubTask;
    }

    @Override
    public boolean deleteTaskById(Integer id) {
        boolean del = super.deleteTaskById(id);
        save();
        return del;
    }

    @Override
    public boolean deleteEpicById(Integer id) {
        boolean del = super.deleteEpicById(id);
        save();
        return del;
    }

    @Override
    public boolean deleteSubTaskById(Integer id) {
        boolean del = super.deleteSubTaskById(id);
        save();
        return del;
    }

    @Override
    public void delAllTask() {
        super.delAllTask();
        save();
    }

    @Override
    public void delAllEpic() {
        super.delAllEpic();
        save();
    }

    @Override
    public void deleteAllSubTasksByEpicId(Integer id) {
        super.deleteAllSubTasksByEpicId(id);
        save();
    }

    @Override
    public void delAllSubTask() {
        super.delAllSubTask();
        save();
    }

}
