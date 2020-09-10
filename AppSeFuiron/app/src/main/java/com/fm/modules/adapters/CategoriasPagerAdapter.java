package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;

import com.fm.modules.R;
import com.fm.modules.models.Categoria;

import java.util.List;

public class CategoriasPagerAdapter extends PagerAdapter {

    private List<Categoria> categoriaList;
    private LayoutInflater layoutInflater;
    private Context context;

    public CategoriasPagerAdapter(List<Categoria> categoriaList, Context context) {
        this.categoriaList = categoriaList;
        this.context = context;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return categoriaList.size();
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
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
        container.addView(view,0);
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
