package com.fm.modules.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fm.modules.R;
import com.fm.modules.models.Categoria;

import java.util.List;

public class CategoriasPagerAdapter extends FragmentStatePagerAdapter {

    private List<Categoria> categoriaList;
    private LayoutInflater layoutInflater;
    private int numTabs;

    public CategoriasPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Categoria> categorias) {
        super(fm, behavior);
        categoriaList = categorias;
        numTabs = categorias.size();
    }

    @Override
    public int getCount() {
        return categoriaList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final HolderItemCategorias holderItemCategorias;
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.holder_item_category_unit, container, false);
        try {
            holderItemCategorias = new HolderItemCategorias(view);
            view.setTag(holderItemCategorias);
            final Categoria categoria = categoriaList.get(position);
            holderItemCategorias.catImage.setImageResource(R.drawable.ic_flan);
            holderItemCategorias.catName.setText(categoria.getNombreCategoria());
            holderItemCategorias.catCountRestaurants.setText("");
        } catch (Exception e) {
        }
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    public class HolderItemCategorias {
        AppCompatImageView catImage;
        AppCompatTextView catName;
        AppCompatTextView catCountRestaurants;

        public HolderItemCategorias(View view) {
            catImage = view.findViewById(R.id.ivCategoryIcon1);
            catName = view.findViewById(R.id.tvCategoryName1);
            catCountRestaurants = view.findViewById(R.id.tvRestaurantsCount1);
        }
    }
}
