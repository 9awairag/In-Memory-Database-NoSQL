package com.amolw2905.database;

public interface IDatabase {

    String getString(String key) throws Exception;

    int getInt(String key) throws Exception;

    Double getDouble(String key) throws Exception;

    IArray getArray(String key) throws Exception;

    ICustomObject getObject(String key) throws Exception;

    Object get(String key) throws Exception;

    Object remove(String key) throws Exception;
}
