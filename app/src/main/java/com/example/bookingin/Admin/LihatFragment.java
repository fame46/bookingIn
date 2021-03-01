package com.example.bookingin.Admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookingin.Adapter.ViewPagerAdapter;
import com.example.bookingin.R;
import com.google.android.material.tabs.TabLayout;

public class LihatFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lihat, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tablayout_idLihat);

        tabLayout.addTab(tabLayout.newTab().setText("MOBIL"));
        tabLayout.addTab(tabLayout.newTab().setText("KAMERA"));
        tabLayout.addTab(tabLayout.newTab().setText("ALAT CAMPING"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager_idLihat);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);

        return v;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }
}