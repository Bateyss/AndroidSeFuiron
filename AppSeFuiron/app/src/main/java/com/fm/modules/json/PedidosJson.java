package com.fm.modules.json;

import android.os.Build;
import android.util.Log;

import com.fm.modules.models.Pedido;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class PedidosJson implements AdaptersInterface<Pedido> {
    public List<Pedido> parser(String contenido) {
        List<Pedido> lista = new LinkedList<>();
        boolean isJsonObject = false;
        try {
            if (contenido != null && !"".equals(contenido)) {
                System.out.println(contenido);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = new JSONArray(contenido);
                        System.out.println("es json array");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject object = jsonArray.getJSONObject(x);
                            Pedido p = ASingleJsonAdapapters.pedidoAdapter(object);
                            lista.add(p);
                        }
                    } catch (Exception ex) {
                        System.out.println("error jsonParser: " + ex);
                        isJsonObject = true;
                    }
                    if (isJsonObject) {
                        System.out.println("es json object");
                        JSONObject object = new JSONObject(contenido);
                        Pedido p = ASingleJsonAdapapters.pedidoAdapter(object);
                        lista.add(p);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error " + e);
            Log.e("erro en :", getClass().getName());
            Log.e("error", "" + e);
        }
        return lista;
    }
}
