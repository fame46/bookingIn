package com.example.bookingin.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.bookingin.LoginActivity;
import com.example.bookingin.R;
import com.example.bookingin.Utils.Preferences;
import com.google.android.material.navigation.NavigationView;

public class HomeAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout frameLayout;

    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        preferences = new Preferences(this);

        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("BookingIn");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        frameLayout = findViewById(R.id.framelayout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex){
        if (savedInstanceState == null){
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_booklist:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new PermintaanFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.nav_tambah:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new TambahFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.nav_Lihat:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new LihatFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.nav_laporan:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_id, new LaporanFragment())
                        .commit();
                closeDrawer();
                break;
            case R.id.nav_logout:
                preferences.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, false);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}