
package ru.kogtev.client;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class ChatWindow extends JFrame {

    private JTextArea chatArea;

    private JList<String> userList;

    private final Client client;
    private String serverAddress;
    private String username;

    public ChatWindow(Client client) {
        this.client = client;

        initializeServerAddress();

        initializeUsername();

        initialize();
    }

    private void initializeServerAddress() {
        serverAddress = JOptionPane.showInputDialog(this, "Enter server address:",
                "Connect settings", JOptionPane.QUESTION_MESSAGE);
        if (serverAddress == null) {
            System.exit(2);
        }
    }

    private void initializeUsername() {
        username = JOptionPane.showInputDialog(this, "Enter your name:",
                "Your name", JOptionPane.QUESTION_MESSAGE);
        if (username == null) {
            System.exit(2);
        }
    }


    private void initialize() {
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
                client.sendMessageToServer(message);
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
    }

    public void appendMessage(String message) {
        setTitle(username);
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
        listModel.clear();

        for (String user : users) {
            listModel.addElement(user);
        }

        this.userList.setModel(listModel);
    }
}

