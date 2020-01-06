package database;

import commonMessages.ExceptionMessages;
import database.interfaces.Database;

import java.util.Map;

import static validator.Validator.requireNonNull;

public abstract class BaseUserDatabase<T> implements Database<T> {

    private Map<String, T> data;

    BaseUserDatabase(Map<String, T> data) {
        this.setData(data);
    }

    private void setData(Map<String, T> data) {
        requireNonNull(data, ExceptionMessages.DATABASE_MAP_NULL);
        this.data = data;
    }

    public Map<String, T> getData() {
        return data;
    }
}
