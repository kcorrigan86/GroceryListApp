package kellycorrigan.grocerylistapp;

class PurchasedItem {
    private String mItem;
    private String mDate;
    private String mLocation;

    PurchasedItem() {
        mItem = "";
        mDate = "";
        mLocation = "";
    }

    String getItem() {
        return mItem;
    }

    String getDate() {
        return mDate;
    }

    String getLocation() {
        return mLocation;
    }

    void setItem(String item) {
        mItem = item;
    }

    void setDate(String date) {
        mDate = date;
    }

    void setLocation(String location) {
        mLocation = location;
    }
}
