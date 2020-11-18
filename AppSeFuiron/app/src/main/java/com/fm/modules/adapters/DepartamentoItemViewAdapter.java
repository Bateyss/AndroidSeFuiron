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

import java.util.ArrayList;
import java.util.List;

public class DepartamentoItemViewAdapter extends ItemViewAdapterImagen<Departamento> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;
    private Spinner spinner;
    private Spinner spinnerMunicipio;


    public DepartamentoItemViewAdapter(List<Departamento> lista, Context context, int resource,Spinner spinner,Spinner spinnerMunicipio) {
        super(lista, context);
        this.context = context;
        this.resource = resource;
        this.spinner = spinner;
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

            final Departamento departamento = (Departamento) getItem(position);

            if (departamento.getNombreDepartamento() != null) {
                holder.itemOption.setText(departamento.getNombreDepartamento());
            } else {
                String ss = "none";
                holder.itemOption.setText(ss);
            }
            holder.itemOption.setTextColor(context.getResources().getColor(R.color.purple));
            holder.itemOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalCarrito.departamentoSelected = departamento;
                    if (departamento.getNombreDepartamento() != null) {
                        GlobalCarrito.direccion9 = departamento.getNombreDepartamento();
                        spinner.setSelection(position);
                        List<Municipio> municipios = new ArrayList<>();
                        if (GlobalCarrito.municipioList != null && !GlobalCarrito.municipioList.isEmpty()) {
                            for (Municipio municipio : GlobalCarrito.municipioList) {
                                if (municipio.getDepartamento() != null && municipio.getDepartamento().getDepartamentoId() != null) {
                                    if (municipio.getDepartamento().getDepartamentoId().intValue() == departamento.getDepartamentoId().intValue()) {
                                        municipios.add(municipio);
                                    }
                                }
                            }
                        }
                        MunicipioItemViewAdapter adapter = new MunicipioItemViewAdapter(municipios, context, android.R.layout.simple_spinner_dropdown_item, spinnerMunicipio);
                        spinnerMunicipio.setAdapter(adapter);
                        GlobalCarrito.municipioSelected = null;
                        GlobalCarrito.direccion8 = null;
                        notifyDataSetChanged();
                    }
                }
            });
            if (departamento.getNombreDepartamento() != null && departamento.getNombreDepartamento().equals(GlobalCarrito.direccion9)) {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
            } else {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            if (GlobalCarrito.departamentoSelected == null && 0 == position) {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
                GlobalCarrito.departamentoSelected = departamento;
                GlobalCarrito.direccion9 = departamento.getNombreDepartamento();
            }
        } catch (Exception ignore) {

        }
        return convertView;
    }
}
