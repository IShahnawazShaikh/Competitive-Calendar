package com.shahnawazshaikh.competitive.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.adapter.MyPagerAdapter;
import com.shahnawazshaikh.competitive.adapter.MyPagerProfileAdapter;
import com.shahnawazshaikh.competitive.preferences.UserSession;

import java.util.HashMap;

import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_EMAIL;
import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_INSTITUTE;
import static com.shahnawazshaikh.competitive.preferences.UserSession.KEY_NAME;

public class Profile extends AppCompatActivity {
    private HashMap<String, String> userDetails;
    private UserSession userSession;
    TextView nameID,emailID,instituteID;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayouts);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#7d56b0"));
        final ViewPager viewPager = findViewById(R.id.pager);
        nameID = findViewById(R.id.name);
        emailID = findViewById(R.id.email);
        instituteID = findViewById(R.id.institute);
        logout = findViewById(R.id.logout);
        userSession = new UserSession(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Logged Out", Toast.LENGTH_SHORT).show();
                userSession.logoutUser();
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finishAffinity();
            }
        });

        final MyPagerProfileAdapter adapter = new MyPagerProfileAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.edit);
        tabLayout.getTabAt(1).setIcon(R.drawable.suggestion);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        setCurrentUser();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setCurrentUser() {

        userDetails = userSession.getUserDetails();
        nameID.setText(userDetails.get(KEY_NAME));
        emailID.setText(userDetails.get(KEY_EMAIL));
        instituteID.setText(userDetails.get(KEY_INSTITUTE));
    }
}