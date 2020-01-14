package connection;

import commonMessages.ExceptionMessages;
import exceptions.ConnectionException;
import validator.Validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPConnection implements Connection {

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public TCPConnection(Socket socket) throws IOException {
        Validator.requireNonNull(socket, ExceptionMessages.SOCKET_NULL);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public <T> void write(T data) {
        try {
            this.objectOutputStream.writeObject(data);
        } catch (IOException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

    @Override
    public <T> T read() {
        try {
            return (T) this.objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
           throw new ConnectionException(ex.getMessage());
        }
    }
}
