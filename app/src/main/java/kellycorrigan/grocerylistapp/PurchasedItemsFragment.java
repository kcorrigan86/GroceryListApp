package kellycorrigan.grocerylistapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class PurchasedItemsFragment extends Fragment {

    private ItemDatabaseHelper mHelper;
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
    }

    @Override
    public void onDestroy() {
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

    // Update the view
    private void updateUI() {
        // Get an ArrayList of all PurchasedItems in the database
        ArrayList<PurchasedItem> purchasedList = mHelper.queryAllPurchasedItems();

        // Add items from the ArrayList into the view using ListViewAdapter
        ListViewAdapter adapter = new ListViewAdapter(this.getActivity(), purchasedList);
        mItemListView.setAdapter(adapter);
    }
}
