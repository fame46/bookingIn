package com.example.bookingin.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookingin.R;
import com.example.bookingin.Utils.Preferences;

public class ProfileFragment extends Fragment {

    TextView tvUsername, tvNomorHP, tvAlamat;

    Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        preferences = new Preferences(getContext());

        tvUsername = v.findViewById(R.id.tvUsername);
        tvNomorHP = v.findViewById(R.id.tvNoHP);
        tvAlamat = v.findViewById(R.id.tvAlamat);

        tvUsername.setText(preferences.getSPNama());
        tvNomorHP.setText(preferences.getSpNomorhp());
        tvAlamat.setText(preferences.getSpAlamat());

        return v;
    }
}