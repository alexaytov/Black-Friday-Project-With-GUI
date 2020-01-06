package store;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Store {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Store(InetAddress ip, int port) throws IOException {
        connectToServer(ip, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        oos = new ObjectOutputStream(outputStream);
        oos.flush();
        ois = new ObjectInputStream(inputStream);


    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    private void connectToServer(InetAddress ip, int port) throws IOException {
        socket = new Socket(ip, port);
    }
}
