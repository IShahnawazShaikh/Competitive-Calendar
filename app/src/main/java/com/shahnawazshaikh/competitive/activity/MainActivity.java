package com.shahnawazshaikh.competitive.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.adapter.Adapter;
import com.shahnawazshaikh.competitive.fragment.All;
import com.shahnawazshaikh.competitive.models.ContestBean;
import com.shahnawazshaikh.competitive.pageradapter.MyPagerAdapter;
import com.shahnawazshaikh.competitive.view_models.FetchData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
       // ImageButton refresh = (ImageButton) findViewById(R.id.refresh);

        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Codeforces"));
        tabLayout.addTab(tabLayout.newTab().setText("Codechef"));
        tabLayout.addTab(tabLayout.newTab().setText("Atcoder"));
        tabLayout.addTab(tabLayout.newTab().setText("Leetcode"));
        tabLayout.addTab(tabLayout.newTab().setText("HackerRank"));
        tabLayout.addTab(tabLayout.newTab().setText("HackerEarth"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#7d56b0"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.Pager);

        viewPager.setOffscreenPageLimit(6);

        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //position=tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
               // tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#7d56b0"));


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });




    }

}
