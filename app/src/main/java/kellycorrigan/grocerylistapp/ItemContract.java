package kellycorrigan.grocerylistapp;


import android.provider.BaseColumns;

public class ItemContract {
    public static final String DB_NAME = "grocerylist.db";
    public static final int DB_VERSION = 1;

    public class ItemEntry implements BaseColumns {
        public static final String TABLE = "items";

        public static final String COL_ITEM_TITLE = "title";
    }

}
