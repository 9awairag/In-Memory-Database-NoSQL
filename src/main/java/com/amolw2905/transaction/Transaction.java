package com.amolw2905.transaction;

import com.amolw2905.command.IDatabaseCommands;
import com.amolw2905.database.Array;
import com.amolw2905.database.CustomObject;
import com.amolw2905.database.Database;
import com.amolw2905.database.IDatabase;
import com.amolw2905.decorator.DatabaseExecutor;
import com.amolw2905.exception.KeyNotFoundException;

import java.util.Stack;

public class Transaction implements IDatabase {

    private Database database;
    private DatabaseExecutor databaseExecutor;

    private Boolean isActive = true;
    Stack<IDatabaseCommands> operations = new Stack<>();
    public Transaction(Database database){
        this.database = database;
        databaseExecutor = new DatabaseExecutor(this.database,operations);
    }
    public boolean put(String key, Object value) {
        boolean response = false;
        try {
            response = databaseExecutor.put(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public int getInt(String key) throws Exception {
        return database.getInt(key);
    }

    public boolean isActive() {
        return isActive;
    }

    public Double getDouble(String key) throws Exception {
        return database.getDouble(key);
    }

    public String getString(String key) throws Exception {
        return database.getString(key);
    }

    public Array getArray(String key) throws Exception {
        return database.getArray(key);
    }

    public CustomObject getObject(String key) throws Exception {
        return database.getObject(key);
    }


    public Object get(String key) throws Exception {
        return database.get(key);
    }

    public Object remove(String key) throws KeyNotFoundException {
        return databaseExecutor.remove(key);
    }

    public boolean abort() throws KeyNotFoundException {
        operations = databaseExecutor.getCommands();
        while(!operations.isEmpty()) {
            IDatabaseCommands operation = operations.pop();
            operation.undo();
        }
        isActive = false;

        databaseExecutor.snapshot();
        return true;
    }

    /**
     * Commit commands and make the transaction inActive
     * @return true if commands commited
     */
    public boolean commit() {
        databaseExecutor.commitCommands();
        isActive = false;
        return true;
    }
}
