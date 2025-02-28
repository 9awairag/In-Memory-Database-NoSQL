package com.amolw2905.command.arrayCommands;

import com.amolw2905.command.IDatabaseCommands;
import com.amolw2905.database.Array;

import java.io.Serializable;

public class ArrayPutCommand implements IDatabaseCommands, Serializable {
    private Object value;
    private Array array;
    private int index;

    public ArrayPutCommand(Object value) {
        this.value = value;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        index = this.array.length();
        return this.array.put(value);
    }

    public Object undo() {
        return array.remove(index);
    }

}
