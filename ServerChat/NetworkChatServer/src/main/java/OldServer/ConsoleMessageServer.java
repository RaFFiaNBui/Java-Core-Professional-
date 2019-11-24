package OldServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleMessageServer {

    private static final int PORT = 8189;
    private static DataInputStream input;
    private static DataOutputStream output;
    private static Socket socket;
    private Thread consoleMessageServerThread;

    public static void main(String[] args) {

        new ConsoleMessageServer().runServer();
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(ConsoleMessageServer.PORT)) {
            System.out.println("Сервер запущен");
            waitClient(serverSocket);
            startConsoleThread();
            while (true) {
                String str = input.readUTF();
                if (str.equals("/end")) {
                    shutDownServer(serverSocket);
                    break;
                }
                System.out.println("Сообщение от клиента: " +  str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitClient(ServerSocket serverSocket) throws IOException {
        System.out.println("Ожидание подключения клиента...");
        socket = serverSocket.accept();
        System.out.println("Клиент подключен!");
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

    private void startConsoleThread() {
        consoleMessageServerThread = new Thread(() -> {
            BufferedReader clientInput = new BufferedReader(new InputStreamReader((System.in)));
            System.out.println("Введите сообщение клиенту: ");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (clientInput.ready()) {
                        String messageFromConsoleServer = clientInput.readLine();
                        output.writeUTF(messageFromConsoleServer);
                    }
                    Thread.sleep(500);
                } catch (InterruptedException | IOException e) {
                    break;
                }
            }
            System.out.println("Консольный поток закрыт!");
        });
        consoleMessageServerThread.start();
    }

    private void shutDownServer(ServerSocket serverSocket) throws IOException {
        consoleMessageServerThread.interrupt();
        socket.close();
        serverSocket.close();
        System.out.println("Порт закрыт. Сервер остановлен!");
    }
}
