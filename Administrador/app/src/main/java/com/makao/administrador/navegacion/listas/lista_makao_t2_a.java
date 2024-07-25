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

public class lista_makao_t2_a extends Fragment {

    GridView grdListaMakao;
    listaCuartos_tipo2 adapter;
    ArrayList<Cuartos> listaCuartos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_lista_makao_t2, container, false);

        // Inicializar RecyclerView
        grdListaMakao = vista.findViewById(R.id.grdListaMakao);
        listaCuartos.clear();

        // Inicializar lista de cuartos (datos de prueba cambiar por base de datos luego)

        listaCuartos.add(new Cuartos(1,1,"MKO","25","101",250,"Al Día"));
        listaCuartos.add(new Cuartos(2,2,"MKO","25","201",250,"Al Día"));
        listaCuartos.add(new Cuartos(3,3,"MKO","25","301",250,"En Deuda"));
        listaCuartos.add(new Cuartos(4,4,"MKO","25","401",250,"En Deuda"));
        listaCuartos.add(new Cuartos(5,5,"MKO","25","411",250,"Morosidad Inminente"));
        listaCuartos.add(new Cuartos(6,6,"MKO","25","211",250,"En Deuda"));
        listaCuartos.add(new Cuartos(7,7,"MKO","25","314",250,"Al Día"));

        // Inicializar y configurar el adapter
        adapter = new listaCuartos_tipo2(listaCuartos, getContext());
        grdListaMakao.setAdapter(adapter);

        return vista;
    }
}