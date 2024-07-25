package com.makao.administrador.navegacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.makao.administrador.R;
import com.makao.administrador.activities.Enviar_Mensajes;
import com.makao.administrador.mensages.Dialog_notificar;

public class Notificas extends Fragment {

    Dialog_notificar dialogNotificar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_notificas, container, false);

        Button btnNotificar = vista.findViewById(R.id.btnNotificar);
        Button btnReportes = vista.findViewById(R.id.btnReportes);

        btnNotificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Enviar_Mensajes.class);
                startActivity(i);
            }
        });

        return vista;
    }
}