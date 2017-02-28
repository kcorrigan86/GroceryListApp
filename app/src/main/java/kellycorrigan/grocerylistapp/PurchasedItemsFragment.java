package kellycorrigan.grocerylistapp;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class PurchasedItemsFragment extends ListFragment {

    private Cursor mCursor;
    private ItemDbHelper mHelper;
    private ArrayAdapter<String> mAdapter;
    private ListView mItemListView;

    public PurchasedItemsFragment() {
        // Required empty public constructor
    }

    public static PurchasedItemsFragment newInstance() {
        return new PurchasedItemsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Create a new ItemDbHelper
        ItemDbHelper itemDbHelper = new ItemDbHelper(this.getActivity());

        // Query the purchased items and obtain a cursor
        mCursor = itemDbHelper.queryPurchasedItems();
    }

    @Override
    public void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.purchased_item, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // Update the view
    private void updateUI() {

        // Add all items in the database into an ArrayList
        ArrayList<String> groceryList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(mHelper.TABLE_PURCHASED_LIST,
                new String[]{mHelper.KEY_ITEM},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(mHelper.KEY_ITEM);
            groceryList.add(cursor.getString(idx));
        }

        // Add all items in the ArrayList into the view using an ArrayAdapter
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(
                    this.getActivity(),
                    R.layout.purchased_item,
                    R.id.item_title,
                    groceryList);
        } else {
            mAdapter.clear();
            mAdapter.addAll(groceryList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

}
