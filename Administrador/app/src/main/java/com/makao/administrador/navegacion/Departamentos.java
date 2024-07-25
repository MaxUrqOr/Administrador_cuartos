package com.makao.administrador.navegacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.makao.administrador.R;
import com.makao.administrador.navegacion.listas.lista_makao_t2;
import com.makao.administrador.navegacion.listas.lista_parque_t2;

public class Departamentos extends Fragment {
    Button btnMakaoT2, btnParqueT2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_departamentos, container, false);
        showFragmentMakao();

        btnMakaoT2 = vista.findViewById(R.id.btnMakaoT2);
        btnParqueT2 = vista.findViewById(R.id.btnParqueT2);

        btnMakaoT2.setBackgroundResource(R.drawable.background_btn_inicio);
        btnParqueT2.setBackgroundResource(R.drawable.background_btn_inicio_off);

        btnMakaoT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMakaoT2.setBackgroundResource(R.drawable.background_btn_inicio);
                btnParqueT2.setBackgroundResource(R.drawable.background_btn_inicio_off);
                showFragmentMakao();
            }
        });

        btnParqueT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnParqueT2.setBackgroundResource(R.drawable.background_btn_inicio);
                btnMakaoT2.setBackgroundResource(R.drawable.background_btn_inicio_off);

                showFragmentParque();
            }
        });

        return vista;
    }

    private void showFragmentParque() {
        Fragment parqueFragment = new lista_parque_t2();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, parqueFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showFragmentMakao() {
        Fragment makaoFragment = new lista_makao_t2();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, makaoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}