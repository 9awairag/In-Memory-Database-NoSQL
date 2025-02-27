package com.amolw2905.command.objectCommands;

import com.amolw2905.command.IDatabaseCommands;
import com.amolw2905.database.CustomObject;
import com.amolw2905.exception.KeyNotFoundException;

import java.io.Serializable;

public class ObjectPutCommand implements IDatabaseCommands, Serializable {
    private Object value;
    private CustomObject customObject;
    private String key;

    public ObjectPutCommand(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object execute(Object object) throws KeyNotFoundException {
        customObject = (CustomObject) object;
        return customObject.put(key, value);
    }
    public Object undo() throws KeyNotFoundException {
        return customObject.remove(key);
    }

}
