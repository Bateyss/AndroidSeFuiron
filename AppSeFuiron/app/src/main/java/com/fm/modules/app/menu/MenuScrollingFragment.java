package com.fm.modules.app.menu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerTabMenuAdapter;
import com.fm.modules.app.commons.utils.Generics;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.app.restaurantes.MenuDeRestauranteFragment;
import com.fm.modules.models.Menu;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.fm.modules.service.PlatilloService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class MenuScrollingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_scrolling, container, false);
    }

}