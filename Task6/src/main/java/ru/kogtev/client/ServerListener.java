package ru.kogtev.client;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.kogtev.common.ChatMessage;
import ru.kogtev.common.Message;
import ru.kogtev.common.MessageType;
import ru.kogtev.common.UserListMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerListener implements Runnable {
    private static final Logger logger = LogManager.getLogger(ServerListener.class);

    private static final String SEPARATOR = ": ";

    private final Socket socket;
    private final Client client;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServerListener(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        try (InputStreamReader inputStreamReader =
                     new InputStreamReader(socket.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String jsonMessage;

            while ((jsonMessage = reader.readLine()) != null) {
                processJsonMessage(jsonMessage);
            }

        } catch (IOException e) {
            logger.warn("Сокет не был получен {}", e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.warn("Сокет получилось закрыть {}", e.getMessage());
            }
        }
    }

    private void processJsonMessage(String jsonMessage) {
        try {
            Message message = objectMapper.readValue(jsonMessage, Message.class);
            String messageType = message.getClass().getAnnotation(JsonTypeName.class).value();

            switch (MessageType.valueOf(messageType)) {
                case MESSAGE:
                    handleMessage((ChatMessage) message);
                    break;
                case USER_LIST:
                    handleUserList((UserListMessage) message);
                    break;
                default:
                    logger.warn("Неизвестный тип сообщения - {}", messageType);
            }
        } catch (IOException e) {
            logger.warn("Сообщение не может быть преобразовано - {}", e.getMessage());
        }
    }

    private void handleMessage(ChatMessage chatMessage) {
        client.appendMessageToChat(chatMessage.getSender() + SEPARATOR + chatMessage.getContent());
    }

    private void handleUserList(UserListMessage userListMessage) {
        client.updateUserList(userListMessage.getUsers());
    }
}
