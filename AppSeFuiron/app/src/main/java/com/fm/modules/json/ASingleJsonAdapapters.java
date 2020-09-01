package com.fm.modules.json;

import com.fm.modules.models.Administrador;
import com.fm.modules.models.Categoria;
import com.fm.modules.models.Departamento;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Menu;
import com.fm.modules.models.MenxCategoria;
import com.fm.modules.models.Municipio;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pais;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatilloFavorito;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.RestauranteDestacado;
import com.fm.modules.models.SubMenu;
import com.fm.modules.models.Usuario;

import org.json.JSONObject;

import java.sql.Date;

public class ASingleJsonAdapapters {

    public static Usuario usuariosAdapter(JSONObject object) {
        Usuario u = new Usuario();
        try {
            u.setUsuarioId(object.getInt("usuarioId"));
            u.setNombre(object.getString("nombre"));
            u.setUsername(object.getString("username"));
            u.setPassword(object.getString("password"));
            u.setHabilitado(object.getBoolean("habilitado"));
            u.setRegPago(object.getString("regPago"));
            u.setDireccion(object.getString("direccion"));
            u.setUltimoInicio(Date.valueOf(object.getString("ultimoInicio")));
            u.setFechaCreacion(Date.valueOf(object.getString("fechaCreacion")));
            u.setApellido(object.getString("apellido"));
            u.setCorreoElectronico(object.getString("correoElectronico"));
            u.setFechaDeMacimiento(Date.valueOf(object.getString("fechaDeMacimiento")));
            u.setCelular(object.getString("celular"));
            try {
                u.setImagenDePerfil((byte[]) object.get("imagenDePerfil"));
            } catch (Exception ex) {

            }
        } catch (Exception e) {
            u = new Usuario();
        }
        return u;
    }

    public static Administrador administradorAdapter(JSONObject object) {
        Administrador a = new Administrador();
        try {
            a.setAdministradorId(object.getInt("administradorId"));
            a.setUsername(object.getString("username"));
            a.setPassword(object.getString("password"));
            a.setUltimoInicio(Date.valueOf(object.getString("ultimoInicio")));
            a.setFechaCreacion(Date.valueOf(object.getString("fechaCreacion")));
        } catch (Exception e) {
            a = new Administrador();
        }
        return a;
    }

    public static Driver driverAdapter(JSONObject object) {
        Driver d = new Driver();
        try {
            d.setDriverId(object.getInt("driverId"));
            d.setUsername(object.getString("username"));
            d.setPassword(object.getString("password"));
            d.setNombreDriver(object.getString("nombreDriver"));
            d.setHabilitado(object.getBoolean("habilitado"));
            d.setHoraDeEntrada(object.getString("horaDeEntrada"));
            d.setHoraDeSalida(object.getString("horaDeSalida"));
            d.setFechaCreado(Date.valueOf(object.getString("fechaCreado")));
        } catch (Exception e) {
            d = new Driver();
        }
        return d;
    }

    public static Categoria categoriaAdapter(JSONObject object) {
        Categoria c = new Categoria();
        try {
            c.setCategoriaId(object.getInt("categoriaId"));
            c.setNombreCategoria(object.getString("nombreCategoria"));
        } catch (Exception e) {
            c = new Categoria();
        }
        return c;
    }

    public static Pais paisAdapter(JSONObject object) {
        Pais p = new Pais();
        try {
            p.setPaisId(object.getInt("paisId"));
            p.setNombrePais(object.getString("nombrePais"));
        } catch (Exception e) {
            p = new Pais();
        }
        return p;
    }

    public static Departamento departamentoAdapter(JSONObject object) {
        Departamento d = new Departamento();
        try {
            d.setDepartamentoId(object.getInt("departamentoId"));
            d.setNombreDepartamento(object.getString("nombreDepartamento"));
            d.setPais(paisAdapter(object.getJSONObject("pais")));
        } catch (Exception e) {
            d = new Departamento();
        }
        return d;
    }

    public static Municipio municipioAdapter(JSONObject object) {
        Municipio d = new Municipio();
        try {
            d.setMunicipioId(object.getInt("municipioId"));
            d.setDepartamento(departamentoAdapter(object.getJSONObject("departamento")));
            d.setNombreMunicipio(object.getString("nombreMunicipio"));
        } catch (Exception e) {
            d = new Municipio();
        }
        return d;
    }

