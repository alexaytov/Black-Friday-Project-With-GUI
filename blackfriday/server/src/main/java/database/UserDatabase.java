package database;

import database.interfaces.Database;
import exceptions.NotFoundException;
import user.interfaces.User;

import java.io.IOException;
import java.util.Map;

public class UserDatabase implements Database<User> {
    private final String filename;
    private Map<String, User> data;

    public UserDatabase(String fileName) throws IOException {
//        super(JSONReader.readUsers(fileName, userType));
        this.filename = fileName;
    }

    public User getByName(String username) throws NotFoundException {
        User user = this.data.get(username);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @Override
    public synchronized void write(User user) {
        this.data.put(user.getUsername(), user);
        this.saveAllChanges();

    }

    @Override
    public synchronized void delete(String username) throws NotFoundException {
        if (this.data.remove(username) == null) {
            throw new NotFoundException();
        }
        this.saveAllChanges();
    }

    @Override
    public boolean contains(User user) {
        return this.data.values().contains(user);
    }

    public boolean contains(String username) {
        return this.data.containsKey(username);
    }

    @Override
    public void saveAllChanges() {
//        JSONWriter.writeUsers(super.getData(), filename);
        //TODO save all change with the new library
    }
}
