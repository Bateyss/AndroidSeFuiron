package com.fm.modules.api;


import com.fm.modules.json.AdaptersInterface;
import com.fm.modules.json.UsuariosJson;
import com.fm.modules.models.Usuario;

public class UsuarioApi extends AbsApiModel<Usuario> implements AbstracInterfApi<Usuario> {

    private ApiGetRest<Usuario> restApi;
    private AdaptersInterface<Usuario> adapterJson;
    String middle = "/usuarios";


    public UsuarioApi(String domain) {
        super(Usuario.class, domain);
        adapterJson = new UsuariosJson();
    }

    @Override
    public String findAllURL() {
        return middle + "/get";
    }

    @Override
    public String findByIdURL() {
        return middle;
    }

    @Override
    public String createURL() {
        return middle;
    }

    @Override
    public String deleteURL() {
        return middle;
    }

    @Override
    public String updateURL() {
        return middle;
    }

    public Usuario findByUserAndPass(String user, String pass) {
        String url = getDomain() + middle + "/" + "in";
        /*
         * se crea un objeto T con el objeto iniciado T(clase) para poder recoger y
         * responder con los datos procesados
         */
        Usuario u = new Usuario();
        try {
            restApi = new ApiGetRest<Usuario>();
            u.setUsername(user);
            u.setPassword(pass);
            u.setUsuarioId(0);
            // se reciven los datos con el rest template
            String datos = restApi.simplePOST(u, url);
            // se adaptan los datos a la clase T
            if (!adapterJson.parser(datos).isEmpty()) {
                u = adapterJson.parser(datos).get(0);
            } else {
                u = null;
            }

        } catch (Exception e) {
            System.out.println("error al consultar por id en: " + getClass().getName());
            // Log.e("error", "error al consultar por id en :" + getClass().getName());
            System.out.println("error :" + e);
            // si falla el adaptador, se reinicia el objeto T con T(clase)
            /*
             * lo mejor es no enviar un objeto null, para evitar problemas de reuso
             */
            u = null;
        }
        return u;
    }

}
