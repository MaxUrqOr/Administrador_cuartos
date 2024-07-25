package com.makao.administrador.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Entities.Cuotas;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.adapters.listaPagos;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Detalle_Pagos extends AppCompatActivity {
    listaPagos adapter, adapter2;
    ArrayList<Cuotas> lista = new ArrayList<>();
    ArrayList<Cuotas> lista2 = new ArrayList<>();
    ImageView imgRegresar;
    TextView txtDepartamento;
    RecyclerView rclPendientes, rclPagos;
    ConstraintLayout llyEncabezado;
    String IDC, Departamento, Estado;
    private ProgresDialogPersonalizado progressDialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    int totalConsultas = 2;
    int consultasCompletadas = 0;

    @Override
    protected void onResume() {
        super.onResume();
        // Inicializar RecyclerView
        lista.clear();
        lista2.clear();

        consultarDatos(IDC);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pagos);

        imgRegresar = findViewById(R.id.imgRegresar);
        txtDepartamento = findViewById(R.id.txtDepartamento);
        rclPendientes = findViewById(R.id.rclPendientes);
        rclPagos = findViewById(R.id.rclPagos);
        llyEncabezado = findViewById(R.id.llyEncabezado);

        IDC = getIntent().getStringExtra("idC");
        Departamento = getIntent().getStringExtra("dep");
        Estado = getIntent().getStringExtra("estado");

//        // Inicializar RecyclerView
//        lista.clear();
//        lista2.clear();
//
//        consultarDatos(IDC);
//        consulta2(IDC);

        switch (Estado.toString()) {
            case "Libre":
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorLibre2));
                break;
            case "Al Día":
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorPagado));
                break;
            case "En Deuda":
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorDeuda));
                break;
            case "Morosidad Inminente":
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorMorosidad));
                break;
        }

        txtDepartamento.setText(Departamento);

        imgRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void consultarDatos(String codigo) {
        String urlPagosPendientes = Utils.RUTA_APIS + "ConsultarPagosCuarto.php?codigo_cuarto=" + codigo;
        String urlPagosRealizados = Utils.RUTA_APIS + "ConsultarPagosCuartoPagado.php?codigo_cuarto=" + codigo;

        urlPagosPendientes = urlPagosPendientes.replace(" ", "%20");
        urlPagosRealizados = urlPagosRealizados.replace(" ", "%20");

        request = Volley.newRequestQueue(Detalle_Pagos.this);
        showProgressDialog();

        // Incrementar el contador de consultas
        totalConsultas = 2;
        consultasCompletadas = 0;

        // Consultar pagos pendientes
        consultarPagos(urlPagosPendientes, new ConsultaCallback2() {
            @Override
            public void onSuccess(ArrayList<Cuotas> lista) {
                // Incrementar el contador de consultas completadas
                consultasCompletadas++;

                // Agregar los datos a la lista
                adapter = new listaPagos(lista, Detalle_Pagos.this);
                rclPendientes.setLayoutManager(new LinearLayoutManager(Detalle_Pagos.this));
                rclPendientes.setAdapter(adapter);
                // Si ambas consultas han finalizado, ocultar el ProgressDialog
                if (consultasCompletadas == totalConsultas) {
                    progressDialog.hide();
                }
            }
        });

        // Consultar pagos realizados
        consultarPagos(urlPagosRealizados, new ConsultaCallback2() {
            @Override
            public void onSuccess(ArrayList<Cuotas> lista) {
                // Incrementar el contador de consultas completadas
                consultasCompletadas++;

                // Agregar los datos a la lista
                adapter2 = new listaPagos(lista, Detalle_Pagos.this);
                rclPagos.setLayoutManager(new LinearLayoutManager(Detalle_Pagos.this));
                rclPagos.setAdapter(adapter2);

                // Si ambas consultas han finalizado, ocultar el ProgressDialog
                if (consultasCompletadas == totalConsultas) {
                    progressDialog.hide();
                }
            }
        });
    }

    private void consultarPagos(String url, final ConsultaCallback2 callback) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Cuotas> lista = new ArrayList<>();

                try {
                    JSONArray jsonArray = response.getJSONArray("pagos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Cuotas cuotas = new Cuotas();
                        // Asignación de datos
                        asignarDatos(cuotas, jsonObject);
                        lista.add(cuotas);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Llamar al método onSuccess del callback y pasar la lista de datos
                callback.onSuccess(lista);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar el error aquí

                // Llamar al método onSuccess del callback incluso si hay un error
                callback.onSuccess(new ArrayList<>());
            }
        });

        request.add(jsonObjectRequest);
    }

    private interface ConsultaCallback2 {
        void onSuccess(ArrayList<Cuotas> lista);
    }

    private void asignarDatos(Cuotas cuotas, JSONObject jsonObject) throws JSONException {
        // Verificar y asignar datos
        cuotas.setIdPago(jsonObject.optInt("ID_PAGO"));
        cuotas.setIdCuota(jsonObject.optInt("ID_CUOTA"));
        cuotas.setFechaPago(jsonObject.optString("FECHA_PAG"));
        cuotas.setMontoPago(jsonObject.optInt("MONTO_PAG"));
        cuotas.setEstadoPago(jsonObject.optString("ESTADO_PAG"));
        cuotas.setIdCuarto(jsonObject.optInt("ID_CUARTO"));
        cuotas.setCodigoCua(jsonObject.optString("CODIGO_CUA"));
        cuotas.setPisoCua(jsonObject.optString("PISO_CUA"));
        cuotas.setCostoCua(jsonObject.optInt("COSTO_CUA"));
        cuotas.setEstadoCuota(jsonObject.optString("ESTADO_CUO"));
        cuotas.setFechaVencimiento(jsonObject.optString("FECHA_VENCIMIENTO"));
        cuotas.setIdInquilino(jsonObject.optInt("ID_INQUILINO"));
        cuotas.setNombreInqui(jsonObject.optString("NOMBRE_INQ"));
        cuotas.setMontoCuo(jsonObject.optString("MONTO_CUO"));
    }

    private void showProgressDialog() {
        progressDialog = new ProgresDialogPersonalizado(Detalle_Pagos.this);
        progressDialog.show();
    }
}