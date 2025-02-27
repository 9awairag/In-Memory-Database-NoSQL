package com.amolw2905.database;

import com.amolw2905.exception.IncompatibleTypeException;
import com.amolw2905.exception.KeyNotFoundException;

public interface IArray {

    boolean put(Object object) throws KeyNotFoundException;

    Object get(int index);

    String getString(int index) throws IncompatibleTypeException;

    int getInt(int index) throws IncompatibleTypeException;

    Double getDouble(int index) throws IncompatibleTypeException;

    IArray getArray(int index) throws IncompatibleTypeException;

    ICustomObject getObject(int index) throws IncompatibleTypeException;

    Object remove(int index) throws Exception;
}