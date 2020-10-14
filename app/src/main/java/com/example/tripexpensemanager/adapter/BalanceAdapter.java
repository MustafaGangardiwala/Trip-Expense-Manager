package com.example.tripexpensemanager.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tripexpensemanager.R;
import com.example.tripexpensemanager.item.BalanceItem;

import java.util.List;







public class BalanceAdapter extends ArrayAdapter<BalanceItem> {
    private SparseBooleanArray mSelectedItemsIds;
    private LayoutInflater inflater;
    private Context mContext;
    private List<BalanceItem> list;

    public BalanceAdapter(Context context, int resourceId, List<BalanceItem> list) {
        super(context, resourceId, list);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.list = list;
    }

    private static class ViewHolder {
        TextView name;
        TextView amount;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.balance_listview, null);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.amount = (TextView) view.findViewById(R.id.amount);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.amount.setText(list.get(position).getMoney());
        holder.name.setText(list.get(position).getName());
        return view;
    }

    @Override
    public void remove(BalanceItem remitm) {
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

