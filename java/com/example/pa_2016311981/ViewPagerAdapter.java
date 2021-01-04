package com.example.pa_2016311981;

import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int tabnum;
    private ArrayList<String> tabname = new ArrayList<String>();
    private ArrayList<PostItem> items = new ArrayList<PostItem>();
    private String username;

    public ViewPagerAdapter(FragmentManager fm, int tabnum, ArrayList<PostItem> items, String username){
        super(fm);
        this.tabnum = tabnum;
        this.items = items;
        this.username = username;
        tabname.add("Personal");
        tabname.add("Public");
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PersonalFrag personalFrag = new PersonalFrag(items, username);
                return personalFrag;
            case 1:
                PublicFrag publicFrag = new PublicFrag();
                return publicFrag;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabnum;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabname.get(position);
    }
}
