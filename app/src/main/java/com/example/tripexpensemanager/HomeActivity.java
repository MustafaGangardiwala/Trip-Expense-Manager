package com.example.tripexpensemanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripexpensemanager.adapter.TripAdapter;
import com.example.tripexpensemanager.addactivity.AddNewTripDetails;
import com.example.tripexpensemanager.item.TripItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {
    public static int j;
    int count=0;
    FloatingActionButton fb;
    SQLiteDatabase db;
    List<TripItem> tripItemList;
    ListView listView;
    TripAdapter adapter;
    Vector<Integer> vec = new Vector<>();
    ArrayList<String> list_item = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ShowDataActivity();
        fb= findViewById(R.id.fab);
        OnfabClick();
    }

    void ShowDataActivity() {
        db = openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);

        tripItemList = new ArrayList<>();
        listView = findViewById(R.id.listviewTripInfo);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView temp = (TextView) view.findViewById(R.id.name);
                        String str = temp.getText().toString();
                        Intent intent = new Intent(HomeActivity.this, ViewTripDetails.class);
                        intent.putExtra("Id", str);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS Trip (Id INTEGER PRIMARY KEY AUTOINCREMENT ,Place TEXT NOT NULL, Date DATE NOT NULL, " + "MemberNo INTEGER NOT NULL DEFAULT 0);");
        Cursor cursor = db.rawQuery("SELECT * FROM Trip ORDER BY Date DESC;", null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {

                    String place = cursor.getString(cursor.getColumnIndex("Place"));
                    String date = cursor.getString(cursor.getColumnIndex("Date"));
                    tripItemList.add(new TripItem(place, date));
                    vec.add(1);
                    j++;
                } while (cursor.moveToNext());
        }

        adapter = new TripAdapter(getApplicationContext(), R.layout.listview_trip_info, tripItemList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView temp = (TextView) view.findViewById(R.id.name);
                        String str = temp.getText().toString();
                        Intent intent = new Intent(HomeActivity.this, ViewTripDetails.class);
                        intent.putExtra("Id", str);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if(vec.get(i)==0){
                    list_item.remove(tripItemList.get(i));
                    count -= 1;
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                    vec.set(i,1);
                    actionMode.setTitle(count + " Trip Selected");
                } else {
                    count += 1;
                    listView.getChildAt(i).setBackgroundColor(Color.LTGRAY);
                    vec.set(i,0);
                    actionMode.setTitle(count + " Trip Selected");
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
                        for (int i = vec.size() - 1; i > -1; i--) {
                            if (vec.get(i) == 0) {
                                String name = tripItemList.get(i).getName();
                                vec.set(i, 1);
                                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                                final String TABLE_NAME = "Trip";
                                db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE Place ='" + name + "';");
                                String tb1="Trip_"+name;
                                db.execSQL("DROP TABLE IF EXISTS'"+tb1+"';");
                                db.execSQL("DROP TABLE IF EXISTS'"+name+"';");
                                tripItemList.remove(i);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), count + "Trip Removed", Toast.LENGTH_SHORT).show();
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
        ////////////////////////////////////
    }

    public void OnfabClick(){
        fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(HomeActivity.this, AddNewTripDetails.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
}