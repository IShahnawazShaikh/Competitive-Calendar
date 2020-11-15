package com.shahnawazshaikh.competitive.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shahnawazshaikh.competitive.fragment.Edit;
import com.shahnawazshaikh.competitive.fragment.Suggestion;

public class MyPagerProfileAdapter extends FragmentPagerAdapter {
    int tabNo;
    public MyPagerProfileAdapter(FragmentManager fm, int tabNo){
        super(fm);
        this.tabNo=tabNo;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                Edit edit=new Edit();
                return edit;
            case 1:
                Suggestion suggestion=new Suggestion();
                return suggestion;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabNo;
    }
}
