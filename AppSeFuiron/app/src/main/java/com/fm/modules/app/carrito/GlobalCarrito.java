package com.fm.modules.app.carrito;

import com.fm.modules.models.Municipio;
import com.fm.modules.models.Pedido;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GlobalCarrito {
    public static LatLng latLngSeleccionada;
    public static String direccion1;
    public static String direccion2;
    public static String direccion4;
    public static String direccion5;
    public static String direccion6;
    public static String direccion7;
    public static String direccion8;
    public static List<Municipio> municipioList;
    public static Municipio municipioSelected;
    public static Pedido pedidoRegistrado;
    public static boolean toSales;
    public static boolean toComplementos;
    public static boolean toMenu;
    public static boolean toShopinCart;
}
