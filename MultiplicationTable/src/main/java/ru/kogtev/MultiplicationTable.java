package ru.kogtev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiplicationTable {
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {

            System.out.println("Введите размер таблицы умножения (от 1 до 32): ");

            while (true) {

                int size = Integer.parseInt(reader.readLine());

                if (size >= 1 && size <= 32) {

                    int minWidthValue = (size + "|").length();
                    int maxWidthValue = ((size * size) + "|").length();
                    String oneCellSplit = String.format("%" + minWidthValue + "s", "+").replace(" ", "-");

                    printHeader(size, minWidthValue, maxWidthValue);
                    printSplitLine(oneCellSplit, size, maxWidthValue);
                    printTable(size, minWidthValue, maxWidthValue, oneCellSplit);


                } else {
                    System.out.println("Вы ввели данные некорректно, значения должно быть от 1 до 32");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода данных");
        } catch (NumberFormatException ex) {
            System.out.println("Введен пустой размер таблицы, значение должно быть от 1 до 32 и не может быть пустым");
        }
    }

    private static void printHeader(int size, int minWidthValue, int maxWidthValue) {
        System.out.printf("%" + (minWidthValue - 1) + "s|", "");
        for (int i = 1; i <= size; i++) {
            System.out.printf("%" + (maxWidthValue - 1) + "s", i);
            if (i != size) {
                System.out.print("|");
            }
        }
        System.out.println();
    }

    private static void printSplitLine(String oneCellSplit, int size, int maxWidthValue) {
        System.out.print(oneCellSplit);
        out:
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j < maxWidthValue; j++) {
                System.out.print("-");
                if (j == (maxWidthValue - 1) && i == size) {
                    break out;
                }
            }
            System.out.print("+");
        }
        System.out.println();
    }

    private static void printTable(int size, int minWidthValue, int maxWidthValue, String oneCellSplit) {
        for (int i = 1; i <= size; i++) {
            System.out.printf("%" + (minWidthValue - 1) + "s|", i);
            for (int j = 1; j <= size; j++) {
                System.out.printf("%" + (maxWidthValue - 1) + "s", i * j);
                if (j != size) {
                    System.out.print("|");
                }
            }
            System.out.println();
            printSplitLine(oneCellSplit, size, maxWidthValue);
        }
    }
}

