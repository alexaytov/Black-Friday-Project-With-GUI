package connection;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class TCPConnectionTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenSocketIsNull() {
        Socket socket = null;
        try {
            TCPConnection tcpConnection = new TCPConnection(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
