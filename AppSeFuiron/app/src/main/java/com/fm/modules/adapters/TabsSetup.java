package com.fm.modules.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fm.modules.models.Menu;

import java.util.List;

public class TabsSetup extends FragmentPagerAdapter {

    List<Menu> simpleTabsList;

    public TabsSetup(List<Menu> simpleTabsList, @NonNull FragmentManager fm) {
        super(fm);
        this.simpleTabsList = simpleTabsList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return simpleTabsList.size();
    }

    /*
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
        if(simpleTabsList.size() == 0){
            return null;
        }
        return simpleTabsList.get(position);
    }*/
}
