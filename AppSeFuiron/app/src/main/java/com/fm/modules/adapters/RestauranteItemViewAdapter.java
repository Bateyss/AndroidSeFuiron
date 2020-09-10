package com.fm.modules.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fm.modules.R;
import com.fm.modules.models.Restaurante;

import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class RestauranteItemViewAdapter extends ItemViewAdapterImagen<Restaurante> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;

    public RestauranteItemViewAdapter(List<Restaurante> lista, Context context, int resource) {
        super(lista);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderRestaurantes holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_item_restaurant, parent, false);
                holder = new HolderRestaurantes(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderRestaurantes) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Restaurante restaurante = (Restaurante) getItem(position);

            //Byte[] bitmapdata = restaurante.getImagenDePortada(); // let this be your byte array
            //Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);


            //holder.ivOutstandingImage.setImageBitmap(restaurante.getLogoDeRestaurante());

            // Esto es de prueba
            //System.out.println("Array: " +restaurante.getImagenDePortada());
            Bitmap imagenP;
            //ImageService imageService = new ImageService();
            //List<Imagen> list = imageService.obtenerImages();
            //int img = list.get(0).getContent().length;
            //System.out.println("JSON IMG: " +img);
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //Imagen imagen = obtenerImg();
            //byte[] byteCode = Base64.decode(obtenerImg(), Base64.DEFAULT);
            //imagenP = BitmapFactory.decodeByteArray(byteCode, 0, byteCode.length);


            holder.ivOutstandingImage.setImageResource(R.drawable.not_found);
            holder.tvRestaurantName.setText(restaurante.getNombreRestaurante());
            holder.tvLabelMinimalMount.setText(restaurante.getDepartamento().getNombreDepartamento());
            holder.tvMinimalMount.setText(String.valueOf(restaurante.getDepartamento().getPais().getNombrePais()));
            holder.ivOutstandingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent i = new Intent(context, RestauranteMenuActivity.class);
                    //i.putExtra("idRestaurante", restaurante.getRestautanteId());
                    //System.out.println("ID RESTAURANTE: " + restaurante.getRestautanteId());
                    //context.startActivity(i);
                }
            });

        } catch (Exception e) {
            System.out.println("Error ListaAdapterRestaurante" + e);
        }
        return convertView;
    }

}

