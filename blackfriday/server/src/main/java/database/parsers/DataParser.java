package database.parsers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataParser<T> {

    T parseData(ResultSet resultSet) throws SQLException, IOException;
}
