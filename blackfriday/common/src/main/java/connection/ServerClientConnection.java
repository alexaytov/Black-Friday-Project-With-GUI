package connection;

import java.io.IOException;

public interface ServerClientConnection {

    <T> void write(T data) throws IOException;

    <T> T read() throws IOException, ClassNotFoundException;

}
