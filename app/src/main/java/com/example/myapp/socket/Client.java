package com.example.myapp.socket;

import android.util.TimeUtils;

import com.example.myapp.socket.i.CallClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {

    // 服务器连接参数
    private static final String IP = "175.178.217.212";
    private static final int PORT = 12345;
    private static Client SocketClient = null;
    private static Socket client = null;
    private static OutputStream os = null;
    private static InputStream is = null;
    private List<String> ServiceMessageList;

    private Client() throws Exception {
        if (client == null) {
            ConnectionThread conn = new ConnectionThread();
            conn.start();
            conn.join(2000);
        }
        ServiceMessageList = new CopyOnWriteArrayList<>();
    }

    public static Client getClient() throws Exception {
        synchronized (Client.class) {
            if (SocketClient == null) {
                SocketClient = new Client();
                // 开启一条持续读取的socket线程
                ReadTread readTread = new ReadTread();
                readTread.start();
            }
        }
        return SocketClient;
    }

    public static OutputStream getOs() {
        return os;
    }

    public static InputStream getIs() {
        return is;
    }

    public SendThread Send(String msg, CallClient call) {
        SendThread send = new SendThread();
        send.setMsg(msg);
        send.setCall(call);
        send.start();
        return send;
    }

    static class ConnectionThread extends Thread {
        @Override
        public void run() {
            try {
                client = new Socket(IP, PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static public class SendThread extends Thread {
        String msg = "";
        CallClient call;

        public void setCall(CallClient call) {
            this.call = call;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (msg.length() == 0) return;
            try {
                Client.getClient().removeMessage();
                client.getOutputStream().write(msg.getBytes());
                TimeUnit.SECONDS.sleep(1);
                if (!Client.getServiceMessageList().isEmpty())
                    call.Receive(Client.getServiceMessageList().get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getServiceMessageList() throws Exception {
        return Client.getClient().ServiceMessageList;
    }

    public void removeMessage() {
        if (!ServiceMessageList.isEmpty())
            ServiceMessageList.clear();
    }

    static class ReadTread extends Thread {
        @Override
        public void run() {
            byte[] buf;
            int ret;
            StringBuilder result = new StringBuilder();
            while (true) {
                try {
                    while (true) {
                        buf = new byte[1024];
                        ret = client.getInputStream().read(buf);
                        if (ret == 1024) {
                            String s = new String(buf);
                            result.append(s);
                            continue;
                        }
                        String s = new String(buf);
                        Client.getServiceMessageList().add(result + s);
                        result = new StringBuilder();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
