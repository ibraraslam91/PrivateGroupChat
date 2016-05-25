package com.example.ibraraslam.privategroupchat.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.adapter.PageAdapter_LogActivity;
import com.example.ibraraslam.privategroupchat.fragment.SignInFragment;
import com.example.ibraraslam.privategroupchat.fragment.SignUpFragment;

public class MainActivity extends AppCompatActivity implements SignUpFragment.OnFragmentInteractionListener,SignInFragment.OnFragmentInteractionListener {
    PageAdapter_LogActivity pageAdapter;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabLayout();
    }

    public void initTabLayout(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Sign In"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pageAdapter = new PageAdapter_LogActivity(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


    @Override
    public void isSignInComplete() {
        this.finish();
    }

    @Override
    public void isSignupComplete() {
        this.finish();
    }
}
