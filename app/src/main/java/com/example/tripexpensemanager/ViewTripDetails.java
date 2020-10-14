package com.example.tripexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tripexpensemanager.fragmentui.FragmentBalance;
import com.example.tripexpensemanager.fragmentui.FragmentExpense;

public class ViewTripDetails extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    String p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip_details);
        Intent in=getIntent();
        Bundle bundle=in.getExtras();
        assert bundle != null;
        p=bundle.getString("Id");
        setTitle(p);
        ViewPager vpPager = findViewById(R.id.viewPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Fragment f1=new FragmentExpense();
                    return FragmentExpense.newInstance(0, null);
                case 1:
                    Fragment f2=new FragmentBalance();
                    return FragmentBalance.newInstance(1,null);


                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0) return "Expense";
            else if(position==1) return "Balance";
            return null;
        }

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent in=new Intent(getApplicationContext(),HomeActivity.class);
        in.putExtra("Id",p);
        startActivity(in);
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

}