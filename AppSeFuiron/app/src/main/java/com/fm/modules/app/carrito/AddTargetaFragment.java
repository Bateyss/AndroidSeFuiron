package com.fm.modules.app.carrito;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTargetaFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerDialogListener;
    private View viewGlobal;
    private EditText nombre;
    private EditText numero;
    private EditText codigo;
    private EditText fecha;
    private Button guardarButton;
    private Date fecha1 = null;
    private ImageView leftArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_targeta, container, false);
        viewGlobal = view;
        nombre = (EditText) view.findViewById(R.id.targetaAddName);
        numero = (EditText) view.findViewById(R.id.targetaAddNumber);
        codigo = (EditText) view.findViewById(R.id.targetaAddCode);
        fecha = (EditText) view.findViewById(R.id.targetaAddDateEx);
        leftArrow = (ImageView) view.findViewById(R.id.leftArrowChoice);
        guardarButton = (Button) view.findViewById(R.id.targetaAddBtn);
        datePicherLoader();
        listenerGuardar();
        leftArrowListener();
        onBack();
        return view;
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new PagoTargetaFragment());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void leftArrowListener() {
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PagoTargetaFragment());
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void listenerGuardar() {
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    Toast.makeText(viewGlobal.getContext(), "Passed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validar() {
        if ("".equals(nombre.getText().toString())) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(numero.getText().toString())) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Numero", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numero.getText().toString().length() < 8) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Numero", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(codigo.getText().toString())) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Codigo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (codigo.getText().toString().length() < 3) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Numero", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fecha1 == null) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Fecha", Toast.LENGTH_SHORT).show();
            return false;
        }
        Date d = obtenerMes(fecha1);
        Date d2 = obtenerMesActual();
        System.out.println("d1: " + d.getTime() + " d2: " + d2.getTime());
        if (d.getTime() >= d2.getTime()) {
            Toast.makeText(viewGlobal.getContext(), "La Tergeta ha expirado", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Date obtenerMes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int mes = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, mes, 1, 23, 59, 59);
        return calendar.getTime();
    }

    private Date obtenerMesActual() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int mes = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, mes, 1, 0, 0, 0);
        return calendar.getTime();
    }

    private void datePicherLoader() {
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // codigo para la fecha
                // datepicker
                showDatePickDialog(view);
            }
        });
        datePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(i, i1, i2);
                SimpleDateFormat sdt = new SimpleDateFormat("EEEE dd MMMM  yyyy");
                fecha.setText(sdt.format(cal.getTime()));
                fecha1 = cal.getTime();
            }
        };
    }

    private void showDatePickDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mont = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(viewGlobal.getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                datePickerDialogListener,
                year,
                mont,
                day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }
}