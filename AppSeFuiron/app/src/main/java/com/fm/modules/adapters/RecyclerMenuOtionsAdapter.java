package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.login.Logon;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.OptionsEntity;
import com.fm.modules.app.menu.UserProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RecyclerMenuOtionsAdapter extends RecyclerView.Adapter<RecyclerMenuOtionsAdapter.ViewHolder> {

    private List<OptionsEntity> items;
    private Context context;
    private FragmentActivity fragmentActivity;
    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public RecyclerMenuOtionsAdapter(List<OptionsEntity> optionsEntities, Context context, FragmentActivity fragmentActivity) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.items = optionsEntities;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        sharedPreferences = context.getSharedPreferences("LogonData", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_option_menu, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.asignarDatos(items.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (items.get(position).getId()) {
                    case 1:
                        showFragment(new UserProfileFragment());
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        try {
                            editor = sharedPreferences.edit();
                            editor.putString("email", "neles");
                            editor.putString("password", "neles");
                            editor.commit();
                        } catch (Exception ignore) {
                        }
                        firebaseAuth.signOut();
                        Logued.usuarioLogued = null;
                        Intent intent = new Intent(context, Logon.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imagen;
        AppCompatTextView optioName;
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            imagen = (AppCompatImageView) view.findViewById(R.id.menuOptionImage);
            optioName = (AppCompatTextView) view.findViewById(R.id.menuOptionName);
            linearLayout = (LinearLayout) view.findViewById(R.id.holderItemOptionMenu);
        }

        public void asignarDatos(final OptionsEntity optionsEntity) {
            imagen.setImageResource(optionsEntity.getResImageId());
            optioName.setText(optionsEntity.getOptionname());
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
