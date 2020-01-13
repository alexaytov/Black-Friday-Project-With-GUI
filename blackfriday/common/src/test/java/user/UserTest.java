package user;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {

    private String firstName = "firstName";
    private String lastName = "lastName";
    private String username = "username";
    private String password = "password";
    private int age = 10;
    private LocalDateTime dateTime = LocalDateTime.now();
    private Permission permission = Permission.CLIENT;

    private User user;

    @Before
    public void setup(){
        user = new User(this.username, this.password, this.permission, this.firstName, this.lastName, this.age, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUsernameIsNull(){
        user = new User(null, this.password, this.permission, this.firstName, this.lastName, this.age, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPasswordIsNull(){
        user = new User(this.username, null, this.permission, this.firstName, this.lastName, this.age, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPermissionIsNull(){
        user = new User(this.username, this.password, null, this.firstName, this.lastName, this.age, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenFirstNameIsNull(){
        user = new User(this.username, this.password, this.permission, null, this.lastName, this.age, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLastNameIsNull(){
        user = new User(this.username, this.password, this.permission, this.firstName, null, this.age, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAgeIsZero(){
        user = new User(this.username, this.password, this.permission, this.firstName, this.lastName, 0, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAgeIsNegative(){
        user = new User(this.username, this.password, this.permission, this.firstName, this.lastName, -1, this.dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDateIsNull(){
        user = new User(this.username, this.password, this.permission, this.firstName, this.lastName, this.age, null);
    }

    @Test
    public void whenConstructorWithoutDateIsUsedDateTimeShouldBeNow(){
        user = new User(this.username, this.password, this.permission, this.firstName, this.lastName, this.age);
        assertEquals(this.user.getDateOfCreation(), LocalDateTime.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setUsernameShouldThrowExceptionWhenUsernameIsNull(){
        this.user.setUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPasswordShouldThrowExceptionWhenPasswordIsNull(){
        this.user.setUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFirstNameShouldThrowExceptionWhenFirstNameIsNull(){
        this.user.setFirstName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLastNameShouldThrowExceptionWhenLastNameIsNull(){
        this.user.setLastName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAgeShouldThrowExceptionWhenAgeIsZero(){
        this.user.setAge(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAgeShouldThrowExceptionWhenAgeIsNegative(){
        this.user.setAge(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPermissionShouldThrowExceptionWhenPermissionIsNull(){
        this.user.setPermission(null);
    }

    @Test
    public void setPasswordShouldHashPassword(){
        user = new User(this.username, this.password, Permission.CLIENT, this.firstName, this.lastName, this.age);
        assertNotEquals(this.password, this.user.getPassword());
        user.setPassword(this.password);
        assertNotEquals(this.password, this.user.getPassword());
    }

    @Test
    public void setPasswordHashShouldNotHashThePassword(){
        this.user.setPasswordHash(this.password);
        assertEquals(this.password, this.user.getPassword());
    }

    @Test
    public void constructorWithDateTimeShouldNotHashPassword(){
        this.user = new User(this.username, this.password, Permission.CLIENT, this.firstName, this.lastName, this.age, LocalDateTime.now());
        assertEquals(this.password, this.user.getPassword());
    }

    @Test
    public void getFirstNameShouldReturnCorrectValue(){
        assertEquals(this.firstName, this.user.getFirstName());
    }

    @Test
    public void getLastNameShouldReturnCorrectValue(){
        assertEquals(this.lastName, this.user.getLastName());
    }

    @Test
    public void getUsernameShouldReturnCorrectValue(){
        assertEquals(this.username, this.user.getUsername());
    }

    @Test
    public void getAgeShouldReturnCorrectValue(){
        assertEquals(this.age, this.user.getAge());
    }

    @Test
    public void getDateShouldReturnCorrectValue(){
        assertEquals(this.dateTime, this.user.getDateOfCreation());
    }

    @Test
    public void getPermissionShouldReturnCorrectValue(){
        assertEquals(this.permission, this.user.getPermission());
    }


}
