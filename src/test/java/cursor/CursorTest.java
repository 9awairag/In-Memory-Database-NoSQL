package cursor;

import com.amolw2905.cursor.Cursor;
import com.amolw2905.database.Array;
import com.amolw2905.database.CustomObject;
import com.amolw2905.database.Database;
import com.amolw2905.decorator.DatabaseExecutor;
import com.amolw2905.observer.Observer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CursorTest {
    @Test
    void addCursor() throws Exception {
        Array finalArray = new Array();

        Array array = new Array();
        array.put("Amol");
        array.put(26);

        CustomObject customObject = new CustomObject();
        customObject.put("Location", "San Ramon");
        customObject.put("Contact", "[000-000-0000]");

        finalArray.put(array);
        finalArray.put(customObject);
        finalArray.put(100);

        Database database = new Database();
        DatabaseExecutor databaseExecutor = new DatabaseExecutor(database);
        databaseExecutor.put("AmolW",finalArray);
        Cursor cursor = database.getCursor("AmolW");
        Observer observer = new Observer();
        cursor.addObserver(observer);
        databaseExecutor.getArray("AmolW").put("India");
        Assertions.assertEquals(observer.getUpdates().get(0),"AmolW in the DB updated with [[\"Amol\",26],{\"Location\":\"San Ramon\",\"Contact\":\"[000-000-0000]\"},100]");

    }
}
