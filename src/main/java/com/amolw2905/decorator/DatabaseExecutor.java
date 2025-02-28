package com.amolw2905.decorator;

import com.amolw2905.command.IDatabaseCommands;
import com.amolw2905.command.databaseCommands.DatabasePutCommand;
import com.amolw2905.command.databaseCommands.DatabaseRemoveCommand;
import com.amolw2905.cursor.Cursor;
import com.amolw2905.database.*;
import com.amolw2905.exception.KeyNotFoundException;
import com.amolw2905.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.util.List;
import java.util.Stack;

public class DatabaseExecutor implements IDatabase {

    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";

    private Database database;
    private Stack<IDatabaseCommands> stack ;

    private File commandFile;
    private  Executor executor;
    private List<String> commandStrings;

    public DatabaseExecutor(Database db) {
        this.database = db;
        executor = Executor.getInstance(db,new File(COMMANDS_FILEPATH));

    }

    public DatabaseExecutor(Database db, File commandFile) {
        executor = Executor.getInstance(db,commandFile);
        this.database = db;
        this.commandFile = commandFile;
        executor = Executor.getInstance(db,new File(COMMANDS_FILEPATH));

    }

    public DatabaseExecutor(Database db, Stack<IDatabaseCommands> operations) {
        this.database = db;
        this.stack = operations;
        commandStrings = new Stack<>();
        executor = Executor.getInstance(db,new File(COMMANDS_FILEPATH));

    }

    public void executeSavedCommands(File commandFile) throws Exception {
        List<List<String>> commands;
        if (commandFile != null) {
            commands = executor.getCommands(commandFile);
        } else {
            commands = executor.getCommands(new File(COMMANDS_FILEPATH));
        }
        for (List<String> command : commands) {
            executeCommands(command);
        }
    }

    public void executeCommands(List<String> commands) throws Exception {
        String key = commands.get(1);
        String value = commands.get(2);

        if (database.containsKey(key)) {
            database.remove(key);
            put(key, getObjectFromString(value));
        } else {
            put(key, getObjectFromString(value));
        }
    }


    /**
     * Parse value to specific type of object based on string format
     * @param value String value which needs to be parsed
     * @return specific type of object
     * @throws JsonProcessingException if any in conversion of fromString
     */
    public Object getObjectFromString(String value) throws JsonProcessingException {

        if (value.charAt(0) == '[') {
            return new Array().fromString(value);
        } else if (value.charAt(0) == '{') {
            return new CustomObject().fromString(value);
        } else if(value.charAt(0) == '('){
            String intValue = value.substring(1,value.length()-1);
            return Integer.parseInt(intValue);
        } else {
            return value;
        }
    }

    public String toString() {
        return this.database.toString();
    }

    /**
     * Execute put command and saves String format of command in file
     * @param key in database where value will be stored
     * @param value to store in DB against given key
     * @return true if success, false otherwise
     * @throws Exception if any
     */
    public boolean put(String key, Object value) throws Exception {
        IDatabaseCommands put = new DatabasePutCommand(key,value);

        put.execute(this.database);

        insertSuperKey(key, value);
        String commandString = "PUT->" + key + "->" + database.get(key).toString();
        if (stack == null) {
            executor.writeToFile(commandString);
        } else {
            this.stack.add(put);
            commandStrings.add(commandString);
        }


        return true;
    }

    /**
     * Inserts key at every child object to handle the nested values in objects
     * @param key in database where value will be stored
     * @param value to store in DB against given key
     */
    public void insertSuperKey(String key, Object value) {
        if(value instanceof Array) {
            ((Array)value).setParent(key);
        } else if (value instanceof CustomObject) {
            ((CustomObject)value).setParent(key
            );
        }
    }

    public Object get(String key) throws Exception {
        return this.database.get(key);
    }

    public int getInt(String key) throws Exception {
        return this.database.getInt(key);
    }

    public Double getDouble(String key) throws Exception {
        return this.database.getDouble(key);
    }

    public String getString(String key) throws Exception {
        return this.database.getString(key);
    }

    public IArray getArray(String key) throws Exception {
        return new ArrayExecutor(this.database.getArray(key));
    }

    public ICustomObject getObject(String key) throws Exception {
        return new ObjectExecutor(database.getObject(key));
    }

    /**
     * Execute remove command and saves String format of command in file
     * @param key in database to be deleted
     * @return removed object
     * @throws Exception if given key is not present
     */
    public Object remove(String key) throws KeyNotFoundException {
        IDatabaseCommands remove = new DatabaseRemoveCommand(key);

        String commandString = "PUT->" + key +  "->" + "NO-VALUE";
        Object value = remove.execute(this.database);

        if (stack == null) {
            executor.writeToFile(commandString);
        } else {
            this.stack.add(remove);
            commandStrings.add(commandString);

        }

        return value;
    }

    public Cursor getCursor(String key) {
        return this.database.getCursor(key);
    }

    public Stack<IDatabaseCommands> getCommands() {
        return stack;
    }
    public boolean clearCommands() {
        this.stack = new Stack<>();
        return true;
    }

    public Transaction transaction() {
        return this.database.transaction();
    }

    public void commitCommands() {
        this.commandStrings.forEach((operation) -> executor.writeToFile(operation));
        snapshot();
    }

    public void snapshot() {
        database.snapshot();
    }

}
