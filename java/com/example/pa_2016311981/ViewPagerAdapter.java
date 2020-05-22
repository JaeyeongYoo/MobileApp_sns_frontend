package com.example.pa_2016311981;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int tabnum;
    private ArrayList<String> tabname = new ArrayList<String>();
    public ViewPagerAdapter(FragmentManager fm, int tabnum){
        super(fm);
        this.tabnum = tabnum;
        tabname.add("Personal");
        tabname.add("Public");
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PersonalFrag personalFrag = new PersonalFrag();
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
