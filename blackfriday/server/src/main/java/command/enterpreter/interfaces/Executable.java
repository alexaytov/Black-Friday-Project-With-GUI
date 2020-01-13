package command.enterpreter.interfaces;

import java.io.IOException;
import java.sql.SQLException;

public interface Executable {

    void execute() throws IOException, SQLException;
}
