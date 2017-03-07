package kellycorrigan.grocerylistapp;

public class PurchasedItem {
    private String mItem;
    private String mDate;
    private String mLocation;

    public PurchasedItem() {
        mItem = "";
        mDate = "";
        mLocation = "";
    }

    public PurchasedItem(String item, String date, String location) {
        mItem = item;
        mDate = date;
        mLocation = location;
    }

    public String getItem() {
        return mItem;
    }

    public String getDate() {
        return mDate;
    }

    public String getlocation() {
        return mLocation;
    }

    public void setItem(String item) {
        mItem = item;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}
