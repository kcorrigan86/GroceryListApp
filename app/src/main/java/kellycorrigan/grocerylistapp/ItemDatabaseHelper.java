package kellycorrigan.grocerylistapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ItemDatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "groceryItemsManager";

    // Table names
    public static final String TABLE_GROCERY_LIST = "grocery_list_items";
    public static final String TABLE_PURCHASED_LIST = "purchased_list_items";

    // Column names
    public static final String KEY_ID = "id";
    public static final String KEY_ITEM = "item";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOCATION = "latitude";

    // Grocery list table create statement
    private static final String CREATE_TABLE_GROCERY = "CREATE TABLE " +
            TABLE_GROCERY_LIST + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM +
            " TEXT) ";

    // Purchased items table create statement
    private static final String CREATE_TABLE_PURCHASED = "CREATE TABLE " +
            TABLE_PURCHASED_LIST + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM +
            " TEXT," + KEY_DATE + " DATETIME," + KEY_LOCATION + " TEXT)";

    public ItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GROCERY);
        db.execSQL(CREATE_TABLE_PURCHASED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_LIST);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PURCHASED_LIST);

        // Create new tables
        onCreate(db);
    }


    // Return an ArrayList of all PurchasedItems for the purchased items database
    ArrayList<PurchasedItem> queryAllPurchasedItems() {
        ArrayList<PurchasedItem> purchasedList = new ArrayList<PurchasedItem>();
        SQLiteDatabase db = getReadableDatabase();

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

        while(cursor.moveToNext()) {
            PurchasedItem item = new PurchasedItem();
            item.setItem(cursor.getString(cursor.getColumnIndex(KEY_ITEM)));
            item.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            item.setLocation(cursor.getString(cursor.getColumnIndex(KEY_LOCATION)));
            purchasedList.add(item);
        }

        cursor.close();
        db.close();

        return purchasedList;
    }
}

