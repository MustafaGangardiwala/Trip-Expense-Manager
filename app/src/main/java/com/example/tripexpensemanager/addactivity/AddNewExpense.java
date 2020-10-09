package com.example.tripexpensemanager.addactivity;

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

import com.example.tripexpensemanager.R;
import com.example.tripexpensemanager.ViewTripDetails;

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
        setTitle("New expense");
        p=bundle.getString("Id");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final String sp = "Trip_" + p;
        Cursor cursor=db.rawQuery("SELECT * FROM "+sp+";",null);
        if(cursor!=null) {
            int i=0;
            if(cursor.moveToFirst()){
                do{
                    String d4=cursor.getString(cursor.getColumnIndex("Member"));
                    names.add(d4);
                    i++;
                }while(cursor.moveToNext());
            }
        }
        spBusinessType = (Spinner) findViewById(R.id.nameSpinner);
        adapterBusinessType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapterBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBusinessType.setAdapter(adapterBusinessType);
        title=(EditText)findViewById(R.id.paidinfoEt);
        amount=(EditText)findViewById(R.id.amountEt);
        btn_add=(Button)findViewById(R.id.adddataBtn);
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
        final String sp="Trip_"+p;
        btn_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.execSQL("CREATE TABLE IF NOT EXISTS " + p +" (Id INTEGER PRIMARY KEY AUTOINCREMENT,Member TEXT,Note TEXT,Amount TEXT)");
                        String COL_2="Member";
                        String COL_3="Note";
                        String COL_4="Amount";
                        String ti=title.getText().toString();
                        String am=amount.getText().toString();
                        String n = spBusinessType.getSelectedItem().toString();
                        String table=p;
                        try {
                            if (ti.matches("") || am.matches(""))
                                throw new ArithmeticException("Mismatched");


                            Cursor cursor = db.rawQuery("SELECT * FROM '" + p + "';", null);
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    do {
                                        if (ti.matches(cursor.getString(cursor.getColumnIndex("Note"))) && n.matches(cursor.getString(cursor.getColumnIndex("Member")))) {
                                            int a = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Amount")));
                                            a += Integer.parseInt(am);
                                            String t = String.valueOf(a);
                                            int index=cursor.getInt(cursor.getColumnIndex("Id"));
                                            db.execSQL("UPDATE '" + p + "' SET Amount = '" + a + "' WHERE Id ='" + index + "';");
                                            flag = 1;
                                            break;
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                            if (flag == 0) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(COL_2, n);
                                contentValues.put(COL_3, ti);
                                contentValues.put(COL_4, am);
                                long result = db.insert(table, null, contentValues);
                                if (result != -1) {
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                }
                            }

                            flag = 0;
                            String tb = "Trip_" + p;
                            Cursor c = db.rawQuery("SELECT * FROM  " + tb + " ;", null);
                            if (c != null) {
                                if (c.moveToFirst()) {
                                    do {
                                        String ch = c.getString(c.getColumnIndex("Member"));
                                        if (ch.matches(n)) {
                                            String temp = c.getString(c.getColumnIndex("Amount"));
                                            int index = c.getInt(c.getColumnIndex("Id"));
                                            int mon1 = Integer.parseInt(temp);
                                            int mon2 = Integer.parseInt(am);
                                            int mon = mon1 + mon2;
                                            db.execSQL("UPDATE '" + tb + "' SET Amount = '" + mon + "' WHERE Id ='" + index + "';");
                                            int x=c.getInt(c.getColumnIndex("Amount"));
                                            c.moveToLast();
                                        }
                                    } while (c.moveToNext());
                                }
                            }

                            Toast.makeText(getApplicationContext(), "Trip Details Updated!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ViewTripDetails.class);
                            intent.putExtra("Id", p);
                            startActivity(intent);
                            finish();
                        }catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"Empty Fields",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
        );
        flag=0;
    }
}