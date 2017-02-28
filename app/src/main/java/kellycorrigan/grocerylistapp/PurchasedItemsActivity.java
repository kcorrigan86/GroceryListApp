package kellycorrigan.grocerylistapp;

import android.app.Fragment;

public class PurchasedItemsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PurchasedItemsFragment.newInstance();
    }
}
