package com.fm.modules.service;

import com.fm.modules.models.SubMenu;

import java.io.Serializable;
import java.util.List;

public class SubMenuService extends RestTemplateEntity<SubMenu> implements Serializable {

    public SubMenuService() {
        super(new SubMenu(), SubMenu.class, SubMenu[].class);
    }

    private static final long serialVersionUID = 1L;
    private final String url = Constantes.URL_SUBMENU;

    public List<SubMenu> obtenerSubMenus() {
        List<SubMenu> lista = getListURL(url);
        return lista;
    }

    public SubMenu obtenerSubMenuPorId(Long id) {
        SubMenu enti = getOneURL(url, id);
        return enti;
    }

    public SubMenu obtenerSubMenuPorBody(SubMenu objeto) {
        SubMenu enti = getByBodyURL(url, objeto);
        return enti;
    }

    public SubMenu crearSubMenu(SubMenu objeto) {
        SubMenu enti = createURL(url, objeto);
        return enti;
    }

    public SubMenu actualizarSubMenuPorId(SubMenu objeto) {
        SubMenu enti = updateURL(url, objeto.getSubMenuId(), objeto);
        return enti;
    }

    public void eliminarSubMenuPorId(Long id) {
        deleteURL(url, id);
    }

}
