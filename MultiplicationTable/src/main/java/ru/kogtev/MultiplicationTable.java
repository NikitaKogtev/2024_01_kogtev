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

                    int minWidthValue = String.valueOf(size).length();
                    int maxWidthValue = String.valueOf(size * size).length();


                    for (int i = 1; i <= size; i++) {
                        for (int j = 1; j <= size; j++) {
                            System.out.print(i * j);
                            System.out.print(" ");
                        }
                        System.out.println(" ");
                    }


                } else {
                    System.out.println("Вы ввели данные некорректно, значения должно быть от 1 до 32");
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}