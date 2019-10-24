import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TcpServerThread extends Thread {
    private Socket socket;

    /**
     * 初始化
     *
     * @param socket
     */
    public TcpServerThread(Socket socket) {
        System.out.println(socket);
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();

        InputStreamReader reader = null;
        BufferedReader bufReader = null;
        OutputStream os = null;
        try {
            reader = new InputStreamReader(socket.getInputStream());
            bufReader = new BufferedReader(reader);
            String s = null;
            StringBuffer sb = new StringBuffer();
            while ((s = bufReader.readLine()) != null) {
                sb.append(s);
            }
            System.out.println("服务器：" + sb.toString());
            // 关闭输入流
            socket.shutdownInput();

            // 返回给客户端数据
            os = socket.getOutputStream();
            os.write(("我是客户端, 服务端发给我的数据就是：" + sb.toString()).getBytes());
            os.flush();
            socket.shutdownOutput();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
