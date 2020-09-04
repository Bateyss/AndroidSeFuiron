package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fm.modules.R;
import com.fm.modules.app.restaurantes.RestauranteMenuActivity;
import com.fm.modules.models.Imagen;
import com.fm.modules.models.Restaurante;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RestauranteItemViewAdapter extends ItemViewAdapterImagen<Restaurante> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;

    private RequestQueue queue;

    public RestauranteItemViewAdapter(List<Restaurante> lista, Context context, int resource) {
        super(lista);
        this.context = context;
        this.resource = resource;

        queue = Volley.newRequestQueue(context);
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
                    Intent i = new Intent(context, RestauranteMenuActivity.class);
                    i.putExtra("idRestaurante", restaurante.getRestautanteId());
                    System.out.println("ID RESTAURANTE: " + restaurante.getRestautanteId());
                    context.startActivity(i);
                }
            });

        } catch (Exception e) {
            System.out.println("Error ListaAdapterRestaurante" + e);
        }
        return convertView;
    }

    private String obtenerImg() {

        String url = "http://192.168.1.2:9090/images";
        final String[] content = new String[1];
        final Imagen imagen = new Imagen();
        List<Imagen> img = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray mJsonArray = response.getJSONArray("[]");
                    for(int i=0; i < mJsonArray.length(); i++){

                        JSONObject mJSONObject = mJsonArray.getJSONObject(i);

                        imagen.setId(mJSONObject.optLong("id"));
                        imagen.setName(mJSONObject.optString("name"));
                        imagen.setContentType(mJSONObject.optString("contentType"));
                        imagen.setContent(mJSONObject.optString("content"));
                        content[0] = mJSONObject.optString("content");
                        //imgContent[0] = mJSONObject.getString("content");
                        //img.add(imagen);
                    }

                }catch (JSONException e){
                    System.out.println("Error en la consulta1: " +e.getMessage());
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error en la consulta: "+error.getClass() +" " +error.getMessage() );
            }
        });
        queue.add(request);
        return content[0];

    }

}

