package com.amolw2905.cursor;


import com.amolw2905.database.Array;
import com.amolw2905.database.CustomObject;
import com.amolw2905.database.Database;
import com.amolw2905.exception.IncompatibleTypeException;
import com.amolw2905.exception.KeyNotFoundException;
import com.amolw2905.observer.IObserver;
import com.amolw2905.observer.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anuj Kawane <akawane0813@sdsu.edu> <825890176>
 */
public class Cursor {
    private String key;
    private Database database;
    private Object currentValue;

    public List<IObserver> observers = new ArrayList<>();

    private CursorTracker cursorTracker = CursorTracker.getInstance();
    public Cursor(String key, Database database) throws Exception {

        this.database = database;
        this.key = key;
        this.currentValue = this.database.get(key);

        if (this.currentValue instanceof Array) {
            this.currentValue = ((Array)this.database.get(key)).clone();
        } else if(this.currentValue instanceof CustomObject) {
            this.currentValue = ((CustomObject)this.database.get(key)).clone();
        }

        cursorTracker.put(key,this);
    }

    /**
     * updates all observers in the list with the change
     * @return true
     */
    public boolean updateObserver() {
        String message = "";
        try {
            Object newValue = this.database.get(key);
            message =  key + " in the DB updated with "+newValue.toString();

        } catch(KeyNotFoundException e) {
            message = key + " removed from the DB";
        } catch (Exception e) {
            e.printStackTrace();
        }


        String finalMessage = message;
        observers.forEach((object)->object.update(finalMessage));
        return true;
    }

    /**
     * Adds observer to the list
     * @param observer to be added in list
     * @return true if added
     */
    public boolean addObserver(IObserver observer) {
        observers.add(observer);
        return true;
    }

    public boolean removeObserver(Observer observer) {
        observers.remove(observer);
        return true;
    }

    public Object get() {
        return this.currentValue;
    }

    public Integer getInt() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof Integer)) {
            throw new IncompatibleTypeException("Object is not of type integer");
        }
        return (Integer) this.currentValue;
    }

    public Double getDouble() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof Double)) {
            throw new IncompatibleTypeException("Object is not of type Double");
        }
        return (Double) this.currentValue;
    }

    public String getString() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof String)) {
            throw new IncompatibleTypeException("Object is not of type string");
        }
        return (String) this.currentValue;
    }

    public Array getArray() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof Array)) {
            throw new IncompatibleTypeException("Object is not of type Array");
        }
        return (Array) this.currentValue;
    }

    public CustomObject getObject() throws IncompatibleTypeException {
        if(!(this.currentValue instanceof CustomObject)) {
            throw new IncompatibleTypeException("Object is not of type CustomObject");
        }
        return (CustomObject) this.currentValue;
    }
}