    public static Restaurante restauranteAdapter(JSONObject object) {
        Restaurante d = new Restaurante();
        try {
            d.setRestautanteId(object.getLong("restautanteId"));
            d.setDepartamento(departamentoAdapter(object.getJSONObject("departamento")));
            d.setUsername(object.getString("username"));
            d.setPassword(object.getString("password"));
            d.setHorarioDeApertura(object.getString("horarioDeApertura"));
            d.setHorarioDeCierre(object.getString("horarioDeCierre"));
            d.setTiempoEstimadoDeEntrega(object.getString("tiempoEstimadoDeEntrega"));
            d.setDescuento(object.getDouble("descuento"));
            d.setRepresentante(object.getString("representante"));
            d.setNumeroDeContacto(object.getString("numeroDeContacto"));
            d.setComision(object.getDouble("comision"));
            d.setCargosExtra(object.getInt("cargosExtra"));
            try {
                d.setImagenDePortada((Byte[]) object.get("imagenDePortada"));
            } catch (Exception ex) {
            }
            try {
                d.setLogoDeRestaurante((Byte[]) object.get("logoDeRestaurante"));
            } catch (Exception ex) {
            }
            d.setNit(object.getString("nit"));
            d.setCorreo(object.getString("correo"));
        } catch (Exception e) {
            d = new Restaurante();
        }
        return d;
    }

    public static RestauranteDestacado restauranteDestacadoAdapter(JSONObject object) {
        RestauranteDestacado d = new RestauranteDestacado();
        try {
            d.setRestauranteDestacadoId(object.getLong("restauranteDestacadoId"));
            d.setRestaurantes(restauranteAdapter(object.getJSONObject("restaurante")));
            d.setNombre(object.getString("nombre"));
            try {
                d.setImagen((byte[]) object.get("imagen"));
            } catch (Exception ex) {
            }
            try {
                d.setLogo((byte[]) object.get("logo"));
            } catch (Exception ex) {
            }
            d.setMontoMinimo(object.getDouble("montoMinimo"));
            d.setOrden(object.getInt("orden"));
        } catch (Exception e) {
            d = new RestauranteDestacado();
        }
        return d;
    }

    public static Menu menuAdapter(JSONObject object) {
        Menu d = new Menu();
        try {
            d.setMenuId(object.getLong("menuId"));
            d.setRestaurante(restauranteAdapter(object.getJSONObject("restaurante")));
            d.setNombreMenu(object.getString("nombreMenu"));
            d.setOrden(object.getInt("orden"));
        } catch (Exception e) {
            d = new Menu();
        }
        return d;
    }

    public static MenxCategoria menxCategoriaAdapter(JSONObject object) {
        MenxCategoria d = new MenxCategoria();
        try {
            d.setMenuxcategoria_id(object.getLong("menuxcategoria_id"));
            d.setMenu(menuAdapter(object.getJSONObject("menu")));
            d.setCategoria(categoriaAdapter(object.getJSONObject("categoria")));
        } catch (Exception e) {
            d = new MenxCategoria();
        }
        return d;
    }

    public static Platillo platilloAdapter(JSONObject object) {
        Platillo d = new Platillo();
        try {
            d.setPlatilloId(object.getLong("platilloId"));
            d.setMenu(menuAdapter(object.getJSONObject("menu")));
            d.setNombre(object.getString("nombre"));
            d.setPrecioBase(object.getDouble("precioBase"));
            try {
                d.setImagen((byte[]) object.get("imagen"));
            } catch (Exception ex) {
            }
            d.setDescripcion(object.getString("descripcion"));
            d.setOrden(object.getInt("orden"));
        } catch (Exception e) {
            d = new Platillo();
        }
        return d;
    }

