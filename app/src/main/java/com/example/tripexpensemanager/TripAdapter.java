package com.example.tripexpensemanager;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TripAdapter extends ArrayAdapter<TripItem> {
    private SparseBooleanArray mSelectedItems;
    private LayoutInflater inflater;
    private Context mContext;
    private List<TripItem> list;

    public TripAdapter(Context context, int resourceId, List<TripItem> list) {
        super(context, resourceId, list);
        mSelectedItems = new SparseBooleanArray();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.list = list;
    }

    private static class ViewHolder {
        TextView date;
        TextView name;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_info, null);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.date = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.date.setText(list.get(position).getDate());
        holder.name.setText(list.get(position).getName());
        return view;
    }

    @Override
    public void remove(TripItem remitm) {
        list.remove(remitm);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, mSelectedItems.get(position));
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
