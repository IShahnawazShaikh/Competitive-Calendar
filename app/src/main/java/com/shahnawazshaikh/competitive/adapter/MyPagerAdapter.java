package com.shahnawazshaikh.competitive.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shahnawazshaikh.competitive.fragment.All;
import com.shahnawazshaikh.competitive.fragment.Atcoder;
import com.shahnawazshaikh.competitive.fragment.CodeForces;
import com.shahnawazshaikh.competitive.fragment.Codechef;
import com.shahnawazshaikh.competitive.fragment.HackerEarth;
import com.shahnawazshaikh.competitive.fragment.TopCoder;
import com.shahnawazshaikh.competitive.fragment.Leetcode;

public class MyPagerAdapter extends FragmentPagerAdapter {
    int tabNo;
    public MyPagerAdapter(FragmentManager fm, int tabNo){
        super(fm);
        this.tabNo=tabNo;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                All all=new All();
                return all;
            case 1:
                CodeForces cf=new CodeForces();
                return cf;
            case 2:
                Codechef cc= new Codechef();
                return cc;
            case 3:
                Atcoder atcoder= new Atcoder();
                return atcoder;
            case 4:
                Leetcode leetcode= new Leetcode();
                return leetcode;
            case 5:
                TopCoder topCoder = new TopCoder();
                return topCoder;
            case 6:
                HackerEarth hackerEarth= new HackerEarth();
                return hackerEarth;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabNo;
    }
}
