package com.fm.modules.json;

import android.os.Build;
import android.util.Log;

import com.fm.modules.models.Administrador;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class AdministradorJson implements AdaptersInterface<Administrador> {
    public List<Administrador> parser(String contenido) {
        List<Administrador> lista = new LinkedList<>();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                JSONArray jsonArray = new JSONArray(contenido);
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject object = jsonArray.getJSONObject(x);
                    Administrador u = ASingleJsonAdapapters.administradorAdapter(object);
                    lista.add(u);
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
