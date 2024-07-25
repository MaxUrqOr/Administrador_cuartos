package com.makao.administrador.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Entities.Cuotas;
import com.makao.administrador.Inicio;
import com.makao.administrador.R;
import com.makao.administrador.Splash;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.adapters.listaPagos;
import com.makao.administrador.mensages.Dialog_liberar_departamento;
import com.makao.administrador.mensages.Dialog_mensaje_reportar;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Detalle_Departamento extends AppCompatActivity {
    listaPagos adapter, adapter2;
    ArrayList<Cuotas> lista = new ArrayList<>();
    ArrayList<Cuotas> lista2 = new ArrayList<>();
    ImageView imgRegresar;
    LinearLayout lnySuperior2;
    TextView txtDepartamento, txtLiberarDepartamento, txtMensaje;
    EditText edtNombresCompletos, edtDNI, edtFecha, edtCosto, edtTelefono, edtGarantia;
    RecyclerView rclPendientes, rclPagos;
    AppCompatButton btnVerHistorial, btnReportar, btnEditar, btnGuardar;
    ConstraintLayout llyEncabezado;
    String IDC, IDI, Departamento, Estado, Costo, Dni, Nombre, Telefono, Fecha, fechaFormateada, Garantia;
    private Calendar calendar;
    int Conteo = 0;
    private ProgresDialogPersonalizado progressDialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    int totalConsultas = 2;
    int consultasCompletadas = 0;
    private RequestQueue mRequestQueue;

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
        setContentView(R.layout.activity_detalle_departamento);

        //Recuperamos rango Administador
        SharedPreferences sharedPreferences = this.getSharedPreferences("TipoUsuario", this.MODE_PRIVATE);
        String Tipo = sharedPreferences.getString(Utils.USURAIO_TIPO, "");

        // Inicialización del objeto Calendar
        calendar = Calendar.getInstance();

        imgRegresar = findViewById(R.id.imgRegresar);

        txtDepartamento = findViewById(R.id.txtDepartamento);
        txtLiberarDepartamento = findViewById(R.id.txtLiberarDepartamento);
        txtMensaje = findViewById(R.id.txtMensaje);

        edtNombresCompletos = findViewById(R.id.edtNombreCompleto);
        edtDNI = findViewById(R.id.edtDNI);
        edtFecha = findViewById(R.id.edtFecha);
        edtCosto = findViewById(R.id.edtCosto);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtGarantia = findViewById(R.id.edtGarantia);

        rclPendientes = findViewById(R.id.rclPendientes);
        rclPagos = findViewById(R.id.rclPagos);

        btnVerHistorial = findViewById(R.id.btnVerHistorial);
        btnReportar = findViewById(R.id.btnReportar);
        btnEditar = findViewById(R.id.btnEditar);
        btnGuardar = findViewById(R.id.btnGuardar);

        llyEncabezado = findViewById(R.id.llyEncabezado);
        lnySuperior2 = findViewById(R.id.lnySuperior2);

        lnySuperior2.setVisibility(View.GONE);

        IDI = getIntent().getStringExtra("idI");
        IDC = getIntent().getStringExtra("idC");
        Departamento = getIntent().getStringExtra("dep");
        Estado = getIntent().getStringExtra("estado");

        Nombre = getIntent().getStringExtra("nombre");
        Dni = getIntent().getStringExtra("dni");
        Fecha = getIntent().getStringExtra("fecha");
        Telefono = getIntent().getStringExtra("telefono");
        Costo = getIntent().getStringExtra("costo");
        Garantia = getIntent().getStringExtra("garantia");


//      Hace la consulta a la hora de crear la actividad
//        // Inicializar RecyclerView
//        lista.clear();
//        lista2.clear();
//        consultarDatos(IDC);

        //bloqueo de accesos
        if (Tipo.equals("Asistente")) {
            txtLiberarDepartamento.setEnabled(false);
            btnEditar.setEnabled(false);

        } else {
            txtLiberarDepartamento.setEnabled(true);
            btnEditar.setEnabled(true);

        }

        switch (Estado.toString()) {
            case "Libre":
                btnReportar.setVisibility(View.GONE);
                btnVerHistorial.setVisibility(View.GONE);
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorLibre2));
                break;
            case "Al Día":
                btnReportar.setVisibility(View.GONE);
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorPagado));
                btnVerHistorial.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPagado)));
                break;
            case "En Deuda":
                btnReportar.setVisibility(View.GONE);
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorDeuda));
                btnVerHistorial.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDeuda)));
                break;
            case "Morosidad Inminente":
                llyEncabezado.setBackgroundColor(getResources().getColor(R.color.colorMorosidad));
                btnVerHistorial.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorMorosidad)));
                break;
        }

        // Definir el formato de entrada (de "dd/MM/yyyy" a "yyyy-MM-dd")
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Definir el formato de salida
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());

        try {
            // Parsear la fecha de texto plano
            Date fechaParseada = formatoEntrada.parse(Fecha);

            // Formatear la fecha en el formato deseado
            fechaFormateada = formatoSalida.format(fechaParseada).toUpperCase();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtDepartamento.setText(Departamento);

        edtDNI.setText(Dni);
        edtFecha.setText(fechaFormateada);
        edtNombresCompletos.setText(Nombre);
        edtTelefono.setText(Telefono);
        edtCosto.setText(Costo);
        edtGarantia.setText(Garantia);

        // Configurar el clic en el EditText para mostrar el DatePickerDialog
        edtFecha.setOnClickListener(v -> mostrarDatePickerDialog());

        bloquearEdts();

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                desbloquearEdts();
                Toast.makeText(Detalle_Departamento.this, "Se Activo la edicion de Datos", Toast.LENGTH_SHORT).show();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Detalle_Departamento.this, edtDNI.getText().toString() + " " + edtNombresCompletos.getText().toString(), Toast.LENGTH_SHORT).show();
                ActualizarDatosInquilino(
                        IDI,
                        edtDNI.getText().toString(),
                        edtNombresCompletos.getText().toString(),
                        edtTelefono.getText().toString(),
                        Fecha);
                bloquearEdts();
            }
        });

        imgRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        btnReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_mensaje_reportar dialog_mensaje_reportar = new Dialog_mensaje_reportar
                        (Detalle_Departamento.this, 1);
                dialog_mensaje_reportar.show();
            }
        });

        txtLiberarDepartamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Estado.equals("Al Día")) {
                    Dialog_liberar_departamento dialogLiberarDepartamento = new Dialog_liberar_departamento
                            (Detalle_Departamento.this, IDC);
                    dialogLiberarDepartamento.show();
                } else if (Estado.equals("Libre")) {
                    Toast.makeText(Detalle_Departamento.this, "El Cuarto esta Libre", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Detalle_Departamento.this, "El cuarto tiene deudas pendientes por Pagar", Toast.LENGTH_LONG).show();
                }

            }


        });

        btnVerHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Detalle_Departamento.this, Detalle_Pagos.class);

                i.putExtra("idC", IDC);
                i.putExtra("dep", Departamento);
                i.putExtra("estado", Estado);

                startActivity(i);
            }
        });

        final String textoPredeterminado = "S/."; // Texto predeterminado que deseas colocar antes del número

        edtCosto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { // Si el EditText pierde el foco
                    String textoActual = edtCosto.getText().toString();
                    if (textoActual.isEmpty()) { // Si no se ha ingresado ningún texto
                        // Agregar el texto predeterminado al principio
                        edtCosto.setText(textoPredeterminado);
                    }
                }
            }
        });

        edtGarantia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { // Si el EditText pierde el foco
                    String textoActual = edtGarantia.getText().toString();
                    if (textoActual.isEmpty()) { // Si no se ha ingresado ningún texto
                        // Agregar el texto predeterminado al principio
                        edtGarantia.setText(textoPredeterminado);
                    }
                }
            }
        });

    }

    public void consultarDatos(String codigo) {
        String urlPagosPendientes = Utils.RUTA_APIS + "ConsultarPagosCuarto.php?codigo_cuarto=" + codigo;
        String urlPagosRealizados = Utils.RUTA_APIS + "ConsultarPagosCuartoPagado.php?codigo_cuarto=" + codigo;

        urlPagosPendientes = urlPagosPendientes.replace(" ", "%20");
        urlPagosRealizados = urlPagosRealizados.replace(" ", "%20");

        request = Volley.newRequestQueue(Detalle_Departamento.this);
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

                // Buscar en la lista de cuartos por el texto ingresado
                ArrayList<Cuotas> listaFiltrada = new ArrayList<>();
                for (Cuotas cuotas : lista) {
                    if (!cuotas.getEstadoCuota().equals("Futuro")) {
                        listaFiltrada.add(cuotas);
                    }
                }
                // Agregar los datos a la lista
                adapter = new listaPagos(listaFiltrada, Detalle_Departamento.this);
                rclPendientes.setLayoutManager(new LinearLayoutManager(Detalle_Departamento.this));
                rclPendientes.setAdapter(adapter);
                // Si ambas consultas han finalizado, ocultar el ProgressDialog
                if (consultasCompletadas == totalConsultas) {
                    progressDialog.hide();
                }
                Conteo = listaFiltrada.size();

                if (Conteo == 1) {
                    lnySuperior2.setVisibility(View.GONE);
                } else {
                    rclPendientes.setVisibility(View.INVISIBLE);
                    lnySuperior2.setVisibility(View.VISIBLE);
                    if (Conteo >= 2) {
                        rclPendientes.setVisibility(View.INVISIBLE);
                        txtMensaje.setText("El inquilino debe 2 o más meses");
                        lnySuperior2.setBackground(getResources().getDrawable(R.drawable.background_resumen_rcv_red));
                    } else if (Conteo <= 0) {
                        if (Estado.equals("Libre")) {
                            rclPendientes.setVisibility(View.INVISIBLE);
                            lnySuperior2.setVisibility(View.VISIBLE);
                            txtMensaje.setText("El Inqulino no registra ninguna deuda");
                            lnySuperior2.setBackground(getResources().getDrawable(R.drawable.background_resumen_rcv_plomo));
                        } else {
                            rclPendientes.setVisibility(View.INVISIBLE);
                            lnySuperior2.setVisibility(View.VISIBLE);
                            txtMensaje.setText("El Inqulino esta Al Día");
                            lnySuperior2.setBackground(getResources().getDrawable(R.drawable.background_resumen_rcv_green));
                        }
                    }
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
                adapter2 = new listaPagos(lista, Detalle_Departamento.this);
                rclPagos.setLayoutManager(new LinearLayoutManager(Detalle_Departamento.this));
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
        progressDialog = new ProgresDialogPersonalizado(Detalle_Departamento.this);
        progressDialog.show();
    }

    public void ActualizarDatosInquilino(String IDI, String DNI, String NOMBRE, String TELEFONO, String FECHAING) {
        String url = Utils.RUTA_APIS + "ActualizarInquilino.php";
        mRequestQueue = Volley.newRequestQueue(Detalle_Departamento.this);
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ActualizarDatosCuarto(
                                IDC,
                                extraerNumeros(edtCosto.getText().toString()),
                                extraerNumeros(edtGarantia.getText().toString()));
                        Toast.makeText(Detalle_Departamento.this, "Se Actualizaron los Datos.", Toast.LENGTH_SHORT).show();
                        // Aquí puedes manejar la respuesta del servidor
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Detalle_Departamento.this, "ERROR " + error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("p_id_inquilino", IDI);
                params.put("p_dni", DNI);
                params.put("p_nombre", NOMBRE);
                params.put("p_telefono", TELEFONO);
                params.put("p_fechaingreso", FECHAING);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        mRequestQueue.add(stringRequest);
    }

    public void ActualizarDatosCuarto(String IDC, String Costo, String Garantia) {
        String url = Utils.RUTA_APIS + "Actualizar_Cuarto_costo_garantia.php";
        mRequestQueue = Volley.newRequestQueue(Detalle_Departamento.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(Detalle_Departamento.this, "Correcto", Toast.LENGTH_SHORT).show();
                        // Aquí puedes manejar la respuesta del servidor
                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Detalle_Departamento.this, "ERROR " + error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_cuarto", IDC);
                params.put("nuevo_costo", Costo);
                params.put("nueva_garantia", Garantia);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        mRequestQueue.add(stringRequest);
    }

    private void bloquearEdts() {
        // Bloquear los EditText

        btnGuardar.setEnabled(false);
        btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLibre3)));

        edtNombresCompletos.setEnabled(false);
        edtNombresCompletos.setTextColor(getResources().getColor(R.color.plomo));
        edtDNI.setEnabled(false);
        edtDNI.setTextColor(getResources().getColor(R.color.plomo));
        edtTelefono.setEnabled(false);
        edtTelefono.setTextColor(getResources().getColor(R.color.plomo));
        edtFecha.setEnabled(false);
        edtFecha.setTextColor(getResources().getColor(R.color.plomo));
        edtCosto.setEnabled(false);
        edtCosto.setTextColor(getResources().getColor(R.color.plomo));
        edtGarantia.setEnabled(false);
        edtGarantia.setTextColor(getResources().getColor(R.color.plomo));
    }

    private void desbloquearEdts() {

        btnGuardar.setEnabled(true);
        btnGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLibre4)));
        // Desbloquear los EditText
        edtNombresCompletos.setEnabled(true);
        edtNombresCompletos.setTextColor(getResources().getColor(R.color.black));
        edtDNI.setEnabled(true);
        edtDNI.setTextColor(getResources().getColor(R.color.black));
        edtTelefono.setEnabled(true);
        edtTelefono.setTextColor(getResources().getColor(R.color.black));
        edtFecha.setEnabled(true);
        edtFecha.setTextColor(getResources().getColor(R.color.black));
        edtCosto.setEnabled(true);
        edtCosto.setTextColor(getResources().getColor(R.color.black));
        edtGarantia.setEnabled(true);
        edtGarantia.setTextColor(getResources().getColor(R.color.black));
    }

    private void mostrarDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear un DatePickerDialog con la fecha actual como predeterminada
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSelected, monthOfYear, dayOfMonthOfMonth) -> {
                    // Actualizar el texto del EditText con la fecha seleccionada
                    String fechaSeleccionada = yearSelected + "-" + (monthOfYear + 1) + "-" + dayOfMonthOfMonth;
                    Fecha = fechaSeleccionada;

                    // Definir el formato de entrada (de "dd/MM/yyyy" a "yyyy-MM-dd")
                    SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    // Definir el formato de salida
                    SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());

                    try {
                        // Parsear la fecha de texto plano
                        Date fechaParseada = formatoEntrada.parse(Fecha);

                        // Formatear la fecha en el formato deseado
                        fechaFormateada = formatoSalida.format(fechaParseada).toUpperCase();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    edtFecha.setText(fechaFormateada);

                }, year, month, dayOfMonth);

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    private String extraerNumeros(String texto) {
        Pattern patron = Pattern.compile("\\d+"); // Patrón para encontrar uno o más dígitos
        Matcher matcher = patron.matcher(texto);

        StringBuilder numeros = new StringBuilder();
        while (matcher.find()) {
            numeros.append(matcher.group());
        }

        return numeros.toString();
    }

    // Función para extraer solo los números del texto
    public String extraerNumeros2(String texto) {
        // Usar una expresión regular para filtrar solo los números
        String numeros = texto.replaceAll("[^\\d]", "");
        return numeros;
    }

    private void volverInicio() {
        Intent intent = new Intent(Detalle_Departamento.this, Inicio.class);
        startActivity(intent);
    }
}