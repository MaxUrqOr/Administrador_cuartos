package com.makao.administrador.navegacion.listas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.makao.administrador.Entities.Cuartos;
import com.makao.administrador.R;
import com.makao.administrador.adapters.listaCuartos_tipo2;

import java.util.ArrayList;


public class lista_parque_t2_a extends Fragment {

    GridView grdListaParque;
    listaCuartos_tipo2 adapter;
    ArrayList<Cuartos> listaCuartos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_lista_parque_t2, container, false);

        // Inicializar RecyclerView
        grdListaParque = vista.findViewById(R.id.grdListaParque);
        listaCuartos.clear();

        // Inicializar lista de cuartos (datos de prueba cambiar por base de datos luego)

        listaCuartos.add(new Cuartos(1,1,"PID","25","101",250,"Al Día"));
        listaCuartos.add(new Cuartos(2,2,"PID","25","111",250,"Al Día"));
        listaCuartos.add(new Cuartos(3,3,"PID","25","114",250,"En Deuda"));
        listaCuartos.add(new Cuartos(4,4,"PID","25","211",250,"Morosidad Inminente"));
        listaCuartos.add(new Cuartos(5,5,"PID","25","213",250,"Al Día"));
        listaCuartos.add(new Cuartos(6,6,"PID","25","301",250,"Al Día"));
        listaCuartos.add(new Cuartos(7,7,"PID","25","311",250,"Al Día"));

        // Inicializar y configurar el adapter
        adapter = new listaCuartos_tipo2(listaCuartos, getContext());
        grdListaParque.setAdapter(adapter);

        return vista;
    }
}