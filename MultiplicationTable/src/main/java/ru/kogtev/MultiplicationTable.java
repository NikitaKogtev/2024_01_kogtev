package ru.kogtev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiplicationTable {
    public static void main(String[] args) {


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Введите размер таблицы умножения (от 1 до 32), для завершения работы программы введите - End:");

            boolean isInputEnd = true;

            while (isInputEnd) {

                String input = reader.readLine();

                if (input.equals("End") || input.equals("end")) {
                    isInputEnd = false;
                    continue;
                }

                int size = Integer.parseInt(input);

                if (size >= 1 && size <= 32) {

                    Printer printer = new Printer(size);
                    printer.printMultiplicationTable();

                    System.out.println("Введите новый размер таблицы! Для завершения программы введите End");
                } else {
                    System.out.println("Вы ввели данные некорректно, значения должно быть от 1 до 32");
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода данных");
        } catch (NumberFormatException ex) {
            System.out.println("Введены некорректные данные, значение должно быть от 1 до 32");
        }
    }
}

