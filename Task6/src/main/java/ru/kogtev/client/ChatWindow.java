
package ru.kogtev.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class ChatWindow extends JFrame {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JList<String> userList;

    private String serverAddress;
    private String username;

    public ChatWindow() {
        initializeServerAddress();

        initializeUsername();

        initialize();
    }

    private void initializeServerAddress() {
        serverAddress = JOptionPane.showInputDialog(this, "Enter server address:",
                "Connect settings", JOptionPane.QUESTION_MESSAGE);
        if (serverAddress == null) {
            System.exit(2); // Если пользователь нажал отмену, закрываем приложение
        }
    }

    private void initializeUsername() {
        username = JOptionPane.showInputDialog(this, "Enter your name:",
                "Your name", JOptionPane.QUESTION_MESSAGE);
        if (username == null) {
            System.exit(2); // Если пользователь нажал отмену, закрываем приложение
        }
    }


    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Chat - " + username);

        frame.setBounds(100, 100, 600, 400);
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
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    ClientMain.sendMessage(message);
                    messageField.setText("");
                }
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
    }

    public void display() {
        frame.setVisible(true);
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getUsername() {
        return username;
    }

    public void updateUserList(Set<String> users) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        // Очистить текущий список пользователей
        listModel.clear();

        // Добавить всех пользователей из множества
        for (String user : users) {
            listModel.addElement(user);
        }

        this.userList.setModel(listModel);
    }

//    public void updateUserList(Set<String> userList) {
//        DefaultListModel<String> listModel = new DefaultListModel<>();
//        for (String user : userList) {
//            listModel.addElement(user);
//        }
//        this.userList.setModel(listModel);
//    }

}

