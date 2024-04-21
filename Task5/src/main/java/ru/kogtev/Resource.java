package ru.kogtev;

public class Resource {
    private static int nextId = 1;
    private final int id;

    public Resource() {
        this.id = nextId++;
    }

    public int getId() {
        return id;
    }
}
