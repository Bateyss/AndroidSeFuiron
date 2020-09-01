package com.fm.modules.api;

import com.fm.modules.json.AdaptersInterface;
import com.fm.modules.json.UsuariosJson;
import com.fm.modules.models.Usuario;

import java.util.LinkedList;
import java.util.List;

public abstract class AbsApiModel<T> {

    /*
     * esta clase abstracta permitira utilizar las librerias de RestTemplate Srping
     * para utilizar la tecnologias de rest client de spring
     */

    /*
     * el texto dominio es para colocar la url de dominio desde las clases donde se
     * utiliza la Clase RestUrlGet es una clase asbtracta que permite utilizar las
     * tecnologias de rest template y devuelve texto desde sus metodos el texto es
     * de tipo json to text
     */
    private String domain;
    private ApiGetRest<T> rest;
    private AdaptersInterface<T> adapter;

    private T clase;


    public AbsApiModel(Class<T> tt, String domain) {
        this.domain = domain;
        try {
            if (tt == Usuario.class) {
                adapter = (AdaptersInterface<T>) new UsuariosJson();
            }
        } catch (Exception e) {
            System.out.println("error al iniciar adapter en abs api");
            System.out.println("eee: " + e);
        }

    }

    // primero debemos obtener el enlace desde el metodo para la url
    public abstract String findAllURL();

    public List<T> findAll() {
        List<T> lista = new LinkedList<>();
        // aqui se unen el dominio y la url de enlace
        String url = domain + findAllURL();
        try {
            System.out.println(url);
            rest = new ApiGetRest<T>();
            // se reciven los datos con el rest template
            String datos = rest.simpleGET(url);
            // se convierten los datos
            lista = adapter.parser(datos);
            System.out.println("la lista tiene datos");
        } catch (Exception e) {
            System.out.println("error al consultar todo en: " + getClass().getName());
            // Log.e("error", "error al consultar todo en :" + getClass().getName());
            System.out.println("error :" + e);
        }
        return lista;
    }

    public abstract String findByIdURL();

    public T findById(Integer id) {
        String url = domain + findByIdURL() + "/" + id;
        /*
         * se crea un objeto T con el objeto iniciado T(clase) para poder recoger y
         * responder con los datos procesados
         */
        T cls = clase;
        try {
            rest = new ApiGetRest<T>();
            // se reciven los datos con el rest template
            String datos = rest.simpleGET(url);
            // se adaptan los datos a la clase T
            cls = adapter.parser(datos).get(0);
        } catch (Exception e) {
            System.out.println("error al consultar por id en: " + getClass().getName());
            // Log.e("error", "error al consultar por id en :" + getClass().getName());
            System.out.println("error :" + e);
            // si falla el adaptador, se reinicia el objeto T con T(clase)
            /*
             * lo mejor es no enviar un objeto null, para evitar problemas de reuso
             */
            cls = clase;
        }
        return cls;
    }

    public abstract String createURL();

    public boolean create(T bean) {
        boolean response = false;
        String url = domain + createURL();
        try {
            rest = new ApiGetRest<T>();
            // se reciven los datos con el rest template
            String datos = rest.simplePOST(bean, url);
            // se comprueba la existencia de datos
            // como garantia de la creacion del objeto
            if (datos != null && !datos.equals("")) {
                response = true;
            }
        } catch (Exception e) {
            System.out.println("error al crear registro en: " + getClass().getName());
            // Log.e("error", "error al crear registro en :" + getClass().getName());
            System.out.println("error :" + e);
        }
        return response;
    }

    public abstract String deleteURL();

    public boolean delete(Integer id) {
        boolean response = false;
        String url = domain + deleteURL() + "/" + id;
        try {
            rest = new ApiGetRest<T>();
            // se reciven los datos con el rest template
            String datos = rest.simpleDELETE(url);
            // se comprueba la existencia de datos
            // como garantia de la creacion del objeto
            if (datos != null && !datos.equals("")) {
            } else {
                response = true;
            }
        } catch (Exception e) {
            System.out.println("error al eliminar registro en: " + getClass().getName());
            // Log.e("error", "error al crear registro en :" + getClass().getName());
            System.out.println("error :" + e);
        }
        return response;
    }

    public abstract String updateURL();

    public boolean update(T bean, Integer id) {
        boolean response = false;
        String url = domain + updateURL() + "/" + id;
        try {
            rest = new ApiGetRest<T>();
            // se reciven los datos con el rest template
            String datos = rest.simplePUT(bean, url);
            // se comprueba la existencia de datos
            // como garantia de la creacion del objeto
            if (datos != null && !datos.equals("")) {
                response = true;
            }
        } catch (Exception e) {
            System.out.println("error al modificar registro en: " + getClass().getName());
            // Log.e("error", "error al crear registro en :" + getClass().getName());
            System.out.println("error :" + e);
        }
        return response;
    }

    public String getDomain() {
        return domain;
    }

}
