package com.example.ibraraslam.privategroupchat.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ibraraslam.privategroupchat.R;
import com.example.ibraraslam.privategroupchat.adapter.PageAdapter_LogActivity;
import com.example.ibraraslam.privategroupchat.fragment.SignInFragment;
import com.example.ibraraslam.privategroupchat.fragment.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements SignUpFragment.OnFragmentInteractionListener,SignInFragment.OnFragmentInteractionListener {
    PageAdapter_LogActivity pageAdapter;
    ViewPager viewPager;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("TAG","Auth status change");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d("TAG","user log in " +user.getUid());
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    intent.putExtra("userID",user.getUid());
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        };

        initTabLayout();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
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
