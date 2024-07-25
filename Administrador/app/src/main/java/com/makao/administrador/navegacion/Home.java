package com.makao.administrador.navegacion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.login.Login;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;
import com.makao.administrador.navegacion.listas.lista_makao;
import com.makao.administrador.navegacion.listas.lista_parque;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends Fragment {
    TextView txtNombreUsuario, txtCargo, txtDepartamentosLibres, txtSolicitudes, txtPagosConfirmar;
    ImageView imgPerfil;
    AppCompatButton btnParque, btnMakao;

    private ProgresDialogPersonalizado progressDialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);
        showFragmentMakao();
        btnMakao = vista.findViewById(R.id.btnMakao);
        btnParque = vista.findViewById(R.id.btnParque);
        imgPerfil = vista.findViewById(R.id.imgPerfil);
        txtNombreUsuario = vista.findViewById(R.id.txtNombreUsuario);
        txtCargo = vista.findViewById(R.id.txtCargo);
        txtDepartamentosLibres = vista.findViewById(R.id.txtDepartamentosLibres);
        txtSolicitudes = vista.findViewById(R.id.txtSolicitudes);
        txtPagosConfirmar = vista.findViewById(R.id.txtPagosConfirmar);

        // Obtener las preferencias compartidas
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UsuarioDatos", getContext().MODE_PRIVATE);

        // Obtener el valor del ID de usuario almacenado en las preferencias compartidas
        int idUsuarioRecuperado = sharedPreferences.getInt(Utils.USURAIO_ID, -1); // -1 es el valor predeterminado si no se encuentra el ID de usuario


        consulta(idUsuarioRecuperado, txtNombreUsuario, txtSolicitudes, txtDepartamentosLibres, txtPagosConfirmar);

        btnMakao.setBackgroundResource(R.drawable.background_btn_inicio);
        btnParque.setBackgroundResource(R.drawable.background_btn_inicio_off);

        btnMakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnMakao.setBackgroundResource(R.drawable.background_btn_inicio);
                btnParque.setBackgroundResource(R.drawable.background_btn_inicio_off);

                showFragmentMakao();

            }
        });

        btnParque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnParque.setBackgroundResource(R.drawable.background_btn_inicio);
                btnMakao.setBackgroundResource(R.drawable.background_btn_inicio_off);
                showFragmentParque();
            }
        });

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        return vista;
    }

    private void showFragmentParque() {
        Fragment parqueFragment = new lista_parque();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, parqueFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showFragmentMakao() {
        Fragment makaoFragment = new lista_makao();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, makaoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void consulta(int id, TextView txtNombreUsuario, TextView txtSolicitudes, TextView txtDepartamentosLibres, TextView txtPagosConfirmar) {
        String url = Utils.RUTA_APIS + "ConsultarAdministrador.php?admin_id=" + id;
        url = url.replace(" ", "%20");
        request = Volley.newRequestQueue(getContext());
        showProgressDialog();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                try {
                    // Obtener datos del administrador
                    JSONObject administrador = response.getJSONObject("administrador");

                    // Asignar los valores a tus vistas
                    txtNombreUsuario.setText("Hola " + administrador.getString("Nombre"));
                    txtSolicitudes.setText("" + administrador.getInt("cantidad_solicitudes"));
                    txtDepartamentosLibres.setText("" + administrador.getInt("cantidad_cuartos_libres"));
                    txtPagosConfirmar.setText("" + administrador.getInt("cantidad_pagos"));
                    txtCargo.setText(administrador.getString("Tipo_administrador"));

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TipoUsuario", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Utils.USURAIO_TIPO, administrador.getString("Tipo_administrador"));
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "ERROR try", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(jsonObjectRequest);
    }


    private void showProgressDialog() {
        progressDialog = new ProgresDialogPersonalizado(getActivity());
        progressDialog.show();
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view, Gravity.END, 0, R.style.PopupMenuStyle);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_salir:
                        mostrarDialogoPersonalizado();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }


    private void mostrarDialogoPersonalizado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_cerrar_secion, null);
        builder.setView(dialogView);

        // Obtener referencias a los elementos del diálogo

        AppCompatButton btnAceptar = dialogView.findViewById(R.id.btnAceptar);
        AppCompatButton btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        // Configurar acciones de los botones
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Borrar la información de inicio de sesión almacenada en las preferencias compartidas
                SharedPreferences sharedPreferences =getActivity().getSharedPreferences("UsuarioDatos", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Utils.USURAIO_REGISTRED, false);
                editor.putInt(Utils.USURAIO_ID, 0);
                editor.apply();

                // Redirigir al usuario a la pantalla de inicio de sesión
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);

                // Obtener la actividad asociada al fragmento
                Activity activity = getActivity();
                // Verificar si la actividad no es nula y si es una instancia de AppCompatActivity
                if (activity != null && activity instanceof AppCompatActivity) {
                    // Finalizar la actividad
                    ((AppCompatActivity) activity).finish();
                }

                dialog.dismiss();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        // Crear y mostrar el diálogo
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Establecer el fondo transparente
        dialog.show();
    }
}