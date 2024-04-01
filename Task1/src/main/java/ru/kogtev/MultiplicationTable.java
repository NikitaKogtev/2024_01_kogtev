package ru.kogtev;

public class MultiplicationTable {
    private final int size;


    public MultiplicationTable(int size) {
        this.size = size;
    }

    public void printMultiplicationTable() {
        int minWidthValue = (size + "|").length();
        int maxWidthValue = ((size * size) + "|").length();
        int minSplitLineWidthValue = minWidthValue - 1;
        int maxSplitLineWidthValue = maxWidthValue - 1;

        String oneCellSplit = String.format("%" + minWidthValue + "s", "+").replace(" ", "-");
        String splitLine = String.format(createSplitLine(oneCellSplit, size, maxWidthValue, maxSplitLineWidthValue));

        printHeader(size, minSplitLineWidthValue, maxSplitLineWidthValue, splitLine);
        printTable(size, minSplitLineWidthValue, maxSplitLineWidthValue, splitLine);
    }

    private String createSplitLine(String oneCellSplit, int size, int maxWidthValue, int maxSplitLineWidthValue) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(oneCellSplit);

        out:
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j < maxWidthValue; j++) {
                stringBuilder.append("-");
                if (j == (maxSplitLineWidthValue) && i == size) {
                    break out;
                }
            }
            stringBuilder.append("+");
        }
        return stringBuilder.toString();
    }

    private void printHeader(int size, int minSplitLineWidthValue, int maxSplitLineWidthValue, String splitLine) {
        System.out.printf("%" + (minSplitLineWidthValue) + "s|", "");
        for (int i = 1; i <= size; i++) {
            if (i == size) {
                System.out.printf("%" + (maxSplitLineWidthValue) + "s", i);
            } else {
                System.out.printf("%" + (maxSplitLineWidthValue) + "s|", i);
            }
        }
        System.out.println();
        System.out.println(splitLine);
    }

    private void printTable(int size, int minSplitLineWidthValue, int maxSplitLineWidthValue, String splitLine) {
        for (int i = 1; i <= size; i++) {
            System.out.printf("%" + (minSplitLineWidthValue) + "s|", i);
            for (int j = 1; j <= size; j++) {
                if (j == size) {
                    System.out.printf("%" + (maxSplitLineWidthValue) + "s", i * j);
                } else {
                    System.out.printf("%" + (maxSplitLineWidthValue) + "s|", i * j);
                }
            }
            System.out.println();
            System.out.println(splitLine);
        }
    }
}
