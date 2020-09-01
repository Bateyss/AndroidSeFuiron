package com.fm.modules.api;


import com.fm.modules.json.AdaptersInterface;
import com.fm.modules.json.PedidosJson;
import com.fm.modules.models.Pedido;

import java.util.LinkedList;
import java.util.List;

public class PedidoApi extends AbsApiModel<Pedido> implements AbstracInterfApi<Pedido> {

    private ApiGetRest<Pedido> restApi;
    private AdaptersInterface<Pedido> adapterJson;
    String middle = "/pedido";


    public PedidoApi(String domain) {
        super(Pedido.class, domain);
        adapterJson = new PedidosJson();
    }

    @Override
    public String findAllURL() {
        return middle;
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


    public List<Pedido> findPendientes() {
        String url = getDomain() + middle;
        /*
         * se crea un objeto T con el objeto iniciado T(clase) para poder recoger y
         * responder con los datos procesados
         */
        List<Pedido> list = new LinkedList<>();
        try {
            restApi = new ApiGetRest<Pedido>();
            // se reciven los datos con el rest template
            String datos = restApi.simpleGET(url);
            // se adaptan los datos a la clase T
            if (!adapterJson.parser(datos).isEmpty()) {
                list = adapterJson.parser(datos);
            }
        } catch (Exception e) {
            System.out.println("error al consultar por id en: " + getClass().getName());
            // Log.e("error", "error al consultar por id en :" + getClass().getName());
            System.out.println("error :" + e);
            // si falla el adaptador, se reinicia el objeto T con T(clase)
            /*
             * lo mejor es no enviar un objeto null, para evitar problemas de reuso
             */
        }
        return list;
    }

}
