package com.example.tripexpensemanager;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.Deflater;

public class FragmentExpense extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    String p;
    private ListView nlist;
    private ExpenseAdapter adapter;
    private List<TripItem> niitemlist;
    ArrayList<TripItem> list_items= new ArrayList<>();
    Vector<Integer> vec =new Vector<>();

    int flag=0;
    SQLiteDatabase db=null;


    Button btn;
    int count=0;
    FloatingActionButton f;

    // newInstance constructor for creating fragment with arguments
    public static FragmentExpense newInstance(int page, String title) {
        FragmentExpense fragmentFirst = new FragmentExpense();
        Bundle args = new Bundle();
        Log.e("fragment show","1111111111111");
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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


        nlist = (ListView) view.findViewById(R.id.list_view);
        niitemlist = new ArrayList<>();
        int x=0;
        db.execSQL("create table if not exists " + p + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Member TEXT,Note TEXT,Amount TEXT)");
        Cursor c = db.rawQuery("SELECT * FROM " + p + ";", null);
        if (c != null) {
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    //create list view from table place_name
                    String d2 = c.getString(c.getColumnIndex("Member"));
                    String d1 = c.getString(c.getColumnIndex("Note"));
                    String d4 = c.getString(c.getColumnIndex("Amount"));
                    vec.add(1);
                    niitemlist.add(new TripItem(d1,d4,d2));

                } while (c.moveToNext());
            }
        }
        try {
            //adapter=new adapter_Show(Fragment_show.this,niitemlist);
            adapter =new ExpenseAdapter(getActivity(),R.layout.expenselistview,niitemlist);

            nlist.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }

        nlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        nlist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if(vec.get(i)==0){
                    list_items.remove(niitemlist.get(i));
                    count -= 1;
                    nlist.getChildAt(i).setBackgroundColor(Color.WHITE);

                    vec.set(i,1);
                    String ii=Integer.toString(i);
                    Log.e("not select",ii);

                    actionMode.setTitle(count + " items selected");
                } else {
                    count += 1;
                    nlist.getChildAt(i).setBackgroundColor(Color.LTGRAY);

                    vec.set(i,0);
                    String ii=Integer.toString(i);
                    Log.e("select",ii);
                    actionMode.setTitle(count + " items selected");
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
                Log.e("1","enter switch");

                switch (menuItem.getItemId()) {
                    case R.id.delete_item:
                        for (int i=vec.size()-1;i>-1;i--) {
                            if (vec.get(i) == 0) {
                                TextView t = (TextView) view.findViewById(R.id.name);
                                String name=niitemlist.get(i).getName();
                                String amount=niitemlist.get(i).getAmount();
                                String note=niitemlist.get(i).getNote();

                                vec.set(i,1);
                                nlist.getChildAt(i).setBackgroundColor(Color.WHITE);
                                final String TABLE_NAME =p;
                                final String NOTE="Note";
                                final String FRIEND = "Member";
                                Log.e("table name",TABLE_NAME);


                                db.delete(TABLE_NAME,
                                        FRIEND + " = ? AND " + NOTE + " = ?",
                                        new String[] {name, note+""});
                                //update fpune
                                String tb="Trip_"+p;
                                Log.e("sdhjbs",name);
                                Cursor c3 = db.rawQuery("SELECT * FROM " + tb + ";", null);
                                if (c3 != null) {
                                    if (c3.moveToFirst()) {
                                        do {
                                            String ch = c3.getString(c3.getColumnIndex("Member"));
                                            Log.e("friend name",ch);
                                            if (ch.matches(name)) {
                                                String temp = c3.getString(c3.getColumnIndex("Amount"));
                                                int index = c3.getInt(c3.getColumnIndex("Id"));
                                                int money1 = Integer.parseInt(temp);
                                                int money2 = Integer.parseInt(amount);
                                                int money= money1 - money2;
                                                ContentValues cv=new ContentValues();
                                                cv.put("Amount",money);
                                                cv.put("Member",name);
                                                db.update(tb,cv, "Id="+index, null);
                                                temp =c3.getString(c3.getColumnIndex("Amount"));
                                                break;
                                            }

                                        } while (c3.moveToNext());
                                    }
                                }

                                niitemlist.remove(i);
                            }
                            adapter.notifyDataSetChanged();
                            flag=1;
                            start();
                        }
                        Toast.makeText(getActivity(), count + " items removed ", Toast.LENGTH_SHORT).show();
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