package com.example.tripexpensemanager.fragmentui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tripexpensemanager.addactivity.AddNewExpense;
import com.example.tripexpensemanager.R;
import com.example.tripexpensemanager.ViewTripDetails;
import com.example.tripexpensemanager.adapter.ExpenseAdapter;
import com.example.tripexpensemanager.item.ExpenseItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FragmentExpense extends Fragment {
    private String title;
    private int page;
    String p;
    private ListView listView;
    private ExpenseAdapter adapter;
    private List<ExpenseItem> expenseItemList;
    ArrayList<ExpenseItem> list_items= new ArrayList<>();
    Vector<Integer> vec =new Vector<>();
    int flag=0;
    SQLiteDatabase db;
    int count=0;
    FloatingActionButton f;
    public static FragmentExpense newInstance(int page, String title) {
        FragmentExpense fragmentExpense = new FragmentExpense();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentExpense.setArguments(args);
        return fragmentExpense;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_expense, container, false);
        db = getActivity().openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
        Intent in = getActivity().getIntent();
        Bundle bundle = in.getExtras();
        p = bundle.getString("Id");
        f = (FloatingActionButton) view.findViewById(R.id.fab_expense);
        f.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddNewExpense.class);
                        intent.putExtra("Id", p);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
        );


        listView = (ListView) view.findViewById(R.id.expenseInfoLv);
        expenseItemList = new ArrayList<>();
        int x=0;
        db.execSQL("CREATE TABLE IF NOT EXISTS " + p + " (Id INTEGER PRIMARY KEY AUTOINCREMENT,Member TEXT,Note TEXT,Amount TEXT)");
        Cursor cursor = db.rawQuery("SELECT * FROM " + p + ";", null);
        if (cursor != null) {
            int i = 0;
            if (cursor.moveToFirst()) {
                do {
                    String d1 = cursor.getString(cursor.getColumnIndex("Member"));
                    String d2 = cursor.getString(cursor.getColumnIndex("Note"));
                    String d3 = cursor.getString(cursor.getColumnIndex("Amount"));
                    vec.add(1);
                    expenseItemList.add(new ExpenseItem(d1,d2,d3));
                } while (cursor.moveToNext());
            }
        }
        try {
            adapter =new ExpenseAdapter(getActivity(),R.layout.listview_expense_info,expenseItemList);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if(vec.get(i)==0){
                    list_items.remove(expenseItemList.get(i));
                    count -= 1;
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                    vec.set(i,1);
                    actionMode.setTitle(count + " Items Selected");
                } else {
                    count += 1;
                    listView.getChildAt(i).setBackgroundColor(Color.LTGRAY);
                    vec.set(i,0);
                    actionMode.setTitle(count + " Items Selected");
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_item:
                        for (int i=vec.size()-1;i>-1;i--) {
                            if (vec.get(i) == 0) {
                                TextView t = (TextView) view.findViewById(R.id.nameInfo);
                                String name=expenseItemList.get(i).getName();
                                String amount=expenseItemList.get(i).getAmount();
                                String note=expenseItemList.get(i).getNote();
                                vec.set(i,1);
                                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                                final String TABLE_NAME = p;
                                final String NOTE = "Note";
                                final String MEMBER = "Member";


                                db.delete(TABLE_NAME,MEMBER + " = ? AND " + NOTE + " = ?", new String[] {name, note + ""});
                                String tb="Trip_"+p;
                                Cursor cursor1 = db.rawQuery("SELECT * FROM " + tb + ";", null);
                                if (cursor1 != null) {
                                    if (cursor1.moveToFirst()) {
                                        do {
                                            String ch = cursor1.getString(cursor1.getColumnIndex("Member"));
                                            Log.i("ababababbabababababa",ch);
                                            if (ch.matches(name)) {
                                                String temp = cursor1.getString(cursor1.getColumnIndex("Amount"));
                                                int index = cursor1.getInt(cursor1.getColumnIndex("Id"));
                                                int money1 = Integer.parseInt(temp);
                                                int money2 = Integer.parseInt(amount);
                                                int money= money1 - money2;
                                                ContentValues cv=new ContentValues();
                                                cv.put("Amount",money);
                                                cv.put("Member",name);
                                                db.update(tb, cv,"Id = "+index, null);
//                                                temp =cursor1.getString(cursor1.getColumnIndex("Amount"));
                                                break;
                                            }

                                        } while (cursor1.moveToNext());
                                    }
                                }
                                expenseItemList.remove(i);
                            }
                            adapter.notifyDataSetChanged();
                            flag=1;
                            start();
                        }
                        Toast.makeText(getActivity(), count + " Item Removed ", Toast.LENGTH_SHORT).show();
                        count = 0;
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        return view;
    }
    public void start(){
        Intent same=new Intent(getActivity(), ViewTripDetails.class);
        same.putExtra("Id",p);
        startActivity(same);
        getActivity().finish();
    }
}