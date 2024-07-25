package com.makao.administrador.navegacion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.activities.GenerarAlquiler;
import com.makao.administrador.activities.Pago_Licoreria;

public class Solicitudes extends Fragment {

    LinearLayout crdPagoLicoreria, crdGenerarAlquiler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_solicitudes, container, false);

        crdPagoLicoreria = vista.findViewById(R.id.crdPagoLicoreria);
        crdGenerarAlquiler = vista.findViewById(R.id.crdGenerarAlquiler);

        //Recuperamos rango Administador
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TipoUsuario", getContext().MODE_PRIVATE);
        String Tipo = sharedPreferences.getString(Utils.USURAIO_TIPO,"");

        //bloqueo de accesos
        if(Tipo.equals("Asistente")){
            crdGenerarAlquiler.setEnabled(false);
        }else{
            crdGenerarAlquiler.setEnabled(true);
        }

        crdGenerarAlquiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GenerarAlquiler.class);

                startActivity(i);
            }
        });

        crdPagoLicoreria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Pago_Licoreria.class);

                startActivity(i);
            }
        });
        return vista;
    }
}