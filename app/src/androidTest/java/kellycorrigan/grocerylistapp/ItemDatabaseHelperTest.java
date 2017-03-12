package kellycorrigan.grocerylistapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ItemDatabaseHelperTest {

    private ItemDatabaseHelper database;

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(ItemDatabaseHelper.DATABASE_NAME);
        database = new ItemDatabaseHelper(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = getTargetContext();

        assertEquals("kellycorrigan.grocerylistapp", appContext.getPackageName());
    }

    @Test
    public void shouldAddAndRemoveGroceryItem() throws Exception {
        // Add one grocery item
        database.addGroceryItem("bread");
        ArrayList<String> items = database.queryAllGroceryItems();
        assertThat(items.size(), is(1));
        assertTrue(items.get(0).equals("bread"));

        // Add two more grocery items
        database.addGroceryItem("lettuce");
        database.addGroceryItem("tomatoes");
        items = database.queryAllGroceryItems();
        assertThat(items.size(), is(3));
        assertTrue(items.get(1).equals("lettuce"));

        // Remove one grocery item
        database.removeGroceryItem("lettuce");
        items = database.queryAllGroceryItems();
        assertThat(items.size(), is(2));
        assertFalse(items.contains("lettuce"));
    }
}
