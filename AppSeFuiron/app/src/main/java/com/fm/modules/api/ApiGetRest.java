package com.fm.modules.api;

import android.util.Log;

import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class ApiGetRest<T> {

    public String GET = "GET";
    public String POST = "POST";
    public String PUT = "PUT";
    public String DELETE = "DELETE";

    private String user = "user";
    private String password = "a491c3da-7cc0-4805-a3fc-f0ea7845d213";

    public HttpHeaders setBasicAuth(String username, String password) {
        String singleSign = username + ":" + password;
        byte[] encodeSign = Base64Utils.encode(singleSign.getBytes(Charset.forName("US-ASCII")));
                String heade = "Basic" + new String(encodeSign);
        HttpHeaders signeHeader = new HttpHeaders();
        signeHeader.add("Authorization", heade);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        return signeHeader;
    }

    public static String getData(String uri, String method) {
        String text = "";
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // URLConnection connection = url.openConnection();
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            int status = httpURLConnection.getResponseCode();
            switch (status) {
                default:
                    Log.e("HttpUrlManag", "error en la conex");
                    break;
                case 201:
                    Log.e("HttpUrlManag", "noSuchConex");
                    ;
                    break;
                case 200:
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    text = stringBuilder.toString();
                    bufferedReader.close();
                    break;
            }
            return text;
        } catch (Exception e) {
            System.out.println("error " + e);
            Log.e("ErrorHTTPManager", "" + e);
            e.getStackTrace();
            return null;
        }
    }

    public String simpleGET(String URL) {
        String response = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
             HttpHeaders headers = setBasicAuth(user, password);
             HttpEntity request = new HttpEntity(headers);
             ResponseEntity<String> responser = restTemplate.exchange(URL, HttpMethod.GET, request, String.class);
            response = responser.getBody();
        } catch (Exception e) {
            System.out.println("Error al obtener objeto en: " + getClass().getName());
            System.out.println("Error en: " + e);
            response = "";
        }
        return response;
    }

    public String simplePOST(T entity, String URL) {
        String response = "";

        try {
            // los headers enviaran los datos para modificar
            HttpHeaders headers = new HttpHeaders();
            // los datos se enviaran en formato json
            headers.setContentType(MediaType.APPLICATION_JSON);

            // aqui se colocan los datos a enviar, en este caso es una entity llena
            // los datos se recogen en los parametros del metodo
            // y se envian en formato json
            HttpEntity<T> entity2 = new HttpEntity<T>(entity, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> res = restTemplate.postForEntity(URL, entity2, String.class);
            response = res.getBody();
            System.out.println("simple post targeted!");
        } catch (Exception e) {
            System.out.println("Error al crear objeto en: " + getClass().getName());
            System.out.println("Error en: " + e);
            response = "";
        }
        return response;
    }

    public String simplePUT(T entity, String URL) {
        String response = "";

        try {
            // los headers enviaran los datos para modificar
            HttpHeaders headers = new HttpHeaders();
            // los datos se enviaran en formato json
            headers.setContentType(MediaType.APPLICATION_JSON);

            // aqui se colocan los datos a enviar, en este caso es una entity llena
            // los datos se recogen en los parametros del metodo
            // y se envian en formato json
            HttpEntity<T> entity2 = new HttpEntity<T>(entity, headers);

            RestTemplate restTemplate = new RestTemplate();
            // primero se ejectua el PUT
            // pero este no devuelve valores
            restTemplate.put(URL, entity2);
            // ahora buscamos el objeto actualizado y lo recivimos
            response = restTemplate.getForObject(URL, String.class);

        } catch (Exception e) {
            System.out.println("Error al modificar objeto en: " + getClass().getName());
            System.out.println("Error en: " + e);
            response = "";
        }
        return response;
    }

    public String simpleDELETE(String URL) {
        String response = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            // borramos el objeto
            restTemplate.delete(URL);
            // revisamos si el oobjeto fue eliminado
            response = restTemplate.getForObject(URL, String.class);
        } catch (Exception e) {
            System.out.println("Error al eliminar objeto en: " + getClass().getName());
            System.out.println("Error en: " + e);
            response = "";
        }
        return response;
    }
}


