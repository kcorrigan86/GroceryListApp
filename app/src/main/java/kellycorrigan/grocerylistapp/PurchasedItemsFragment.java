package kellycorrigan.grocerylistapp;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class PurchasedItemsFragment extends Fragment {

    private Cursor mCursor;
    private ItemDatabaseHelper mHelper;
    private ListViewAdapter mAdapter;
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

        // Create a new ItemDatabaseHelper
        mHelper = new ItemDatabaseHelper(this.getActivity());

        // Query the purchased items and obtain a cursor
        mCursor = mHelper.queryPurchasedItems();
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
        View view = inflater.inflate(R.layout.fragment_purchased_items, container, false);
        mItemListView = (ListView) view.findViewById(R.id.purchased_items_list);

        updateUI();

        return view;
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
        ArrayList<PurchasedItem> purchasedList = new ArrayList<PurchasedItem>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(mHelper.TABLE_PURCHASED_LIST,
                new String[]{mHelper.KEY_ITEM, mHelper.KEY_DATE, mHelper.KEY_LOCATION},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            PurchasedItem item = new PurchasedItem();
            item.setItem(cursor.getString(cursor.getColumnIndex(mHelper.KEY_ITEM)));
            item.setDate(cursor.getString(cursor.getColumnIndex(mHelper.KEY_DATE)));
            item.setLocation(cursor.getString(cursor.getColumnIndex(mHelper.KEY_LOCATION)));
            purchasedList.add(item);
        }

        // Add items from the ArrayList into the view using ListViewAdapter
        mAdapter = new ListViewAdapter(this.getActivity(), purchasedList);
        mItemListView.setAdapter(mAdapter);

        cursor.close();
        db.close();
    }

}
