package kellycorrigan.grocerylistapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kellycorrigan on 3/5/17.
 */

public class ListViewAdapter extends BaseAdapter {
    public ArrayList<PurchasedItem> mPurchasedItemList;
    Activity activity;

    public ListViewAdapter(Activity activity, ArrayList<PurchasedItem> purchasedItemList) {
        super();
        this.activity = activity;
        this.mPurchasedItemList = purchasedItemList;
    }

    @Override
    public int getCount() {
        return mPurchasedItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPurchasedItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mItem;
        TextView mDate;
        TextView mLocation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.purchased_item, null);
            holder = new ViewHolder();
            holder.mItem = (TextView) convertView.findViewById(R.id.purchased_item_desc);
            holder.mDate = (TextView) convertView.findViewById(R.id.item_date);
            holder.mLocation = (TextView) convertView.findViewById(R.id.item_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PurchasedItem item = mPurchasedItemList.get(position);
        holder.mItem.setText(item.getItem());
        holder.mDate.setText(item.getDate());
        holder.mLocation.setText(item.getlocation());

        return convertView;
    }
}
