package com.example.tripexpensemanager.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tripexpensemanager.R;
import com.example.tripexpensemanager.item.TripItem;

import java.util.List;

public class TripAdapter extends ArrayAdapter<TripItem> {
    private SparseBooleanArray mSelectedItems;
    private LayoutInflater inflater;
    private Context mContext;
    private List<TripItem> tripItemList;

    public TripAdapter (Context context, int resourceId, List<TripItem> tripItemList) {
        super(context, resourceId, tripItemList);
        mSelectedItems = new SparseBooleanArray();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.tripItemList = tripItemList;
    }

    private static class ViewHolder {
        TextView name;
        TextView date;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_trip_info, null);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.date = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(tripItemList.get(position).getName());
        holder.date.setText(tripItemList.get(position).getDate());
        return view;
    }

    @Override
    public void remove(TripItem tripItem) {
        tripItemList.remove(tripItem);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItems.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItems.put(position, value);
        else
            mSelectedItems.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItems;
    }
}
