package com.fm.modules.json;

import android.os.Build;
import android.util.Log;

import com.fm.modules.models.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class UsuariosJson implements AdaptersInterface<Usuario> {
    public List<Usuario> parser(String contenido) {
        List<Usuario> lista = new LinkedList<>();
        boolean isJsonObject = false;
        try {
            if (contenido != null && !"".equals(contenido)) {
                System.out.println(contenido);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = new JSONArray(contenido);
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject object = jsonArray.getJSONObject(x);
                            Usuario u = ASingleJsonAdapapters.usuariosAdapter(object);
                            lista.add(u);
                        }
                    } catch (Exception ex) {
                        isJsonObject = true;
                    }
                    if (isJsonObject) {
                        JSONObject object = new JSONObject(contenido);
                        Usuario u = ASingleJsonAdapapters.usuariosAdapter(object);
                        lista.add(u);
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
