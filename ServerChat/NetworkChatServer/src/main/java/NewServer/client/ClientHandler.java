package NewServer.client;

import NewServer.MyServer;
import common.AuthMessage;
import common.Command;
import common.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {

    public static final int TIMEOUT = 120 * 1000;
    private MyServer myServer;
    private String clientName;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, MyServer myServer) {
        try {
            this.socket = socket;
            this.myServer = myServer;
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());

            Thread thread = new Thread(() -> {
                try {
                    authentication();
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create client handler", e);
        }
    }

    private void readMessages() throws IOException {
        while (true) {
            String clientMessage = input.readUTF();
            System.out.printf("Message '%s' from client %s%n", clientMessage, clientName);
            Message m = Message.fromJson(clientMessage);
            switch (m.command) {
                case PUBLIC_MESSAGE:
                    myServer.broadcastMessage(m, this);
                    break;
                case PRIVATE_MESSAGE:
                    myServer.sendPrivateMessage(m);
                    break;
                case END:
                    return;
            }
        }
    }

    private void closeConnection() {
        myServer.unsubscribe(this);
        myServer.broadcastMessage(Message.createPublic(null, clientName + " is offline"));
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close socket!");
            e.printStackTrace();
        }
    }

    // ожидаем строку типа    "/auth login password"
    private void authentication() throws IOException {
        Timer timeout = new Timer(true);
        timeout.schedule(new TimerTask() {
                @Override
                public void run() {
            try {
                synchronized (this) {
                    if (clientName == null) {
                        System.out.println("Authentication timed out!");
                        sendMessage(Message.createAuthError("Время ожидания подключения истекло!"));
                        Thread.sleep(100);
                        socket.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                }
        }, TIMEOUT);

        while (true) {
            String clientMessage = input.readUTF();
            synchronized (this) {
                Message message = Message.fromJson(clientMessage);
                if (message.command == Command.AUTH_MESSAGE) {
                    AuthMessage authMessage = message.authMessage;
                    String login = authMessage.login;
                    String password = authMessage.password;
                    String nick = myServer.getAuthService().getNickByLoginPass(login, password);
                    if (nick == null) {
                        sendMessage(Message.createAuthError("Неверные логин/пароль"));
                        continue;
                    }
                    if (myServer.isNickBusy(nick)) {
                        sendMessage(Message.createAuthError("Учетная запись уже используется"));
                        continue;
                    }
                    clientName = nick;
                    sendMessage(Message.createAuthOk(clientName));
                    myServer.broadcastMessage(Message.createPublic(null, clientName + " is online"));
                    myServer.subscribe(this);
                    break;
                }
            }
        }
    }

    private void sendMessage (Message message) {
        sendMessage(message.toJson());
    }

    public void sendMessage(String message)  {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            System.err.println("Failed to send message to user " + clientName + " : " + message);
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return clientName;
    }
}
