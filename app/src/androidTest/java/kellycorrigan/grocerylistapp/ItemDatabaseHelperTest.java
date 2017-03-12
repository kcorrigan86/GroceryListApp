package kellycorrigan.grocerylistapp;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
    public void shouldAddAndRemoveGroceryItems() throws Exception {
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

    @Test
    public void shouldAddPurchasedItems() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy", Locale.US);
        Date todaysDate = new Date();

        // Add one purchased item
        database.addPurchasedItem("spaghetti", "(47.6204, -122.3130)");
        ArrayList<PurchasedItem> items = database.queryAllPurchasedItems();
        assertThat(items.size(), is(1));

        // Test the PurchasedItem details
        PurchasedItem item = items.get(0);
        assertThat(item.getItem(), is("spaghetti"));
        assertThat(item.getDate(), is(dateFormat.format(todaysDate)));
        assertThat(item.getLocation(), is("(47.6204, -122.3130)"));

        // Add two more purchased items
        database.addPurchasedItem("basil", "(47.0000, -122.0000)");
        database.addPurchasedItem("olive oil", "(47.1111, -122.1111)");
        items = database.queryAllPurchasedItems();
        assertThat(items.size(), is(3));

        // Test the PurchasedItem details
        item = items.get(1);
        assertThat(item.getItem(), is("basil"));
        assertThat(item.getDate(), is(dateFormat.format(todaysDate)));
        assertThat(item.getLocation(), is("(47.0000, -122.0000)"));
    }
}
