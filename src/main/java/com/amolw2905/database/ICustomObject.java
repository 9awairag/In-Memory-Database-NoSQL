package com.amolw2905.database;

import com.amolw2905.exception.IncompatibleTypeException;
import com.amolw2905.exception.KeyNotFoundException;

public interface ICustomObject {
    Object get(String key) throws KeyNotFoundException;

    String getString(String key) throws Exception;

    int getInt(String key) throws Exception;

    Double getDouble(String key) throws Exception;

    IArray getArray(String key) throws Exception;

    boolean put(String key, Object value) throws KeyNotFoundException;

    Object remove(String key) throws Exception;

    ICustomObject getObject(String key) throws IncompatibleTypeException;
}
