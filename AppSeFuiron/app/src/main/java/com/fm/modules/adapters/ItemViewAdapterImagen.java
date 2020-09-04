package com.fm.modules.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;

public abstract class ItemViewAdapterImagen<T> extends BaseAdapter {

    private List<T> lista = new LinkedList<>();

    public List<T> getLista() {
        return lista;
    }


    public ItemViewAdapterImagen(List<T> lista) {
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return getLista().size();
    }

    @Override
    public Object getItem(int position) {
        return getLista().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return absView(position, convertView, parent);
    }

    public abstract View absView(int position, View convertView, ViewGroup parent);

    public class HolderImagen {

        TextView title;
        TextView description;
        ImageView imageView;

        public HolderImagen(View view) {
            title = (TextView) view.findViewById(R.id.itemTitlei);
            description = (TextView) view.findViewById(R.id.itemTexti);
            imageView = (ImageView) view.findViewById(R.id.itemImage);
        }
    }

    public class HolderTexto {
        TextView title;
        TextView description;

        public HolderTexto(View view) {
            title = (TextView) view.findViewById(R.id.itemTitle);
            description = (TextView) view.findViewById(R.id.itemText);
        }
    }

    public class HolderRestaurantes{
        AppCompatImageView ivOutstandingImage;
        AppCompatImageView ivRestaurantLogo;
        TextView tvRestaurantName;
        TextView tvMinimalMount;
        TextView tvLabelMinimalMount;

        public HolderRestaurantes(View view){
            ivOutstandingImage = (AppCompatImageView) view.findViewById(R.id.ivOutstandingImage);
            ivRestaurantLogo = (AppCompatImageView) view.findViewById(R.id.ivRestaurantLogo);
            tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantName);
            tvMinimalMount = (TextView) view.findViewById(R.id.tvMinimalMount);
            tvLabelMinimalMount = (TextView) view.findViewById(R.id.tvLabelMinimalMount);
        }
    }

    public class HolderItemFood{
        AppCompatImageView ivFoodImage;
        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        Button btnAdd;

        public HolderItemFood(View view){
            ivFoodImage = view.findViewById(R.id.ivFoodImage);
            tvFoodName = view.findViewById(R.id.tvFoodName);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = view.findViewById(R.id.tvFoodPrice);
            btnAdd = view.findViewById(R.id.btnAdd);
        }
    }

    public class HolderMenu{

        public CardView cvMenuName;
        public TextView tvMenuName;

        public HolderMenu(View view) {
            cvMenuName = view.findViewById(R.id.cvMenuName);
            tvMenuName = view.findViewById(R.id.tvMenuName);
        }
    }

}
