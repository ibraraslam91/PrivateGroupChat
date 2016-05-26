package com.example.ibraraslam.privategroupchat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.ibraraslam.privategroupchat.fragment.SignInFragment;
import com.example.ibraraslam.privategroupchat.fragment.SignUpFragment;


/**
 * Created by Ibrar Aslam on 12/31/2015.
 */
public class PageAdapter_LogActivity extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public SignInFragment SignIn;
    public SignUpFragment SignUp;

    public PageAdapter_LogActivity(FragmentManager fm, int NumOfTab) {

        super(fm);

        this.mNumOfTabs = NumOfTab;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SignIn = new SignInFragment();
                return SignIn;
            case 1:
                SignUp = new SignUpFragment();
                return SignUp;
            default:
                return null;

        }


    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
