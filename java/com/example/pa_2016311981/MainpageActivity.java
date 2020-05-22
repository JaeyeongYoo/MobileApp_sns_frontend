package com.example.pa_2016311981;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainpageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    ImageButton imageButton;
    int tab_num = 0;
    String ID, PW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        PW = intent.getStringExtra("PW");
        tab_num = intent.getIntExtra("Tab", 0);

        Toolbar tb = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, tb, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), 2);
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(tab_num);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);



        imageButton = (ImageButton)findViewById(R.id.plusbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_num = viewPager.getCurrentItem();
                Intent intent = new Intent(MainpageActivity.this, NewpostActivity.class);
                intent.putExtra("Tab", tab_num);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MainpageActivity.this, LoginActivity.class);
        intent.putExtra("ID", ID);
        intent.putExtra("PW", PW);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:{
                break;
            }
            case R.id.item2:
                break;
            case R.id.item3:
                break;
        }
        return false;
    }

}
