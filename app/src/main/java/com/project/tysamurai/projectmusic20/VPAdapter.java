package com.project.tysamurai.projectmusic20;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by tanay on 28/4/17.
 */

public class VPAdapter extends FragmentPagerAdapter {


    ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    ArrayList<String> fragmentTilteArr=new ArrayList<>();
    public VPAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFrag(Fragment fragment,String fragmentTitle){

        fragmentArrayList.add(fragment);
        fragmentTilteArr.add(fragmentTitle);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTilteArr.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
}