    public static Pedido pedidoAdapter(JSONObject object) {
        Pedido d = new Pedido();
        try {
            d.setPedidoId(object.getLong("pedidoId"));
            d.setRestaurantes(restauranteAdapter(object.getJSONObject("restaurante")));
            d.setUsuario(usuariosAdapter(object.getJSONObject("usuario")));
            d.setDriver(driverAdapter(object.getJSONObject("drivers")));
            d.setStatus(object.getInt("status"));
            d.setFormaDePago(object.getString("formaDePago"));
            d.setTotalDePedido(object.getDouble("totalDePedido"));
            d.setTotalEnRestautante(object.getDouble("totalEnRestautante"));
            d.setTotalDeCargosExtra(object.getDouble("totalDeCargosExtra"));
            d.setTotalEnRestautanteSinComision(object.getDouble("totalEnRestautanteSinComision"));
            d.setPedidoPagado(object.getBoolean("pedidoPagado"));
            d.setFechaOrdenado(Date.valueOf(object.getString("fechaOrdenado")));
            d.setTiempoPromedioEntrega(object.getString("tiempoPromedioEntrega"));
            d.setPedidoEntregado(object.getBoolean("pedidoEntregado"));
            d.setNotas(object.getString("notas"));
            d.setTiempoAdicional(object.getString("tiempoAdicional"));
            d.setDireccion(object.getString("direccion"));
            System.out.println("jeson parseado  ");
        } catch (Exception e) {
            System.out.println("error pedidoJson: " + e);
            d = new Pedido();
        }
        return d;
    }

    public static SubMenu subMenuAdapter(JSONObject object) {
        SubMenu d = new SubMenu();
        try {
            d.setSubMenuId(object.getLong("subMenuId"));
            d.setPlatillo(platilloAdapter(object.getJSONObject("platillo")));
            d.setTitulo(object.getString("titulo"));
            d.setMenuCobrado(object.getBoolean("menuCobrado"));
            d.setCobrarAPartirDe(object.getInt("cobrarAPartirDe"));
            d.setMaximoOpcionesAEscoger(object.getInt("maximoOpcionesAEscoger"));
            d.setMinimoOpcionesAEscoger(object.getInt("minimoOpcionesAEscoger"));

        } catch (Exception e) {
            d = new SubMenu();
        }
        return d;
    }

    public static OpcionesDeSubMenu opcionesDeSubMenuAdapter(JSONObject object) {
        OpcionesDeSubMenu d = new OpcionesDeSubMenu();
        try {
            d.setOpcionesDeSubmenuId(object.getLong("opcionesDeSubmenuId"));
            d.setSubMenu(subMenuAdapter(object.getJSONObject("subMenu")));
            d.setNombre(object.getString("nombre"));
            d.setPrecio(object.getDouble("precio"));
            d.setOrden(object.getInt("orden"));
            d.setSeleccionadoPorDefecto(object.getBoolean("seleccionadoPorDefecto"));

        } catch (Exception e) {
            d = new OpcionesDeSubMenu();
        }
        return d;
    }

    public static PlatilloSeleccionado platilloSeleccionadoAdapter(JSONObject object) {
        PlatilloSeleccionado d = new PlatilloSeleccionado();
        try {
            d.setPlatilloSeleccionadoId(object.getLong("platilloSeleccionadoId"));
            d.setPlatillos(platilloAdapter(object.getJSONObject("platillo")));
            d.setPedido(pedidoAdapter(object.getJSONObject("pedido")));
            d.setNombre(object.getString("nombre"));
            d.setPrecio(object.getDouble("precio"));
        } catch (Exception e) {
            d = new PlatilloSeleccionado();
        }
        return d;
    }


    public static OpcionesDeSubMenuSeleccionado opcionesDeSubMenuSeleccionadoAdapter(JSONObject object) {
        OpcionesDeSubMenuSeleccionado d = new OpcionesDeSubMenuSeleccionado();
        try {
            d.setOpcionesDeSubMenuSeleccionadoId(object.getLong("opcionesDeSubMenuSeleccionadoId"));
            d.setOpcionesDeSubMenu(opcionesDeSubMenuAdapter(object.getJSONObject("opcionesDeSubMenu")));
            d.setPlatilloSeleccionado(platilloSeleccionadoAdapter(object.getJSONObject("platilloSeleccionado")));
            d.setNombre(object.getString("nombre"));
        } catch (Exception e) {
            d = new OpcionesDeSubMenuSeleccionado();
        }
        return d;
    }

    public static PlatilloFavorito platilloFavoritoAdapter(JSONObject object) {
        PlatilloFavorito d = new PlatilloFavorito();
        try {
            d.setPlatilloFavoritoId(object.getLong("platilloFavoritoId"));
            d.setPlatillos(platilloAdapter(object.getJSONObject("platillo")));
            d.setUsuario(usuariosAdapter(object.getJSONObject("usuarios")));
        } catch (Exception e) {
            d = new PlatilloFavorito();
        }
        return d;
    }
}
