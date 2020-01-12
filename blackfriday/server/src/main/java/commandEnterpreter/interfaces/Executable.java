package commandEnterpreter.interfaces;

import java.io.IOException;
import java.sql.SQLException;

public interface Executable {

    /**
     * @throws IOException                if IO error occurs
     * @throws SQLException               if SQL error occurs
     * @throws ClassNotFoundException     if read class from client connection is not found
     * @throws CloneNotSupportedException if object is not cloneable
     */
    void execute() throws IOException, SQLException, ClassNotFoundException, CloneNotSupportedException;
}
