package com.fm.modules.sqlite.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.service.OpcionSubMenuService;

import java.util.List;

public class RecyclerTarjetasAdapter extends RecyclerView.Adapter<RecyclerTarjetasAdapter.ViewHolder> {

    private Context context;
    private List<TarjetasSaved> tarjetas;
    private FragmentActivity fragmentActivity;

    private OpcionSubMenuService opcionesSubMenuService = new OpcionSubMenuService();

    public RecyclerTarjetasAdapter(List<TarjetasSaved> tarjetas, Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.tarjetas = tarjetas;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerTarjetasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_tarjetas, parent, false);
        return new RecyclerTarjetasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerTarjetasAdapter.ViewHolder holder, final int position) {
        holder.optionIcon.setImageResource(R.drawable.ic_targeta);
        if (tarjetas.get(position).getNumTarjeta() != null) {
            holder.numero.setText(tarjetas.get(position).getNumTarjeta());
        } else {
            String clasified = "Clasified";
            holder.numero.setText(clasified);
        }
        if (tarjetas.get(position).getUsuario() != null && tarjetas.get(position).getUsuario().getNombre() != null) {
            holder.nombre.setText(tarjetas.get(position).getUsuario().getNombre());
        } else {
            String clasified = "Clasified";
            holder.nombre.setText(clasified);
        }
        holder.itemOptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("carviex seleccionado !!!!!!!!!!!!!!!! **");
                Logued.idTarjetaSeleccionada = (long) Integer.parseInt(tarjetas.get(position).getTarjUsuario_id());
                System.out.println("Seleccionado id tarjeta: " + Logued.idTarjetaSeleccionada);
                System.out.println("carviex seleccionado no dio error !!!!!!!!!!!!!!!! **");
                notifyDataSetChanged();
            }
        });
        if (Logued.idTarjetaSeleccionada != null && Logued.idTarjetaSeleccionada.intValue() == Integer.parseInt(tarjetas.get(position).getTarjUsuario_id())) {
            holder.pagoBtnTarjetaCheck.setImageResource(R.drawable.ic_checked_round_lime);
        } else {
            holder.pagoBtnTarjetaCheck.setImageResource(R.drawable.ic_checked_round_white);
        }
        int x = 0;
        try {
            x = Integer.parseInt(tarjetas.get(position).getTipo());
        } catch (Exception ignore) {
        }
        try {
            if (x == 1010) {
                holder.optionIcon.setImageResource(R.drawable.visa_dos);
            }
            if (x == 1020) {
                holder.optionIcon.setImageResource(R.drawable.mastercard);
            }
            if (x == 1030) {
                holder.optionIcon.setImageResource(R.drawable.ic_american_express);
            }
        } catch (Exception ignore) {
        }
    }

    @Override
    public int getItemCount() {
        return tarjetas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView numero;
        AppCompatTextView nombre;
        ImageView pagoBtnTarjetaCheck;
        ConstraintLayout itemOptionLayout;
        AppCompatImageView optionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numero = (AppCompatTextView) itemView.findViewById(R.id.tvOptionNumber);
            nombre = (AppCompatTextView) itemView.findViewById(R.id.tvOptionName);
            itemOptionLayout = (ConstraintLayout) itemView.findViewById(R.id.itemOptionLayout);
            pagoBtnTarjetaCheck = (ImageView) itemView.findViewById(R.id.pagoBtnTarjetaCheck);
            optionIcon = (AppCompatImageView) itemView.findViewById(R.id.ivOptionIcon);
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.tabCoodFragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
