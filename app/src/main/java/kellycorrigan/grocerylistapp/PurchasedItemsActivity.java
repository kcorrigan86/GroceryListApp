package kellycorrigan.grocerylistapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PurchasedItemsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PurchasedItemsFragment.newInstance();
    }
}
