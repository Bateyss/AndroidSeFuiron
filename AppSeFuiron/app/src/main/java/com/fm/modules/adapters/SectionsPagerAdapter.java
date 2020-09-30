package com.fm.modules.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fm.modules.app.menu.MenuTabActivity;
import com.fm.modules.models.Menu;

import java.util.ArrayList;
import java.util.List;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Menu> list = new ArrayList<>();
    private List<Fragment> tabs;
    private Menu menuSelected;

    public SectionsPagerAdapter(List<Menu> list, List<Fragment> tabs, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.list = list;
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getNombreMenu();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

}