import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String[] args) {
        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(23333);
            while (true) {
                System.out.println("Server开始~~~监听~~~");
                // accept方法会阻塞，直到有客户端与之建立连接
                Socket socket = serverSocket.accept();
                System.out.println("发现连接！\n" + socket.getInetAddress() + "\n");
                TcpServerThread serverThread = new TcpServerThread(socket);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
