package database.parsers;

import user.Permission;
import user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserParser implements DataParser<User> {

    @Override
    public User parseData(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String permission = resultSet.getString("permission");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        int age = resultSet.getInt("age");
        String dateOfRegistrationAsString = resultSet.getString("date_of_registration");
        dateOfRegistrationAsString = dateOfRegistrationAsString.replace(' ', 'T');
        LocalDateTime dateOfRegistration = LocalDateTime.parse(dateOfRegistrationAsString);
        
        return new User(username, password, Permission.valueOf(permission.toUpperCase()), firstName, lastName, age, dateOfRegistration);
    }
}
