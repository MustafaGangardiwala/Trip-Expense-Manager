package com.example.tripexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class AddNewExpense extends AppCompatActivity {

    Spinner spBusinessType;
    ArrayAdapter<String> adapterBusinessType;
    ArrayList<String> names = new ArrayList<String>();
    int flag=0;
    EditText title,amount;
    Button btn_add;
    String p;
    SQLiteDatabase db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expense);
        db=openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
        Intent in=getIntent();
        Bundle bundle=in.getExtras();
        p=bundle.getString("Id");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // onBackPressed();
        setTitle("New expense");
        final String sp = "Trip_" + p;
        Cursor c=db.rawQuery("SELECT * FROM "+sp+";",null);
        if(c!=null) {
            int i=0;
            if(c.moveToFirst()){
                do{
                    String d4=c.getString(c.getColumnIndex("Member"));
                    names.add(d4);
                    i++;
                }while(c.moveToNext());
            }
        }
        spBusinessType = (Spinner) findViewById(R.id.spBussinessType);
        adapterBusinessType = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        adapterBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spBusinessType.setAdapter(adapterBusinessType);
        title=(EditText)findViewById(R.id.editTitle);
        amount=(EditText)findViewById(R.id.editAmount);
        btn_add=(Button)findViewById(R.id.button_ok);
        OnClickButtonAdd();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(), ViewTripDetails.class);
        intent.putExtra("Id",p);
        startActivity(intent);
        finish();

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnClickButtonAdd(){
        final String s="Trip_"+p;
        btn_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.execSQL("create table if not exists " + p+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,Member TEXT,Note TEXT,Amount TEXT)");
                        String COL_2="Member";
                        String COL_3="Note";
                        String COL_4="Amount";
                        String ti=title.getText().toString();
                        String am=amount.getText().toString();
                        String n = spBusinessType.getSelectedItem().toString();
                        String table=p;

                        //EXCEPTION
                        try {
                            if (ti.matches("") || am.matches(""))
                                throw new ArithmeticException("string");

                            //check if the note entered is previously entered in the table
                            Cursor c1 = db.rawQuery("SELECT * FROM '" + p + "';", null);
                            if (c1 != null) {
                                Log.e("c1!=null", "ppppppppppp");
                                if (c1.moveToFirst()) {
                                    do {
                                        if (ti.matches(c1.getString(c1.getColumnIndex("Note"))) && n.matches(c1.getString(c1.getColumnIndex("Member")))) {
                                            //update the value
                                            int a = Integer.parseInt(c1.getString(c1.getColumnIndex("Amount")));
                                            a += Integer.parseInt(am);
                                            String t = String.valueOf(a);
                                            int index=c1.getInt(c1.getColumnIndex("ID"));
                                            db.execSQL("UPDATE '" + p + "' SET Amount = '" + a + "' WHERE ID ='" + index + "';");
                                            flag = 1;
                                            Log.e(ti, String.valueOf(a));
                                            break;
                                        }
                                    } while (c1.moveToNext());
                                }
                            }


                            if (flag == 0) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(COL_2, n);
                                contentValues.put(COL_3, ti);
                                contentValues.put(COL_4, am);
                                long result = db.insert(table, null, contentValues);
                                if (result != -1) {
                                    //do
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"error data inserting",Toast.LENGTH_SHORT).show();
                                }
                            }
                            flag = 0;

                            //update

                            String tb = "Trip_" + p;
                            Cursor c = db.rawQuery("SELECT * FROM  " + tb + " ;", null);
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {

                                        String ch = c.getString(c.getColumnIndex("friend"));
                                        if (ch.matches(n)) {
                                            String temp = c.getString(c.getColumnIndex("Amount"));
                                            int index = c.getInt(c.getColumnIndex("ID"));
                                            int money1 = Integer.parseInt(temp);
                                            int money2 = Integer.parseInt(am);
                                            int money = money1 + money2;
                                            Log.e("money1",String.valueOf(money1));
                                            Log.e("money2",String.valueOf(money2));
                                            Log.e("money",String.valueOf(money));

                                            db.execSQL("UPDATE '" + tb + "' SET Amount = '" + money + "' WHERE ID ='" + index + "';");

                                            int x=c.getInt(c.getColumnIndex("Amount"));
                                            Log.e("updated amount:::::::::",String.valueOf(x));
                                            c.moveToLast();
                                        }
                                    } while (c.moveToNext());
                                }
                            }


                            //update
                            Toast.makeText(getApplicationContext(), "trip details updated!", Toast.LENGTH_SHORT).show();
                            Intent newscreen = new Intent(getApplicationContext(), ViewTripDetails.class);
                            newscreen.putExtra("Id", p);
                            startActivity(newscreen);
                            finish();
                        }catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"EMPTY FIELDS",Toast.LENGTH_SHORT).show();
                        }

                    }

                }

        );
        flag=0;
    }
}