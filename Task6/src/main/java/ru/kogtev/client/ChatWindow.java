
package ru.kogtev.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.function.Consumer;

public class ChatWindow extends JFrame {
    private static final Logger logger = LogManager.getLogger(ChatWindow.class);

    private JTextArea chatArea;

    private JList<String> userList;

    public String initializeServerAddress() {
        String serverAddress = JOptionPane.showInputDialog(this, "Enter server address:",
                "Connect settings", JOptionPane.QUESTION_MESSAGE);
        if (serverAddress == null) {
            logger.warn("Передали пустое значение сервера - exit");
            System.exit(2);
        }
        logger.info("Подключение к серверу с адресом - {}", serverAddress);
        return serverAddress;
    }

    public int initializePort() {
        String port = JOptionPane.showInputDialog(this, "Enter port:",
                "Port settings", JOptionPane.QUESTION_MESSAGE);
        if (port == null) {
            logger.warn("Передали пустое значение порта - exit");
            System.exit(2);
        }
        logger.info("Подключение к серверу с портом - {}", port);
        return Integer.parseInt(port);
    }

    public String initializeUsername() {
        String username = JOptionPane.showInputDialog(this, "Enter your name:",
                "Your name", JOptionPane.QUESTION_MESSAGE);
        if (username == null) {
            logger.warn("Передали пустое значение имени - exit");
            System.exit(2);
        }
        logger.info("Подключение к серверу с именем - {}", username);
        return username;
    }


    public void initializeChat(Consumer<String> messageSender, String username) {
        JTextField messageField;
        JButton sendButton;
        JFrame frame;

        frame = new JFrame();
        frame.setTitle("Chat - " + username);

        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

        sendButton.addActionListener(e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                messageSender.accept(message);
                messageField.setText("");
            }
        });

        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.getRootPane().setDefaultButton(sendButton);

        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BorderLayout());
        userListPanel.setPreferredSize(new Dimension(150, 0));
        frame.getContentPane().add(userListPanel, BorderLayout.EAST);

        userList = new JList<>();

        JScrollPane userListScrollPane = new JScrollPane(userList);
        userListPanel.add(userListScrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
        logger.info("Чат инициализирован");
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
        logger.info("Сообщение отправлено в чат");
    }


    public void updateUserList(Set<String> users) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.clear();

        for (String user : users) {
            listModel.addElement(user);
        }
        userList.setModel(listModel);
        logger.info("Обновлен список подключенных пользователей");
    }
}

