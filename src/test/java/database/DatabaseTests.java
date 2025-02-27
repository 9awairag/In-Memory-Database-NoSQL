package database;

import com.amolw2905.database.Array;
import com.amolw2905.database.CustomObject;
import com.amolw2905.database.Database;
import com.amolw2905.decorator.DatabaseExecutor;
import com.amolw2905.exception.IncompatibleTypeException;
import com.amolw2905.exception.KeyNotFoundException;
import com.amolw2905.fileio.FileOperations;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DatabaseTests {

    FileOperations fileOperations;
    DatabaseExecutor databaseExecutor;
    private final String DATABASE_MEMENTO_FILEPATH = "src/main/resources/dbSnapshot.txt";
    private final String COMMANDS_FILEPATH = "src/main/resources/commands.txt";

    @BeforeEach
    public void setUp() {
        databaseExecutor = new DatabaseExecutor(new Database());
        fileOperations = new FileOperations();
        fileOperations.clearFile(new File(DATABASE_MEMENTO_FILEPATH));
        fileOperations.clearFile(new File(COMMANDS_FILEPATH));
    }

    @Test
    public void testPutInDB() throws Exception {
        databaseExecutor.put("Key", "John");
        Assert.assertEquals("John", databaseExecutor.get("Key"));
    }

    @Test
    public void testRemoveFromDB() throws Exception {
        databaseExecutor.put("Key", "John");
        databaseExecutor.remove("Key");
        Assertions.assertThrows(KeyNotFoundException.class, () -> databaseExecutor.get("Key"));
    }

    @Test
    public void testPutFromStringArray() throws Exception {
        String value = "";
        databaseExecutor.put("Key", new Array().fromString("[\"Amol\", 2.0, {\"name\": \"Amol\", \"Number\": 21.0}]"));
        Array array = new Array();
        array.put("Amol");
        array.put(2.0);

        CustomObject customObject = new CustomObject();
        customObject.put("name","Amol");
        customObject.put("Number",21.0);
        array.put(customObject);

        Assert.assertEquals(array, databaseExecutor.get("Key"));
    }

    @Test
    public void testPutFromStringObject() throws Exception {
        String value = "";
        databaseExecutor.put("Key", new CustomObject().fromString("{\"name\": \"Roger\", \"age\": 21.0}"));
        CustomObject customObject = new CustomObject();
        customObject.put("name","Roger");
        customObject.put("age",21.0);
        Assert.assertEquals(customObject, databaseExecutor.get("Key"));
    }

    @Test
    public void testGetXException() throws Exception {
        databaseExecutor.put("Key", "John");
        Assertions.assertThrows(IncompatibleTypeException.class, () -> databaseExecutor.getInt("Key"));
    }
}
