package com.fm.modules.app.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;


public class SupportSendFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_support_send, container, false);
        Button nextBtn = (Button) view.findViewById(R.id.supportSendNext);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RestaurantePorCategoria());
            }
        });
        return view;
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}