package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.exception.ManagerSaveException;
import ru.practicum.task_trecker.exception.NotFoundException;
import ru.practicum.task_trecker.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public FileBackedTaskManager() throws NotFoundException {
        loadFromFile();
    }

    private final String file = "src/path/file.csv";
    private final Path path = Path.of(file);

    @Override
    public void loadFromFile() throws NotFoundException {

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

            super.epics.values().forEach(e -> {
                try {
                    super.updateSubTaskToEpic(e.getId());
                } catch (NotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });

        } catch (IOException e) {
            throw ManagerSaveException.managerLoadException(e);
        }

    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
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
    public Task createTask(Task task) throws NotFoundException {
        Task createTask = super.createTask(task);
        save();
        return createTask;
    }

    @Override
    public Epic createEpic(Epic epic) throws NotFoundException {
        Epic createEpic = super.createEpic(epic);
        save();
        return createEpic;
    }

    @Override
    public Subtask createSubTask(Subtask subTask) throws NotFoundException {
        Subtask createSubTask = super.createSubTask(subTask);
        save();
        return createSubTask;
    }

    @Override
    public Task updateTask(Task task) throws NotFoundException {
        Task updateTask = super.updateTask(task);
        save();
        return updateTask;
    }

    @Override
    public Epic updateEpic(Epic epic) throws NotFoundException {
        Epic updateEpic = super.updateEpic(epic);
        save();
        return updateEpic;
    }

    @Override
    public Subtask updateSubTask(Subtask subTask) throws NotFoundException {
        Subtask updateSubTask = super.updateSubTask(subTask);
        save();
        return updateSubTask;
    }

    @Override
    public boolean deleteTaskById(Integer id) throws NotFoundException {
        boolean del = super.deleteTaskById(id);
        save();
        return del;
    }

    @Override
    public boolean deleteEpicById(Integer id) throws NotFoundException {
        boolean del = super.deleteEpicById(id);
        save();
        return del;
    }

    @Override
    public boolean deleteSubTaskById(Integer id) throws NotFoundException {
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
    public void deleteAllSubTasksByEpicId(Integer id) throws NotFoundException {
        super.deleteAllSubTasksByEpicId(id);
        save();
    }

    @Override
    public void delAllSubTask() {
        super.delAllSubTask();
        save();
    }
}
