package connection;

public interface Connection {

    <T> void write(T data);

    <T> T read();

}
