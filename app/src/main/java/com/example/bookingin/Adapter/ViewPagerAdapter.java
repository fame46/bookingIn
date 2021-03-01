package com.example.bookingin.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bookingin.Admin.AlatCampingFragment;
import com.example.bookingin.Admin.KameraFragment;
import com.example.bookingin.Admin.MobilFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    //Constructor to the class
    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MobilFragment tab1 = new MobilFragment();
                return tab1;
            case 1:
                KameraFragment tab2 = new KameraFragment();
                return tab2;
            case 2:
                AlatCampingFragment tab3 = new AlatCampingFragment();
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
