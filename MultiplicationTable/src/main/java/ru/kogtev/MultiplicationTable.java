package ru.kogtev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiplicationTable {
    public static void main(String[] args) {

        int size = 10;

        int minWidthValue = (size + "|").length();
        int maxWidthValue = ((size * size) + "|").length();


        // Выводим шапку таблицы
        System.out.print(String.format("%" + (minWidthValue - 1) + "s|", ""));
        for (int i = 1; i <= size; i++) {
            System.out.print(String.format("%" + (maxWidthValue - 1) + "d|", i));
        }
        System.out.println();

        // Выводим разделитель
        System.out.print(String.format("%" + minWidthValue + "s", "+").replace(" ", "-"));
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j < maxWidthValue; j++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();

        //Выводим таблицу
        for (int i = 1; i <= size; i++) {
            System.out.print(String.format("%" + (minWidthValue - 1) + "d|", i));
            for (int j = 1; j <= size; j++) {
                System.out.print(String.format("%" + (maxWidthValue - 1) + "d|", i * j));
            }
            System.out.println();
            System.out.print(String.format("%" + minWidthValue + "s", "+").replace(" ", "-"));
            for (int k = 1; k <= size; k++) {
                for (int j = 1; j < maxWidthValue; j++) {
                    System.out.print("-");
                }
                System.out.print("+");
            }
            System.out.println();
        }

    }
}