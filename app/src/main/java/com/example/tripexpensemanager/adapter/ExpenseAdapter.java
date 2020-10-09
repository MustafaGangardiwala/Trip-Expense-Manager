package com.example.tripexpensemanager.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tripexpensemanager.R;
import com.example.tripexpensemanager.item.ExpenseItem;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<ExpenseItem> {
    private SparseBooleanArray mSelectedItemsIds;
    private LayoutInflater inflater;
    private Context mContext;
    private List<ExpenseItem> expenseItemList;

    public ExpenseAdapter (Context context, int resourceId, List<ExpenseItem> expenseItemList) {
        super(context, resourceId, expenseItemList);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.expenseItemList = expenseItemList;
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
            view = inflater.inflate(R.layout.listview_expense_info, null);
            holder.name = view.findViewById(R.id.nameInfo);
            holder.note = view.findViewById(R.id.paymentInfo);
            holder.amount = view.findViewById(R.id.amountPaid);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(expenseItemList.get(position).getName());
        holder.note.setText(expenseItemList.get(position).getNote());
        holder.amount.setText(expenseItemList.get(position).getAmount());
        return view;
    }

    @Override
    public void remove(ExpenseItem expenseItem) {
        expenseItemList.remove(expenseItem);
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
