package kellycorrigan.grocerylistapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {
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

    public ItemDbHelper(Context context) {
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

    // Returns a cursor that has a list for all purchased items
    Cursor queryPurchasedItems() {
        Cursor cursor = getReadableDatabase().query(
                TABLE_PURCHASED_LIST,
                null,                   // columns (all)
                null,                   // where (all rows)
                null,                   // whereArgs
                null,                   // group by
                null,                   // having
                null,                   // order by
                null                    // limit
        );
        return cursor;
    }
}

