package com.example.tripexpensemanager.addactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripexpensemanager.HomeActivity;
import com.example.tripexpensemanager.R;

import java.util.ArrayList;

public class AddNewTripDetails extends AppCompatActivity {

    private static Button b1;
    private static Button b2;
    public DatePicker dp1;
    SQLiteDatabase db1=null;
    EditText e1,e2;
    Editable d1,d2;
    TextView tv1;
    TextView edtxt;
    EditText f_add;
    EditText num;
    int check=0;
    ArrayList<String> fname= new ArrayList<String>();
    int size=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip_details);
        b1 = (Button) findViewById(R.id.adddataBtn);
        edtxt=(TextView) findViewById(R.id.dateIs);
        b2=(Button)findViewById(R.id.addMemberbtn);
        f_add=(EditText)findViewById(R.id.memberNameet);
        num=(EditText)findViewById(R.id.amountEt);
        dp1 =(DatePicker)findViewById(R.id.datePickerbox);
        int selectedYear = dp1.getYear();
        int selectedMonth = dp1.getMonth();
        int selectedDay =  dp1.getDayOfMonth();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        OnClickButtonSubmit();
        setTitle("New Trip");
        OnClickButtonAddFriend();
        dp1.init(
                dp1.getYear(), dp1.getMonth(), dp1.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        onDateChange(i,i1,i2);
                    }
                }
        );
    }

    public void onDateChange(int y, int m, int d){
        edtxt.setText(d+"/"+m+"/"+y);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AddNewTripDetails.this, HomeActivity.class));
        finish();
    }

    void OnClickButtonSubmit(){
        e1=(EditText)findViewById(R.id.paidinfoEt);
        e2=(EditText)findViewById(R.id.amountEt);
        tv1=(TextView) findViewById(R.id.dateIs);
        b1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db1 = openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
                        d1 = e1.getText();
                        d2 = e2.getText();
                        String dd = tv1.getText().toString();
                        String s1 = d1.toString().toUpperCase();
                        String s2 = d2.toString();
                        s1=s1.trim();
                        s2=s2.trim();

                        try {
                            db1.execSQL("CREATE TABLE IF NOT EXISTS Trip (Id INTEGER PRIMARY KEY AUTOINCREMENT ,Place TEXT NOT NULL, Date DATE NOT NULL," + " MemberNo INTEGER NOT NULL DEFAULT 0);");
                            String table = "Trip";
                            String COL_2 = "Place";
                            String COL_3 = "Date";
                            String COL_4="MemberNo";
                            String tripName;
                            if (s1.matches("") || s2.matches("") || dd.matches("") || Integer.parseInt(s2)==0) {
                                throw new ArithmeticException("Enter Again");
                            }
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(COL_2, d1.toString().toUpperCase());
                            contentValues.put(COL_3, dd);
                            contentValues.put(COL_4,Integer.parseInt(num.getText().toString()));
                            int x=Integer.parseInt(num.getText().toString());

                            try {
                                Cursor c = db1.rawQuery("SELECT * FROM Trip ORDER BY Date DESC;", null);
                                if (c != null) {
                                    int i = 0;
                                    if (c.moveToFirst()) {
                                        do {
                                            String compare = c.getString(c.getColumnIndex("Place"));
                                            if (compare.matches(e1.getText().toString().toUpperCase())) {
                                                throw new ArithmeticException("Exists");
                                            }
                                        } while (c.moveToNext());
                                    }
                                }
                                try {
                                    if (check == Integer.parseInt(num.getText().toString())) {
                                        long result = db1.insert(table, null, contentValues);
                                        if (result != -1) {
                                            Toast.makeText(getApplicationContext(), "Trip Added", Toast.LENGTH_SHORT).show();
                                            Intent lis = new Intent(getApplicationContext(), HomeActivity.class);
                                            startActivity(lis);
                                            finish();
                                        } else
                                            throw new ArithmeticException("Enter Again");

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Add More Members", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Enter Again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Trip Name Already Added", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Enter Again", Toast.LENGTH_LONG).show();
                        }
                    }

                }
        );
    }

    public void OnClickButtonAddFriend(){
        b2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String z = num.getText().toString().trim();
                            if (z.matches(""))
                                throw new ArithmeticException("Empty");
                            int x = Integer.parseInt(num.getText().toString());
                            if (check < x) {
                                db1 = openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
                                String TABLE_NAME;
                                e1 = (EditText) findViewById(R.id.paidinfoEt);
                                TABLE_NAME = "Trip_" + e1.getText().toString().toUpperCase();
                                try {
                                    Cursor c = db1.rawQuery("SELECT * FROM Trip ORDER BY Date DESC;", null);
                                    if (c != null) {
                                        int i = 0;
                                        if (c.moveToFirst()) {
                                            do {
                                                String compare = c.getString(c.getColumnIndex("Place"));
                                                if (compare.matches(e1.getText().toString().toUpperCase()))
                                                    throw new ArithmeticException("Exists");
                                            }while (c.moveToNext());
                                        }
                                    }
                                    db1.execSQL("create table if not exists " + TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT,Member TEXT,Amount INTEGER NOT NULL DEFAULT 0)");
                                    ContentValues contentValues = new ContentValues();
                                    String COL_2;
                                    COL_2 = "Member";
                                    try {
                                        String y = f_add.getText().toString();
                                        y=y.toUpperCase();
                                        int flag1=0;
                                        if (y.matches(""))
                                            throw new ArithmeticException("Enter Again");
                                        for(int i=0;i<fname.size();i++){
                                            if(y.matches(fname.get(i))){
                                                Toast.makeText(getApplicationContext(),y+"Exists",Toast.LENGTH_SHORT).show();
                                                flag1=1;
                                                break;
                                            }
                                        }
                                        if(flag1==0) {
                                            contentValues.put(COL_2, y);
                                            fname.add(y);
                                            size++;
                                            long result = db1.insert(TABLE_NAME, null, contentValues);
                                            check++;
                                        }
                                        f_add.setText("");
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Enter Member Name", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Trip Exists", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Limit Exceeded", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Enter Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}