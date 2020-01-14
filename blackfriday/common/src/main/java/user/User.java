package user;

import commonMessages.ExceptionMessages;
import passwordHasher.BCryptHasher;
import validator.Validator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class User implements Serializable, Cloneable {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int age;
    private LocalDateTime dateOfCreation;
    private Permission permission;


    public User(String username, String password, Permission permission, String firstName, String lastName, int age, LocalDateTime dateOfCreation) {
        this.setUsername(username);
        // doesn't hash password
        this.setPasswordHash(password);
        this.setPermission(permission);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.setDateOfCreation(dateOfCreation);
    }


    public User(String username, String password, Permission permission, String firstName, String lastName, int age) {
        this.setUsername(username);
        // hashes password
        this.setPassword(password);
        this.setPermission(permission);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.dateOfCreation = LocalDateTime.now();
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        Validator.requireNonNull(permission, ExceptionMessages.PERMISSION_NULL);
        this.permission = permission;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        Validator.requireNonBlank(username, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        Validator.requireNonBlank(password, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
        password = BCryptHasher.hash(password);
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        Validator.requireNonBlank(firstName, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        Validator.requireNonBlank(lastName, ExceptionMessages.NAME_NULL_OR_EMPTY);
        this.lastName = lastName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        Validator.requireNonNegative(age, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
        Validator.requireNonZero(age, ExceptionMessages.AGE_MUST_BE_POSITIVE_NUMBER);
        this.age = age;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    private void setDateOfCreation(LocalDateTime dateOfCreation) {
        Validator.requireNonNull(dateOfCreation, ExceptionMessages.DATE_NULL);
        this.dateOfCreation = dateOfCreation;
    }

    public void setPasswordHash(String password) {
        Validator.requireNonBlank(password, ExceptionMessages.PASSWORD_NULL_OR_EMPTY);
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User baseUser = (User) o;
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
