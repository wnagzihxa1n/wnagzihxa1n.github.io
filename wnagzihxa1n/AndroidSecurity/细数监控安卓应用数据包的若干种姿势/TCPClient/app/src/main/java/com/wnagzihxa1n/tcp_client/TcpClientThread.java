package com.wnagzihxa1n.tcp_client;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClientThread extends Thread {

    private String address;
    private int port;
    private String msg;
    private Handler mHandler;

    public TcpClientThread(Handler handler, String address, int port, String msg) {
        this.mHandler = handler;
        this.address = address;
        this.port = port;
        this.msg = msg;
    }

    @Override
    public void run() {
        super.run();
        sendSocket();
    }

    private void sendSocket() {
        InputStreamReader reader = null;
        BufferedReader bufReader = null;
        Socket socket = null;
        try {
            socket = new Socket(address, port);
            OutputStream os = socket.getOutputStream();
            os.write(msg.getBytes());
            os.flush();
            socket.shutdownOutput();
            InputStream is = socket.getInputStream();
            reader = new InputStreamReader(is);
            bufReader = new BufferedReader(reader);
            String s = null;
            final StringBuffer sb = new StringBuffer();
            while ((s = bufReader.readLine()) != null) {
                sb.append(s);
            }
            sendMsg(0, sb.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufReader != null)
                    bufReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendMsg(int what, Object object) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }
}
