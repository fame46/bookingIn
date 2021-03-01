package com.example.bookingin.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bookingin.Admin.AlatCampingFragment;
import com.example.bookingin.Admin.KameraFragment;
import com.example.bookingin.Admin.MobilFragment;
import com.example.bookingin.User.AlatCampingFragmentUser;
import com.example.bookingin.User.KameraFragmentUser;
import com.example.bookingin.User.MobilFragmentUser;

public class ViewPagerAdapterUser extends FragmentStatePagerAdapter {

    int tabCount;

    //Constructor to the class
    public ViewPagerAdapterUser(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MobilFragmentUser tab1 = new MobilFragmentUser();
                return tab1;
            case 1:
                KameraFragmentUser tab2 = new KameraFragmentUser();
                return tab2;
            case 2:
                AlatCampingFragmentUser tab3 = new AlatCampingFragmentUser();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
