package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;
    private final Map<Integer, Node> mapHistory = new HashMap<>();

    private Node linkLast(Task task) {
        final Node oldLast = last;
        final Node newNode = new Node(last, task, null);
        last = newNode;
        if (oldLast == null)
            first = newNode;
        else
            oldLast.next = newNode;

        return newNode;
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        mapHistory.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        Node node = mapHistory.remove(id);

        if (node == null) {
            return;
        }

        removeNode(node);
    }

    private void removeNode(Node node) {

        if (node.prev == null && node.next == null) {
            first = null;
            last = null;
        }

        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        if (node.prev == null && node.next != null) {
            node.next.prev = null;
            first = node.next;
        }

        if (node.next == null && node.prev != null) {
            node.prev.next = null;
            last = node.prev;
        }
    }

    @Override
    public List<Task> getHistory() {

        List<Task> nodeList = new ArrayList<>();
        Node nodes = first;

        while (nodes != null) {
            nodeList.add(nodes.value);
            nodes = nodes.next;
        }

        return nodeList;
    }

    private static class Node {
        Node prev;
        Node next;
        Task value;

        public Node(Node prev, Task value, Node next) {
            this.prev = prev;
            this.next = next;
            this.value = value;
        }

    }
}
