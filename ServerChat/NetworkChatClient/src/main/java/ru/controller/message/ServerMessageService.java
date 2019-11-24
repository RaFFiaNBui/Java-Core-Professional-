package ru.controller.message;

import common.*;
import common.Message;
import javafx.scene.control.TextArea;
import ru.controller.Controller;
import ru.controller.Network;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ServerMessageService implements IMessageService {

    private static final String HOST_ADDRESS_PROP = "server.address";
    private static final String HOST_PORT_PROP = "server.port";

    private String hostAddress;
    private int hostPort;

    private final TextArea chatTextArea;
    private Network network;
    private Controller controller;
    private boolean needStopServerOnClosed;

    public ServerMessageService(Controller controller, boolean needStopServerOnClosed) {
        this.chatTextArea = controller.chatTextArea;
        this.controller = controller;
        this.needStopServerOnClosed = needStopServerOnClosed;
        initialize();
    }

    private void initialize() {
        readProperties();
        startConnectionToServer();
    }

    private void startConnectionToServer() {
        try {
            this.network = new Network(hostAddress, hostPort, this);
        } catch (IOException e) {
            throw new ServerConnectionException("Ошибка соединения с сервером.", e);
        }
    }

    private void readProperties() {
        Properties serverProperties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/app.properties")) {
            serverProperties.load(inputStream);
            hostAddress = serverProperties.getProperty(HOST_ADDRESS_PROP);
            hostPort = Integer.parseInt(serverProperties.getProperty(HOST_PORT_PROP));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла app.properties", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка номера порта", e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        network.send(message.toJson());
    }

    @Override
    public void processRetrievedMessage(Message message) {
        switch (message.command) {
            case AUTH_OK:
                processAuthOk(message);
                break;
            case PRIVATE_MESSAGE: {
                processPrivateMessage(message);
                break;
            }
            case PUBLIC_MESSAGE: {
                processPublicMessage(message);
                break;
            }
            case AUTH_ERROR: {
                controller.showAuthError(message.authErrorMessage.errorMsg);
                break;
            }
            case CLIENT_LIST:
                List<String> onlineUserNicknames = message.clientListMessage.online;
                controller.refreshUsersList(onlineUserNicknames);
                break;
            default:
                throw new IllegalArgumentException("Unknown command type: " + message.command);
        }
    }
    private void processPublicMessage(Message message) {
        PublicMessage publicMessage = message.publicMessage;
        String from = publicMessage.from;
        String msg = publicMessage.message;
        if (from != null) {
            chatTextArea.appendText(String.format("%s: %s%n", from, msg));
        } else {
            chatTextArea.appendText(String.format("%s%n", msg));
        }
    }

    private void processPrivateMessage(Message message) {
        PrivateMessage privateMessage = message.privateMessage;
        String from = privateMessage.from;
        String msg = privateMessage.message;
        String msgToView = String.format("%s (private): %s%n", from, msg);
        chatTextArea.appendText(msgToView);
    }

    private void processAuthOk(Message message) {
        controller.setNickName(message.authOkMessage.nickname);
        controller.showChatPanel();
    }

    @Override
    public void close() throws IOException {
        if (needStopServerOnClosed) {
            sendMessage(Message.serverEndMessage());
        }
        network.close();
    }
}
