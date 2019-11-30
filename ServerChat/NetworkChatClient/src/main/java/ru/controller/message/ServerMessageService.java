package ru.controller.message;

import common.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import ru.controller.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.*;

public class ServerMessageService implements IMessageService {

    private static final String HOST_ADDRESS_PROP = "server.address";
    private static final String HOST_PORT_PROP = "server.port";

    private String hostAddress;
    private int hostPort;

    private final TextArea chatTextArea;
    private Network network;
    private Controller controller;
    private boolean needStopServerOnClosed;

    private String nick;

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
        String msgNoNull = String.format("%s: %s%n", from, msg);
        String msgNull = String.format("%s%n", msg);
        if (from != null) {
            chatTextArea.appendText(msgNoNull);
            writeLog(msgNoNull);
        } else {
            chatTextArea.appendText(msgNull);
            writeLog(msgNull);
        }
    }

    private void processPrivateMessage(Message message) {
        PrivateMessage privateMessage = message.privateMessage;
        String from = privateMessage.from;
        String msg = privateMessage.message;
        String msgToView = String.format("%s (private): %s%n", from, msg);
        chatTextArea.appendText(msgToView);
        writeLog(msgToView);
    }

    private void writeLog(String logMsg) {
        Controller.writeLog(logMsg, nick);
    }

    private void processAuthOk(Message message) {
        controller.setNickName(message.authOkMessage.nickname);
        controller.showChatPanel();
        this.nick = message.authOkMessage.nickname;
        readLog();
    }

    private void readLog() {
        //создаем лог файл, если он еще не создан
        File log = new File("src/main/java/ru/controller/message/history/history_" + nick + ".txt");
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                showAlert();
            }
            System.out.println("Create history file for user " + nick);
        }
        //подтягиваем в окно чата последние 100 строк из лога
        try (FileInputStream in = new FileInputStream(log);
             InputStreamReader inStR = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(inStR)) {
            //сохранем последнии 100 строк в коллекцию
            List<String> history = new ArrayList<>();
            for (String o; (o = br.readLine()) != null; ) {
                if (history.add(o) && history.size() > 100) {
                    history.remove(0);
                }
            }
            //выводим коллекцию на экран
            for (String s : history) {
                chatTextArea.appendText(s);
                chatTextArea.appendText(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Reading " + nick + "'s user history file is successfully!");
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Файл истории сообщений не создан!");
        alert.setHeaderText(null);
        alert.setContentText("Возможно не хватает прав на создание файла");
        alert.showAndWait();
    }

    @Override
    public void close() throws IOException {
        if (needStopServerOnClosed) {
            sendMessage(Message.serverEndMessage());
        }
        network.close();
    }
}
