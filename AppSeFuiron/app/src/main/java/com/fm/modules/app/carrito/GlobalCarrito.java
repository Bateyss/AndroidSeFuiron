package com.fm.modules.app.carrito;

import com.fm.modules.models.Departamento;
import com.fm.modules.models.Municipio;
import com.fm.modules.models.Pais;
import com.fm.modules.models.Pedido;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GlobalCarrito {
    public static LatLng latLngSeleccionada;
    public static String direccion1; // direccion
    public static String direccion2; // pasaje
    public static String direccion4; // numero casa
    public static String direccion5; // referencia
    public static String direccion6; // pais
    public static String direccion7; // departamento
    public static String direccion8; // municipio selected
    public static String direccion9; // departamento selected
    public static String direccion10; //pais selected
    public static List<Municipio> municipioList;
    public static List<Departamento> departamentoList;
    public static List<Pais> paisList;
    public static Municipio municipioSelected;
    public static Departamento departamentoSelected;
    public static Pais paisSelected;
    public static Pedido pedidoRegistrado;
    public static boolean toSales;
    public static boolean toComplementos;
    public static boolean toMenu;
    public static boolean toShopinCart;
    public static List<HabiliarAddMap> habilitarAdd;
    public static int NumeroPlatilloSeleccionadp;
    public static int NumeroComplementoSeleccionado;
    public static int NumeroPedido;
}
