package user;

import commonMessages.ExceptionMessages;
import user.interfaces.User;
import validator.Validator;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


public abstract class BaseUser implements User, Serializable, Cloneable {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int age;
    private ZonedDateTime dateOfCreation;


    protected BaseUser(String username, String password, String firstName, String lastName, int age, ZonedDateTime dateOfCreation) {
        this(username, password, firstName, lastName, age);
        this.setDateOfCreation(dateOfCreation);
    }

    protected BaseUser(String username, String password, String firstName, String lastName, int age) {
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.dateOfCreation = ZonedDateTime.now();

    }

    private void setDateOfCreation(ZonedDateTime dateOfCreation) {
        Validator.requireNonNull(dateOfCreation);
        this.dateOfCreation = dateOfCreation;
    }

    @Override
    public ZonedDateTime getDateOfCreation() {
        return this.dateOfCreation;
    }

    @Override
    public void setPassword(String password) {
        Validator.requireNonBlank(password, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
        this.password = password;
    }

    @Override
    public void setUsername(String username) {
        Validator.requireNonBlank(username, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.username = username;
    }


    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setFirstName(String firstName) {
        Validator.requireNonBlank(firstName, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.firstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        Validator.requireNonBlank(lastName, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.lastName = lastName;
    }

    @Override
    public void setAge(int age) {
        Validator.requireNonNegative(age, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
        this.age = age;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    @Override
    public int getAge() {
        return this.age;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseUser baseUser = (BaseUser) o;
        return username.equals(baseUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
