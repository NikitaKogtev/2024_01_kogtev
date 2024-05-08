package ru.kogtev.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerMain {
    public static final int PORT = 8899;

    private static final Set<String> usernames = new HashSet<>(); // Множество для хранения имен пользователей
    private static final List<PrintWriter> clients = new ArrayList<>(); // Список для хранения идентификаторов (Writer'ов) подключенных клиентов

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Принимаем входящее подключение
                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(clientSocket); // Создаем обработчик клиента
                new Thread(clientHandler).start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Внутренний класс для обработки подключенного клиента
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private String username;
        PrintWriter printWriter;
        BufferedReader bufferedReader;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (OutputStreamWriter outputStreamWriter =
                         new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
                 InputStreamReader inputStreamReader =
                         new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)) {

                printWriter = new PrintWriter(outputStreamWriter, true);  // Создаем поток вывода для отправки сообщений клиенту
                bufferedReader = new BufferedReader(inputStreamReader);   // Создаем поток ввода для чтения сообщений от клиента

                printWriter.println("Enter your username: ");
                username = bufferedReader.readLine();

                // Проверяем, что имя не занято другим клиентом
                synchronized (usernames) {
                    while (usernames.contains(username)) {
                        printWriter.println("This username is already taken. Please enter a different username: ");
                        username = bufferedReader.readLine();
                    }

                    usernames.add(username); // Добавляем имя клиента в множество имен
                }

                printWriter.println("Welcome to the chat, " + username + "!");
                broadcast(username + " has joined the chat"); // Рассылаем всем клиентам сообщение о присоединении нового участника

                clients.add(printWriter); // Добавляем поток вывода клиента в список активных клиентов

                String inputLine;
                // Читаем сообщения от клиента и рассылаем их всем остальным клиентам
                while ((inputLine = bufferedReader.readLine()) != null) {
                    broadcast(username + ": " + inputLine);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // При отключении клиента удаляем его имя из множества имен, закрываем потоки и уведомляем остальных клиентов
                if (username != null) {
                    usernames.remove(username);
                    broadcast(username + " has left the chat");
                }
                if (printWriter != null) {
                    clients.remove(printWriter);
                }
                try {
                    clientSocket.close(); // Закрываем сокет клиента
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        // Метод для рассылки сообщения всем подключенным клиентам
        private void broadcast(String message) {
            synchronized (clients) {
                for (PrintWriter client : clients) {
                    client.println(message);
                }
            }
        }
    }
}

//        new Thread(() -> {
//            Random rnd = new Random();
//
//            try {
//                while (true) {
//                    List<Socket> deadClients = new ArrayList<>();
//
//                    for (Socket client : clients) {
//                        try {
//                            OutputStreamWriter writer = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
//                            PrintWriter printWriter = new PrintWriter(writer, true);
//                            printWriter.println("OK - " + rnd.nextInt());
//                        } catch (Exception e) {
//                            System.out.println("Client disconnected");
//                            deadClients.add(client);
//                        }
//                    }
//
//                    clients.removeAll(deadClients);
//
//                    Thread.sleep(1000);
//                }
//            } catch (Exception e) {
//            }
//        }).start();
//
//        while (true) {
//            Socket socket = serverSocket.accept();
//            System.out.println("Client connected");
//            clients.add(socket);
//        }