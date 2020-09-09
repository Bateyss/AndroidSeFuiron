package com.fm.modules.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.RestauranteMenuActivity;
import com.fm.modules.models.Platillo;
import com.fm.modules.service.PlatilloService;

import java.util.List;

public class PlansPagerAdapter extends FragmentPagerAdapter {
    int numofTabs;
    List<Platillo> platilloList;

    public PlansPagerAdapter(FragmentManager fm, int numofTabs){
        super(fm);
        this.numofTabs = numofTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return DynamicFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return numofTabs;
    }

    public static class  DynamicFragment extends Fragment{
        View view;

        public static DynamicFragment newInstance(int val){
            DynamicFragment fragment = new DynamicFragment();
            Bundle args = new Bundle();
            args.putInt("someInt", val);
            fragment.setArguments(args);
            return fragment;
        }

        PlatilloService platilloService = new PlatilloService();
        int val;
        TextView c;
        List<Platillo> lista = platilloService.obtenerPlatillos();

        ListView lvPlatillos;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.holder_vertical_recycler_view, container, false);
            val = getArguments().getInt("someInt", 0);
            lvPlatillos = view.findViewById(R.id.lvPlatillos);
            PlatillosItemViewAdapter adapter = new PlatillosItemViewAdapter(lista, getContext(), R.layout.frg_restaurant_menu);
            lvPlatillos.setAdapter(adapter);
            //c = view.findViewById(R.id.tvFoodName);
            //c.setText("" + val);
            return view;
        }
    }

}
