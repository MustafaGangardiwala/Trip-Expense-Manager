package com.example.tripexpensemanager.fragmentui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tripexpensemanager.R;

public class FragmentBalance extends Fragment {
    private String title;
    private int page;
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
        return view;
    }
}