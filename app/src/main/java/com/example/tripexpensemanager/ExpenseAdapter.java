package com.example.tripexpensemanager;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<TripItem> {
    private SparseBooleanArray mSelectedItemsIds;
    private LayoutInflater inflater;
    private Context mContext;
    private List<TripItem> list;

    public ExpenseAdapter (Context context, int resourceId, List<TripItem> list) {
        super(context, resourceId, list);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.list = list;
    }

    private static class ViewHolder {
        TextView name;
        TextView note;
        TextView amount;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.expenselistview, null);
            holder.name = view.findViewById(R.id.nameInfo);
            holder.note = view.findViewById(R.id.note);
            holder.amount = view.findViewById(R.id.amount);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.note.setText(list.get(position).getNote());
        holder.amount.setText(list.get(position).getAmount());
        holder.name.setText(list.get(position).getName());
        return view;
    }

    @Override
    public void remove(TripItem remitm) {
        list.remove(remitm);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
