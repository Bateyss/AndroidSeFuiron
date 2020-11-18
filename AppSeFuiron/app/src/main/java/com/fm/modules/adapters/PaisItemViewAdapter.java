package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.models.Departamento;
import com.fm.modules.models.Municipio;
import com.fm.modules.models.Pais;

import java.util.ArrayList;
import java.util.List;

public class PaisItemViewAdapter extends ItemViewAdapterImagen<Pais> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;
    private Spinner spinner;
    private Spinner spinnerDepartamentos;
    private Spinner spinnerMunicipio;


    public PaisItemViewAdapter(List<Pais> lista, Context context, int resource, Spinner spinner, Spinner spinnerDepartamentos, Spinner spinnerMunicipio) {
        super(lista, context);
        this.context = context;
        this.resource = resource;
        this.spinner = spinner;
        this.spinnerDepartamentos = spinnerDepartamentos;
        this.spinnerMunicipio = spinnerMunicipio;
    }

    @Override
    public View absView(final int position, View convertView, ViewGroup parent) {
        final HolderItemSpinner holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                holder = new HolderItemSpinner(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderItemSpinner) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Pais pais = (Pais) getItem(position);

            if (pais.getNombrePais() != null) {
                holder.itemOption.setText(pais.getNombrePais());
            } else {
                String ss = "none";
                holder.itemOption.setText(ss);
            }
            holder.itemOption.setTextColor(context.getResources().getColor(R.color.purple));
            holder.itemOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalCarrito.paisSelected = pais;
                    if (pais.getNombrePais() != null) {
                        GlobalCarrito.direccion10 = pais.getNombrePais();
                        spinner.setSelection(position);
                        List<Departamento> departamentos = new ArrayList<>();
                        if (GlobalCarrito.departamentoList != null && !GlobalCarrito.departamentoList.isEmpty()) {
                            for (Departamento departamento : GlobalCarrito.departamentoList) {
                                if (departamento.getPais() != null && departamento.getPais().getPaisId() != null) {
                                    if (departamento.getPais().getPaisId().intValue() == pais.getPaisId().intValue()) {
                                        departamentos.add(departamento);
                                    }
                                }
                            }
                        }
                        DepartamentoItemViewAdapter adapter = new DepartamentoItemViewAdapter(departamentos, context, android.R.layout.simple_spinner_dropdown_item, spinnerDepartamentos, spinnerMunicipio);
                        spinnerDepartamentos.setAdapter(adapter);
                        MunicipioItemViewAdapter municipioItemViewAdapterAdapter = new MunicipioItemViewAdapter(new ArrayList<Municipio>(), context, android.R.layout.simple_spinner_dropdown_item, spinnerMunicipio);
                        spinnerMunicipio.setAdapter(municipioItemViewAdapterAdapter);
                        GlobalCarrito.departamentoSelected = null;
                        GlobalCarrito.municipioSelected = null;
                        GlobalCarrito.direccion9 = null;
                        notifyDataSetChanged();
                    }
                }
            });
            if (pais.getNombrePais() != null && pais.getNombrePais().equals(GlobalCarrito.direccion10)) {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
            } else {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            if (GlobalCarrito.paisSelected == null && 0 == position) {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
                GlobalCarrito.paisSelected = pais;
                GlobalCarrito.direccion10 = pais.getNombrePais();
            }
        } catch (Exception ignore) {

        }
        return convertView;
    }
}
