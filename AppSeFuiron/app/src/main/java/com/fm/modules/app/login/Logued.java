package com.fm.modules.app.login;

import android.graphics.Bitmap;

import com.fm.modules.models.Image;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.Usuario;

import java.util.List;

public class Logued {

    public static Usuario usuarioLogued;
    public static List<PlatilloSeleccionado> platillosSeleccionadosActuales;
    public static List<Pedido> pedidosActuales;
    public static List<OpcionesDeSubMenuSeleccionado> opcionesDeSubMenusEnPlatillosSeleccionados;
    public static Bitmap imagenPerfil;
    public static List<Image> imagenes;
    public static List<Integer> imagenesIDs;
}
