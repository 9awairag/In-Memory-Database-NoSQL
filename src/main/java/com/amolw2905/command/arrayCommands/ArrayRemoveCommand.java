package com.amolw2905.command.arrayCommands;

import com.amolw2905.command.IDatabaseCommands;
import com.amolw2905.database.Array;

import java.io.Serializable;

public class ArrayRemoveCommand implements IDatabaseCommands, Serializable {
    private int index;
    private Array array;
    private Object removedValue;

    public ArrayRemoveCommand(int index) {
        this.index = index;
    }

    public Object execute(Object array) {
        this.array = (Array) array;
        removedValue =  this.array.remove(this.index);
        return removedValue;
    }

    public Object undo() {
        return array.put(removedValue);
    }

}
