package kellycorrigan.grocerylistapp;

// Package sources:
// https://www.sitepoint.com/starting-android-development-creating-todo-app/
// http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ItemDbHelper mHelper;
    private ListView mItemListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new ItemDbHelper(this);
        mItemListView = (ListView) findViewById(R.id.grocery_list);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                addGroceryItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // When the add item button is selected from the menu, create an alert dialog for the
    // user to enter the item
    private void addGroceryItem() {
        final EditText itemEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new grocery item")
                .setMessage("What do you want to add?")
                .setView(itemEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = String.valueOf(itemEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(mHelper.KEY_ITEM, item);

                        // Insert row into the grocery list table to the database
                        db.insertWithOnConflict(
                                mHelper.TABLE_GROCERY_LIST,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);

                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    // Once an item is checked off, it is removed from the grocery list and added
    // to the purchased list
    public void checkOffItem(View view) {
        // Take the passed-in checkbox and find the text of the grocery item it belongs to
        View parent = (View) view.getParent();
        TextView itemTextView = (TextView) parent.findViewById(R.id.item_title);
        String item = String.valueOf(itemTextView.getText());

        // Remove the item from the grocery list table of the database
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(
                mHelper.TABLE_GROCERY_LIST,
                mHelper.KEY_ITEM + " = ?",
                new String[]{item});

        // Add the item to the purchased items table of the database
        ContentValues values = new ContentValues();
        values.put(mHelper.KEY_ITEM, item);
        db.insertWithOnConflict(
                mHelper.TABLE_PURCHASED_LIST,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();

        // Uncheck the checkbox in the grocery list because it will be reused
        AppCompatCheckBox v = (AppCompatCheckBox) view;
        v.setChecked(false);

        // Update the UI
        updateUI();
    }

    // Update the view
    private void updateUI() {

        // Add all items in the database into an ArrayList
        ArrayList<String> groceryList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(mHelper.TABLE_GROCERY_LIST,
                new String[]{mHelper.KEY_ITEM},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(mHelper.KEY_ITEM);
            groceryList.add(cursor.getString(idx));
        }

        // Add all items in the ArrayList into the view using an ArrayAdapter
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(
                    this,
                    R.layout.grocery_item,
                    R.id.item_title,
                    groceryList);
            mItemListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(groceryList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
}
