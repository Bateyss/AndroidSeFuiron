package com.fm.modules.sqlite.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.service.OpcionSubMenuService;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class RecyclerTarjetasAdapter extends RecyclerView.Adapter<RecyclerTarjetasAdapter.ViewHolder> {

    private Context context;
    private List<Tarjetas> tarjetas;
    private FragmentActivity fragmentActivity;

    private OpcionSubMenuService opcionesSubMenuService = new OpcionSubMenuService();

    public RecyclerTarjetasAdapter(List<Tarjetas> tarjetas, Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.tarjetas = tarjetas;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerTarjetasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tab_menus, parent, false);
        return new RecyclerTarjetasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerTarjetasAdapter.ViewHolder holder, final int position) {
        DecimalFormat decimalFormat = new DecimalFormat("0000 0000 0000 0000");
        DecimalFormat decimalFormat2 = new DecimalFormat("**** **** **** 0000");
        holder.nombre.setText(tarjetas.get(position).getNombre());
        try {
            Number number = decimalFormat.parse(tarjetas.get(position).getNumero());
            holder.numero.setText(decimalFormat2.format(number));
        } catch (ParseException e) {
            System.out.println("erro decimal recycler tarjetas: " + e);
        }
    }

    @Override
    public int getItemCount() {
        return tarjetas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numero;
        TextView nombre;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numero = (TextView) itemView.findViewById(R.id.tarjNombre);
            nombre = (TextView) itemView.findViewById(R.id.tarjNumero);
            cardView = (CardView) itemView.findViewById(R.id.tarjCardV);
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.tabCoodFragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
