package com.makao.inquilino;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.makao.inquilino.Entities.Cuotas;
import com.makao.inquilino.Utils.Utils;
import com.makao.inquilino.adapters.listaCuartos_tipo1;
import com.makao.inquilino.adapters.listaPagos;
import com.makao.inquilino.login.LoginActivity;
import com.makao.inquilino.mensages.ProgresDialogPersonalizado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvDepartamentoMain, tvNombreUserMain, tvFechaIngresoMain, tvDniUserMain, tvTituloRcv;
    RecyclerView rcvDeudas, rcvResumen, rcvMensajes;
    AppCompatButton acbuttonDeudasMain, acButtonResumenMain, acButtonMensajesMain;
    ImageView imgLogout;
    listaPagos adapter2;
    listaCuartos_tipo1 adapter;
    ArrayList<Cuotas> lista = new ArrayList<>();
    ArrayList<Cuotas> lista2 = new ArrayList<>();
    private ProgresDialogPersonalizado progressDialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    String fechaFormateada;
    String idCuarto;
    private AlertDialog dialog;
    ImageView imgProximamente;
    int totalConsultas = 2;
    int consultasCompletadas = 0;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDepartamentoMain = findViewById(R.id.tvDepartamentoMain);
        tvNombreUserMain = findViewById(R.id.tvNombreUserMain);
        tvFechaIngresoMain = findViewById(R.id.tvFechaIngresoMain);
        tvDniUserMain = findViewById(R.id.tvDniUserMain);
        rcvDeudas = findViewById(R.id.rcvDeudas);
        rcvResumen = findViewById(R.id.rcvResumen);
        rcvMensajes = findViewById(R.id.rcvMensajes);
        tvTituloRcv = findViewById(R.id.tvTituloRcv);
        imgProximamente = findViewById(R.id.imgProximamente);

        acbuttonDeudasMain = findViewById(R.id.acbuttonDeudasMain);
        acButtonResumenMain = findViewById(R.id.acButtonResumenMain);
        acButtonMensajesMain = findViewById(R.id.acButtonMensajesMain);

        imgLogout = findViewById(R.id.imgLogout);

        rcvDeudas.setVisibility(View.VISIBLE);
        rcvMensajes.setVisibility(View.GONE);
        rcvResumen.setVisibility(View.GONE);
        // Recuperar el ID de usuario almacenado en las SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("UsuarioDatos", this.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt(Utils.USURAIO_ID, -1); // -1 es el valor predeterminado si no se encuentra el ID

        // Verificar si se encontró un ID válido
        if (idUsuario != -1) {
            // El ID de usuario fue recuperado con éxito
            // Puedes usar idUsuario en tu lógica aquí
//            Toast.makeText(this, "ID: "+idUsuario, Toast.LENGTH_SHORT).show();
        } else {
            // No se encontró un ID de usuario válido en las SharedPreferences
//            Toast.makeText(this, "No se encontro el id", Toast.LENGTH_SHORT).show();
        }

        consulta(idUsuario);

        acbuttonDeudasMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarDatos(idCuarto);
                rcvDeudas.setVisibility(View.VISIBLE);
                rcvMensajes.setVisibility(View.GONE);
                rcvResumen.setVisibility(View.GONE);
                imgProximamente.setVisibility(View.GONE);

                acbuttonDeudasMain.setBackgroundResource(R.drawable.background_button_navegacion_seleccionado_main);
                acButtonResumenMain.setBackgroundResource(R.drawable.background_button_navegacion_no_seleccionado_main);
                acButtonMensajesMain.setBackgroundResource(R.drawable.background_button_navegacion_no_seleccionado_main);

                acbuttonDeudasMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                acButtonResumenMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.morado_oscuro));
                acButtonMensajesMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.morado_oscuro));

                tvTituloRcv.setText("Próximos Pagos");


            }
        });
        acButtonResumenMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarDatos(idCuarto);
                rcvDeudas.setVisibility(View.GONE);
                rcvMensajes.setVisibility(View.GONE);
                rcvResumen.setVisibility(View.VISIBLE);
                imgProximamente.setVisibility(View.GONE);

                acbuttonDeudasMain.setBackgroundResource(R.drawable.background_button_navegacion_no_seleccionado_main);
                acButtonResumenMain.setBackgroundResource(R.drawable.background_button_navegacion_seleccionado_main);
                acButtonMensajesMain.setBackgroundResource(R.drawable.background_button_navegacion_no_seleccionado_main);

                acbuttonDeudasMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.morado_oscuro));
                acButtonResumenMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                acButtonMensajesMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.morado_oscuro));

                tvTituloRcv.setText("Resumen de Pagos");

            }
        });
        acButtonMensajesMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarDatos(idCuarto);
                rcvDeudas.setVisibility(View.GONE);
                rcvMensajes.setVisibility(View.GONE);
                rcvResumen.setVisibility(View.GONE);
                imgProximamente.setVisibility(View.VISIBLE);


                acbuttonDeudasMain.setBackgroundResource(R.drawable.background_button_navegacion_no_seleccionado_main);
                acButtonResumenMain.setBackgroundResource(R.drawable.background_button_navegacion_no_seleccionado_main);
                acButtonMensajesMain.setBackgroundResource(R.drawable.background_button_navegacion_seleccionado_main);

                acbuttonDeudasMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.morado_oscuro));
                acButtonResumenMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.morado_oscuro));
                acButtonMensajesMain.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                tvTituloRcv.setText("Mensajes");


            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoPersonalizado();
            }
        });
    }

    public void consulta(int id) {
        String url = Utils.RUTA_APIS + "ConsultarInquilino.php?id_inquilino=" + id;
        url = url.replace(" ", "%20");
        request = Volley.newRequestQueue(MainActivity.this);
        showProgressDialog();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                try {
                    // Obtener datos del administrador
                    JSONObject inquilino = response.getJSONObject("inquilino");
                    JSONObject cuarto = response.getJSONObject("cuarto");
                    // Asignar los valores a tus vistas
                    tvDepartamentoMain.setText(cuarto.getString("Codigo") + " " + cuarto.getString("Piso"));
                    tvDniUserMain.setText(inquilino.getString("DNI"));
                    tvNombreUserMain.setText(inquilino.getString("Nombre"));
                    idCuarto = cuarto.getString("ID");

                    // Definir el formato de entrada (de "dd/MM/yyyy" a "yyyy-MM-dd")
                    SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    // Definir el formato de salida
                    SimpleDateFormat formatoSalida = new SimpleDateFormat(" dd 'de' MMMM yyyy", Locale.getDefault());

                    try {
                        // Parsear la fecha de texto plano
                        Date fecha = formatoEntrada.parse(inquilino.getString("FechaIngreso"));

                        // Formatear la fecha en el formato deseado
                        fechaFormateada = formatoSalida.format(fecha);

                        // Imprimir la fecha formateada
                        System.out.println(fechaFormateada); // Salida: 2024-10-20
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    tvFechaIngresoMain.setText(fechaFormateada);
                    consultarDatos(idCuarto);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(MainActivity.this, "ERROR try", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
//                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(jsonObjectRequest);
    }

    public void consultarDatos(String codigo) {
        String urlPagosPendientes = Utils.RUTA_APIS + "ConsultarPagosCuarto.php?codigo_cuarto=" + codigo;
        String urlPagosRealizados = Utils.RUTA_APIS + "ConsultarPagosCuartoPagado.php?codigo_cuarto=" + codigo;

        urlPagosPendientes = urlPagosPendientes.replace(" ", "%20");
        urlPagosRealizados = urlPagosRealizados.replace(" ", "%20");

        request = Volley.newRequestQueue(MainActivity.this);
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
                adapter = new listaCuartos_tipo1(lista, MainActivity.this);
                rcvDeudas.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rcvDeudas.setAdapter(adapter);
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
                adapter2 = new listaPagos(lista, MainActivity.this);
                rcvResumen.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rcvResumen.setAdapter(adapter2);

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
        cuotas.setEstadoPago(jsonObject.optString("ESTADO_PAG", ""));
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
        progressDialog = new ProgresDialogPersonalizado(MainActivity.this);
        progressDialog.show();
    }

    private void mostrarDialogoPersonalizado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                SharedPreferences sharedPreferences = getSharedPreferences("UsuarioDatos", MainActivity.this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Utils.USURAIO_REGISTRED, false);
                editor.putInt(Utils.USURAIO_ID, 0);
                editor.apply();

                // Redirigir al usuario a la pantalla de inicio de sesión
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual
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