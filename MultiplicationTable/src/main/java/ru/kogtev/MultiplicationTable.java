package ru.kogtev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

                    System.out.print(String.format("%" + (minWidthValue - 1) + "s|", ""));
                    for (int i = 1; i <= size; i++) {

                        if (i != size) {
                            System.out.print(String.format("%" + (maxWidthValue - 1) + "s|", i));
                        } else {
                            System.out.print(String.format("%" + (maxWidthValue - 1) + "s", i));
                        }
                    }
                    System.out.println();

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

                    for (int i = 1; i <= size; i++) {
                        System.out.print(String.format("%" + (minWidthValue - 1) + "s|", i));
                        for (int j = 1; j <= size; j++) {
                            if (j != size) {
                                System.out.print(String.format("%" + (maxWidthValue - 1) + "s|", i * j));
                            } else {
                                System.out.print(String.format("%" + (maxWidthValue - 1) + "s", i * j));
                            }

                        }
                        System.out.println();

                        System.out.print(oneCellSplit);

                        out:
                        for (int k = 1; k <= size; k++) {
                            for (int j = 1; j < maxWidthValue; j++) {
                                System.out.print("-");
                                if (j == (maxWidthValue - 1) && k == size) {
                                    break out;
                                }
                            }
                            System.out.print("+");
                        }
                        System.out.println();
                    }


                } else {
                    System.out.println("Вы ввели данные некорректно, значения должно быть от 1 до 32");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

