package com.example.tripexpensemanager.fragmentui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.tripexpensemanager.R;
import com.example.tripexpensemanager.adapter.BalanceAdapter;
import com.example.tripexpensemanager.item.BalanceItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentBalance extends Fragment {
    private String title;
    private int page;
    ListView list_a;
    SQLiteDatabase db;
    String p;
    Integer n;
    ArrayList<String> list=new ArrayList<String>();
    ArrayList<BalanceItem> list_items=new ArrayList<>();
    public static FragmentBalance newInstance(int page, String title) {
        FragmentBalance fragmentBlance = new FragmentBalance();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentBlance.setArguments(args);
        return fragmentBlance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        list_a=(ListView)view.findViewById(R.id.balanceList);
        List<calculate> list = new ArrayList<>();
        db=getActivity().openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
        Intent in=getActivity().getIntent();
        Bundle bundle=in.getExtras();
        p=bundle.getString("Id");
        Cursor c=db.rawQuery("SELECT * FROM Trip ORDER BY Date DESC;",null);
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    String s=c.getString(c.getColumnIndex("Place"));
                    if(s.matches(p)){
                        n=c.getInt(c.getColumnIndex("MemberNo"));
                        break;
                    }

                }while(c.moveToNext());
            }
        }
        String table="Trip_"+p;
        int i=0;
        c=db.rawQuery("SELECT * FROM "+table+";",null);
        if(c!=null){
            if (c.moveToFirst()) {
                do{
                    calculate assignment1 = new calculate();
                    assignment1.name = c.getString(c.getColumnIndex("Member"));
                    assignment1.money = c.getDouble(c.getColumnIndex("Amount"));
                    list.add(assignment1);
                    i++;
                }while(c.moveToNext());
            }
        }



        Collections.sort(list, new Comparator<calculate>() {
            @Override
            public int compare(calculate fruit2, calculate fruit1)
            {
                return Double.compare(fruit2.money, fruit1.money);
            }
        });

        double avg=0.0;
        for(calculate p :list)
        {
            avg+=p.money;
        }
        avg=(1.0*avg)/n;
        DecimalFormat df = new DecimalFormat("####0.00");
        for(calculate p:list){
            p.money-=avg;
            String d1=p.name;
            String d2=df.format(p.money);
            list_items.add(new BalanceItem(d1,d2));
        }
        BalanceAdapter adapter = new BalanceAdapter(getActivity(), R.layout.balance_listview, list_items);
        list_a.setAdapter(adapter);
        return view;
    }
    public class calculate
    {
        String name;
        Double money;

    }
}