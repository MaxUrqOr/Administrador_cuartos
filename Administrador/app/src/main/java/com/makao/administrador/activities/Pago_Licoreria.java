package com.makao.administrador.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Entities.Cuartos;
import com.makao.administrador.Entities.Cuotas;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.adapters.listaPagos;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Pago_Licoreria extends AppCompatActivity {
    private List<Cuartos> listaCuartos = new ArrayList<>();
    private List<String> nombresCuartos = new ArrayList<>();
    AutoCompleteTextView searchView;
    TextView txtDepartamento, txtDNI, txtNombre;
    ImageView imgAtras;
    AppCompatButton btnPagos, btnCancelar;
    RecyclerView rclDeudas;
    private ProgresDialogPersonalizado progressDialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayAdapter<String> adapter;
    listaPagos adapter2;
    Cuartos cuartos;
    String CostoCuarto;
    int ContadorConsultas=0, MaximoConsultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagolicoreria);

        // Inicializar las vistas
        searchView = findViewById(R.id.srcDepartamentos);
        txtDepartamento = findViewById(R.id.edtDepartamento);
        txtNombre = findViewById(R.id.edtNombre);
        txtDNI = findViewById(R.id.edtDNI);
        imgAtras = findViewById(R.id.imageView);
        rclDeudas = findViewById(R.id.rclDeudas);
        btnPagos = findViewById(R.id.btnAceptar);


        // Configurar el clic del botón Atrás
        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Configurar la selección de elementos en el AutoCompleteTextView
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String textoSeleccionado = adapterView.getItemAtPosition(i).toString();
                // Separar el texto seleccionado en letras y números
                String letras = textoSeleccionado.substring(0, Math.min(textoSeleccionado.length(), 3));
                String numeros = textoSeleccionado.replaceAll("\\D+", "");
                // Buscar el cuarto correspondiente en la lista y realizar alguna acción
                buscarCuarto(letras, numeros);
            }
        });

        btnPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ejemplo de cómo obtener los IDs de pago seleccionados
                List<Integer> idsSeleccionados = adapter2.getSelectedIds();

                Date fechaActual = new Date();
                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                // Formatear la fecha actual en el formato deseado
                String fechaFormateada = formatoFecha.format(fechaActual);

                showProgressDialog();

                ContadorConsultas = 0;
                for (int id : idsSeleccionados) {
                    MaximoConsultas = idsSeleccionados.size();
                    InsertarPago(id+"", fechaFormateada, CostoCuarto+"", "Aprobado", "Pago en Licoreria por: ");
                }
            }
        });

        // Realizar la consulta para obtener la lista de cuartos
        consulta();
    }

    private void consulta() {
        String url = Utils.RUTA_APIS + "ConsultarTodosCuartos.php";
        url = url.replace(" ", "%20");
        request = Volley.newRequestQueue(Pago_Licoreria.this);
        showProgressDialog();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                try {
                    JSONArray jsonArray = response.getJSONArray("cuartos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        cuartos = new Cuartos();

                        String nombre = jsonObject.getString("CODIGO_CUA") + " " + jsonObject.getString("PISO_CUA");

                        nombresCuartos.add(nombre);
                        cuartos.setIdCuarto(jsonObject.getInt("ID_CUARTO"));
                        cuartos.setIdInquilino(jsonObject.getInt("ID_INQUILINO"));
                        cuartos.setCostoCua(jsonObject.getInt("COSTO_CUA"));
                        cuartos.setPisoCua(jsonObject.getString("PISO_CUA"));
                        cuartos.setCodigoCua(jsonObject.getString("CODIGO_CUA"));
                        cuartos.setDniInq(jsonObject.getString("DNI_INQ"));
                        cuartos.setDniInq(jsonObject.getString("DNI_INQ"));
                        cuartos.setNombreInq(jsonObject.getString("NOMBRE_INQ"));
                        listaCuartos.add(cuartos);
                    }
                    // Crear un adaptador y configurarlo con los nombres de los cuartos
                    adapter = new ArrayAdapter<>(Pago_Licoreria.this, android.R.layout.simple_dropdown_item_1line, nombresCuartos);
                    // Establecer el adaptador en el AutoCompleteTextView
                    searchView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });
        request.add(jsonObjectRequest);
    }

    private void buscarCuarto(String letras, String numeros) {
        // Buscar en la lista de cuartos por el texto ingresado

        for (Cuartos cuarto : listaCuartos) {
            if (cuarto.getPisoCua().equals(numeros)&&cuarto.getCodigoCua().toString().equals(letras)) {
                // Coincidencia encontrada, realizar alguna acción con el cuarto encontrado
                txtDepartamento.setText(cuarto.getCodigoCua() + " " + cuarto.getPisoCua());
                txtNombre.setText(cuarto.getNombreInq());
                txtDNI.setText(cuarto.getDniInq());
                CostoCuarto = cuarto.getCostoCua()+"";
                int codigo = cuarto.getIdCuarto();
                String urlPagosPendientes = Utils.RUTA_APIS + "ConsultarPagosCuarto.php?codigo_cuarto=" + codigo;

                urlPagosPendientes = urlPagosPendientes.replace(" ", "%20");

                request = Volley.newRequestQueue(Pago_Licoreria.this);
                showProgressDialog();
                // Consultar pagos pendientes
                consultarPagos(urlPagosPendientes, new ConsultaCallback2() {
                    @Override
                    public void onSuccess(ArrayList<Cuotas> lista) {

                        // Agregar los datos a la lista
                        adapter2 = new listaPagos(lista, Pago_Licoreria.this);
                        rclDeudas.setLayoutManager(new LinearLayoutManager(Pago_Licoreria.this));
                        rclDeudas.setAdapter(adapter2);
                        progressDialog.hide();
                    }
                });
                break; // Terminar el bucle una vez que se encuentre una coincidencia
            }
        }
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
        cuotas.setMontoCuo(jsonObject.optString("MONTO_CUO"));
        cuotas.setEstadoCuota(jsonObject.optString("ESTADO_CUO"));
        cuotas.setFechaVencimiento(jsonObject.optString("FECHA_VENCIMIENTO"));
        cuotas.setIdInquilino(jsonObject.optInt("ID_INQUILINO"));
        cuotas.setNombreInqui(jsonObject.optString("NOMBRE_INQ"));
    }
    private void showProgressDialog() {
        progressDialog = new ProgresDialogPersonalizado(Pago_Licoreria.this);
        progressDialog.show();
    }

    public void InsertarPago(String IDP, String Fecha, String Monto, String Estado, String Razon){
        String url =  Utils.RUTA_APIS + "insertarPago.php";
        // Crear una solicitud POST usando Volley
        RequestQueue request = Volley.newRequestQueue(Pago_Licoreria.this);
        // Tiempo de espera en milisegundos (por ejemplo, 10 segundos)
        int tiempoEspera = 10000;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor
                        ContadorConsultas++;
                        if(MaximoConsultas==ContadorConsultas)
                        {
                            progressDialog.hide();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    recreate();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                    searchView.setText("");
                                }
                            }, 1500);

                            Toast.makeText(Pago_Licoreria.this, "Pagos Realizados", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(Pago_Licoreria.this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Pago_Licoreria.this, "Error de conexión: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Agregar parámetros POST
                Map<String, String> params = new HashMap<>();
                params.put("id_cuota_param", IDP);
                params.put("fecha_pag_param", Fecha);
                params.put("monto_pag_param", Monto);
                params.put("estado_pag_param", Estado);
                params.put("razon_pag_param", Razon);
                return params;
            }
        };

        // Configurar el tiempo de espera para la solicitud
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                tiempoEspera,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Agregar la solicitud a la cola de solicitudes
        request.add(stringRequest);
    }

}
