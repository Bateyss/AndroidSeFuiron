package com.fm.modules.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.app.restaurantes.PlaceholderFragment;
import com.fm.modules.models.Menu;

import java.util.ArrayList;
import java.util.List;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<Menu> list = new ArrayList<>();
    private Menu menuSelected;

    public SectionsPagerAdapter(List<Menu> list, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.list = list;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        System.out.println("menu position " + position);
        GlobalRestaurantes.menuSelectedInMenusTab = list.get(position);
        return new PlaceholderFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getNombreMenu();
    }

    @Override
    public int getCount() {
        // Show total pages.
        return list.size();
    }

}