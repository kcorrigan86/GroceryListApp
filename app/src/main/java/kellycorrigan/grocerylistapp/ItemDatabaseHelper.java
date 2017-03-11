package kellycorrigan.grocerylistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class ItemDatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "groceryItemsManager";

    // Table names
    private static final String TABLE_GROCERY_LIST = "grocery_list_items";
    private static final String TABLE_PURCHASED_LIST = "purchased_list_items";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_ITEM = "item";
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION = "latitude";

    // Grocery list table create statement
    private static final String CREATE_TABLE_GROCERY = "CREATE TABLE " +
            TABLE_GROCERY_LIST + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM +
            " TEXT) ";

    // Purchased items table create statement
    private static final String CREATE_TABLE_PURCHASED = "CREATE TABLE " +
            TABLE_PURCHASED_LIST + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM +
            " TEXT," + KEY_DATE + " DATETIME," + KEY_LOCATION + " TEXT)";

    ItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GROCERY);
        db.execSQL(CREATE_TABLE_PURCHASED);
    }

    // Handle upgrading to a new schema version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_LIST);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PURCHASED_LIST);

        // Create new tables
        onCreate(db);
    }

    // Add an item into the grocery list table
    void addGroceryItem(String item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        // Put the item into a ContentValues structure
        values.put(KEY_ITEM, item);

        // Insert ContentValues row into the table
        db.insertWithOnConflict(
                TABLE_GROCERY_LIST,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    // Remove an item from the grocery list table
    void removeGroceryItem(String item) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                TABLE_GROCERY_LIST,
                KEY_ITEM + " = ?",
                new String[]{item}
        );

        db.close();
    }

    // Return an ArrayList of all items in the grocery items table
    ArrayList<String> queryAllGroceryItems() {
        ArrayList<String> groceryList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Get a cursor with a list of all grocery list items
        Cursor cursor = db.query(TABLE_GROCERY_LIST,
                new String[]{KEY_ITEM},
                null, null, null, null, null);

        // Loop through the cursor list and add each item to the ArrayList
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(KEY_ITEM);
            groceryList.add(cursor.getString(idx));
        }

        cursor.close();
        db.close();

        return groceryList;
    }

    // Add an item into the purchased items table
    void addPurchasedItem(String item, String location) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy", Locale.US);
        Date date = new Date();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        // Put the item, current date, and location into a ContentValues structure
        values.put(KEY_ITEM, item);
        values.put(KEY_DATE, dateFormat.format(date));
        values.put(KEY_LOCATION, location);

        // Insert ContentValues row into the table
        db.insertWithOnConflict(
                TABLE_PURCHASED_LIST,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    // Return an ArrayList of all PurchasedItems in the purchased items table
    ArrayList<PurchasedItem> queryAllPurchasedItems() {
        ArrayList<PurchasedItem> purchasedList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Get a cursor with a list of all purchased items
        Cursor cursor = db.query(
                TABLE_PURCHASED_LIST,
                new String[]{KEY_ITEM, KEY_DATE, KEY_LOCATION},     // columns
                null,                                               // where (all rows)
                null,                                               // whereArgs
                null,                                               // group by
                null,                                               // having
                null,                                               // order by
                null                                                // limit
        );

        // Loop through the cursor list
        while(cursor.moveToNext()) {
            // Create a new PurchasedItem from each row in the cursor list
            PurchasedItem item = new PurchasedItem();
            item.setItem(cursor.getString(cursor.getColumnIndex(KEY_ITEM)));
            item.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            item.setLocation(cursor.getString(cursor.getColumnIndex(KEY_LOCATION)));

            // Add the PurchasedItem to the ArrayList
            purchasedList.add(item);
        }

        cursor.close();
        db.close();

        return purchasedList;
    }
}

