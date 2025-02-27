package com.amolw2905.command.databaseCommands;

import com.amolw2905.command.IDatabaseCommands;
import com.amolw2905.database.Database;
import com.amolw2905.exception.KeyNotFoundException;

import java.io.Serializable;

public class DatabaseRemoveCommand implements IDatabaseCommands, Serializable {
    private Database database;
    private String key;
    private Object removedValue;

    public DatabaseRemoveCommand(String key) {
        this.key = key;
    }

    @Override
    public Object execute(Object db) throws KeyNotFoundException {
        this.database = (Database) db;
        removedValue = this.database.remove(key);
        return removedValue;
    }

    public Object undo() {
        return database.put(key, removedValue);
    }

}
