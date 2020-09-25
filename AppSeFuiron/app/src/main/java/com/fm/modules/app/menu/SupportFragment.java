package com.fm.modules.app.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;

public class SupportFragment extends Fragment {

    private EditText message;
    private Button sendBtn;
    private View viewGlobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        viewGlobal = view;
        message = (EditText) view.findViewById(R.id.supportText);
        sendBtn = (Button) view.findViewById(R.id.supportRegistry);
        sendMessaje();
        return view;
    }

    private void sendMessaje() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messager = message.getText().toString();
                if (!"".equals(messager)) {
                    Toast.makeText(viewGlobal.getContext(), "msm: " + messager, Toast.LENGTH_SHORT).show();
                    showFragment(new SupportSendFragment());
                } else {
                    Toast.makeText(viewGlobal.getContext(), "Escribe un Mensaje", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}