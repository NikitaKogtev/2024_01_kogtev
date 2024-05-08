package ru.kogtev.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientMain {
    private static final String ADDRESS = "localhost";
    public static final int PORT = 8899;

    private final String username;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    private PrintWriter printWriter; // Поток вывода для отправки сообщений на сервер


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClientMain window = new ClientMain();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ClientMain() {
        this.username = JOptionPane.showInputDialog(frame, "Enter your username:");

        initializeUI();
        connectToServer();
    }

    // Метод для инициализации пользовательского интерфейса
    private void initializeUI() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Запрос имени пользователя

        frame.setTitle("Chat - " + username);
    }

    private void connectToServer() {
        try {

            Socket socket = new Socket(ADDRESS, PORT); // Создаем сокет и подключаемся к серверу
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            printWriter = new PrintWriter(outputStreamWriter, true); // Создаем поток вывода для отправки сообщений на сервер

            new Thread(new ServerListener(socket)).start(); // Запускаем поток для приема сообщений от сервера

            printWriter.println(username + " has joined the chat"); // Отправляем на сервер сообщение о присоединении к чату

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            printWriter.println(message); // Отправляем сообщение на сервер
            messageField.setText(""); // Очищаем поле ввода
        }
    }

    // Внутренний класс для приема сообщений от сервера
    private class ServerListener implements Runnable {
        private Socket socket;
        private BufferedReader bufferedReader; // Поток ввода для чтения сообщений от сервера

        public ServerListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStreamReader inputStreamReader =
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);

                bufferedReader = new BufferedReader(inputStreamReader); // Создаем поток ввода для чтения сообщений от сервера
                String message;
                // Читаем сообщения от сервера и выводим их в окно чата
                while ((message = bufferedReader.readLine()) != null) {
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close(); // Закрываем сокет при отключении от сервера
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
