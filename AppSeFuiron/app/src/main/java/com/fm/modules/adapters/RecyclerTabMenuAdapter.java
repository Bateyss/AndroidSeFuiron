package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.app.restaurantes.PlatillosActivity;
import com.fm.modules.models.Menu;
import com.fm.modules.service.OpcionSubMenuService;

import java.util.List;

public class RecyclerTabMenuAdapter extends RecyclerView.Adapter<RecyclerTabMenuAdapter.ViewHolder> {

    private Context context;
    private List<Menu> menus;
    private FragmentActivity fragmentActivity;

    private OpcionSubMenuService opcionesSubMenuService = new OpcionSubMenuService();

    public RecyclerTabMenuAdapter(List<Menu> menuList, Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.menus = menuList;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerTabMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tab_menus, parent, false);
        return new RecyclerTabMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerTabMenuAdapter.ViewHolder holder, final int position) {
        holder.title.setText(menus.get(position).getNombreMenu());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalRestaurantes.menuTabPosition = position;
                GlobalRestaurantes.menuTabSelected = menus.get(position);
                notifyDataSetChanged();
                showFragment(new PlatillosActivity(menus.get(position)));
            }
        });
        if (position == 0) {
            showFragment(new PlatillosActivity(menus.get(position)));
        }
        if (GlobalRestaurantes.menuTabPosition == null) {
            if (position == 0){
                holder.bottonMarker.setBackground(context.getResources().getDrawable(R.drawable.tab_indicator));
                holder.bottonMarker.setBackgroundColor(context.getResources().getColor(R.color.orange));
            }else{
                holder.bottonMarker.setBackground(context.getResources().getDrawable(R.drawable.tab_indicator2));
                holder.bottonMarker.setBackgroundColor(context.getResources().getColor(R.color.opaquePurple));
            }
        }
        if (GlobalRestaurantes.menuTabPosition != null) {
            if (GlobalRestaurantes.menuTabPosition == position) {
                GlobalRestaurantes.menuTabPosition = null;
                holder.bottonMarker.setBackground(context.getResources().getDrawable(R.drawable.tab_indicator));
                holder.bottonMarker.setBackgroundColor(context.getResources().getColor(R.color.orange));
            } else {
                holder.bottonMarker.setBackground(context.getResources().getDrawable(R.drawable.tab_indicator2));
                holder.bottonMarker.setBackgroundColor(context.getResources().getColor(R.color.opaquePurple));
            }
        }
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cardView;
        ImageView bottonMarker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvMenu);
            cardView = (CardView) itemView.findViewById(R.id.cvMenu);
            bottonMarker = (ImageView) itemView.findViewById(R.id.separator);
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.tabCoodFragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
