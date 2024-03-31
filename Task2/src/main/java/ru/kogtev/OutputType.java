package ru.kogtev;

public enum OutputType {
    CONSOLE("console"),
    FILE("file");

    public final String outputType;

    OutputType (String outputType) {
        this.outputType = outputType;
    }

}
