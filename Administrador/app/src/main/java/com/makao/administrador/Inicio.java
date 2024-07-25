package com.makao.administrador;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.makao.administrador.navegacion.Departamentos;
import com.makao.administrador.navegacion.Home;
import com.makao.administrador.navegacion.Notificas;
import com.makao.administrador.navegacion.Pagos;
import com.makao.administrador.navegacion.Solicitudes;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Inicio extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private RelativeLayout rllNavigation;
    Departamentos fragmentDepartamentos;
    Home fragmentInicio;
    Pagos fragmentPagos;
    Notificas fragmentNotificas;
    Solicitudes fragmentSolicitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        setContentView(R.layout.activity_inicio);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        rllNavigation = findViewById(R.id.rllNavigation);

        fragmentInicio = new Home();
        fragmentDepartamentos = new Departamentos();
        fragmentPagos = new Pagos();
        fragmentNotificas = new Notificas();
        fragmentSolicitudes = new Solicitudes();

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_navigation));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_inquilinos_navigation));
        //bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_pagos_navigation));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_solicitudes_navigation));
//        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_notificar_navigation));

        bottomNavigation.show(1, false);
        showFragment(fragmentInicio);

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()) {
                    case 1:
                        showFragment(fragmentInicio);
                        break;
                    case 2:
                        showFragment(fragmentDepartamentos);
                        break;
                    case 3:
                        showFragment(fragmentPagos);
                        break;
                    case 4:
                        showFragment(fragmentSolicitudes);
                        break;
                    case 5:
                        showFragment(fragmentNotificas);
                        break;
                }
                return null;
            }
        });
    }
    private void showFragment(Fragment fragment) {
        // Reemplazar el fragment actual en el contenedor
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.rllNavigation, fragment);
        transaction.addToBackStack(null); // Opcional: para agregar a la pila de retroceso
        transaction.commit();
    }
}