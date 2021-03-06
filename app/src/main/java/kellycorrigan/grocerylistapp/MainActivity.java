package kellycorrigan.grocerylistapp;

// Package sources:
// https://www.sitepoint.com/starting-android-development-creating-todo-app/
// http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/
// http://android-delight.blogspot.com/2015/12/tablelayout-like-listview-multi-column.html
// http://stackoverflow.com/questions/10811400/android-location-listener-or-android-events-in-general

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 8;
    private ItemDatabaseHelper mHelper;
    private ListView mItemListView;
    private ArrayAdapter<String> mAdapter;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment to delete database
        // Context context = getApplicationContext();
        // context.deleteDatabase("groceryItemsManager");

        mHelper = new ItemDatabaseHelper(this);
        mItemListView = (ListView) findViewById(R.id.grocery_list);

        locationSetup();

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
            case R.id.view_purchased_list:
                viewPurchasedItems();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // When the add item button is selected from the menu, create an alert dialog for the
    // user to enter the item
    private void addGroceryItem() {
        // Set up the view that the user will be entering the item into
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.new_item_entry, null);
        final EditText itemEditText = (EditText) view.findViewById(R.id.editTextDialogUserInput);

        // Set up the alert dialog to add a new item
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new grocery item")
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add the item into the database when the 'Add' button is clicked
                        String item = String.valueOf(itemEditText.getText());
                        if (item.length() > 0) {
                            mHelper.addGroceryItem(item);
                        }
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
        mHelper.removeGroceryItem(item);

        // Get the most recent location, if available
        String location;
        if (mLocation != null) {
            String latitude = String.format(Locale.US, "%.4f", mLocation.getLatitude());
            String longitude = String.format(Locale.US, "%.4f", mLocation.getLongitude());
            location = "(" + latitude + ", " + longitude + ")";
        } else {
            location = "location unavailable";
        }

        // Add the item into the purchased items table of the database
        mHelper.addPurchasedItem(item, location);

        // Uncheck the checkbox in the grocery list because it will be reused
        AppCompatCheckBox v = (AppCompatCheckBox) view;
        v.setChecked(false);

        // Update the UI
        updateUI();
    }

    // Update the view
    private void updateUI() {
        // Get an ArrayList of all items in the grocery list database table
        ArrayList<String> groceryList = mHelper.queryAllGroceryItems();

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
    }

    // Show the purchased items list if there are items to show; otherwise display an alert
    private void viewPurchasedItems() {
        ArrayList<PurchasedItem> purchasedItems = mHelper.queryAllPurchasedItems();
        if (purchasedItems.isEmpty()) {
            // Display an alert dialog to let the user know there are no purchased items
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Purchased items list is empty")
                    .setNeutralButton("Ok", null)
                    .create();
            dialog.show();
        } else {
            // Show the purchased items list
            Intent intent = new Intent(this, PurchasedItemsActivity.class);
            startActivity(intent);
        }
    }

    // Perform setup necessary to access device location
    private void locationSetup() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                mLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Request coarse location permission if we don't have it already
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }

        // If we have location permission, get the last known location and register the
        // listener with the Location Manager to receive location updates
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            Criteria locationCritera = new Criteria();
            String providerName = locationManager.getBestProvider(locationCritera,
                    true);
            if (providerName != null) {
                mLocation = locationManager.getLastKnownLocation(providerName);
            }

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    locationListener);
        }
    }
}

