package kellycorrigan.grocerylistapp;

/**
 * Created by kellycorrigan on 3/5/17.
 */

public class PurchasedItem {
    private String mItem;
    private String mDate;

    public PurchasedItem() {
        mItem = "";
        mDate = "";
    }

    public PurchasedItem(String item, String date) {
        mItem = item;
        mDate = date;
    }

    public String getItem() {
        return mItem;
    }

    public String getDate() {
        return mDate;
    }

    public void setItem(String item) {
        mItem = item;
    }

    public void setDate(String date) {
        mDate = date;
    }
}
