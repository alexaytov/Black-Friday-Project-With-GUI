package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPConnection implements Connection {

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public TCPConnection(Socket socket) throws IOException {
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public <T> void write(T data) throws IOException {
        this.objectOutputStream.writeObject(data);
    }

    @Override
    public <T> T read() throws IOException, ClassNotFoundException {
        return (T) this.objectInputStream.readObject();
    }
}
