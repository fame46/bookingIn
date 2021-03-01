package com.example.bookingin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String SP_ADMIN_APP = "spadmin";
    public static final String SP_NAMA = "spNama";
    public static final String SP_ID = "spId";
    public static final String SP_LEVELID = "spLevelid";
    public static final String SP_ALAMAT = "spAlamat";
    public static final String SP_NOMORHP = "spNomorHp";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public Preferences(Context context){
        sp = context.getSharedPreferences(SP_ADMIN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSpLevelId(){
        return sp.getString(SP_LEVELID, "");
    }

    public String getSpAlamat(){
        return sp.getString(SP_ALAMAT, "");
    }

    public String getSpNomorhp(){
        return sp.getString(SP_NOMORHP, "");
    }

    public String getSpId(){
        return sp.getString(SP_ID, "");
    }


    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
