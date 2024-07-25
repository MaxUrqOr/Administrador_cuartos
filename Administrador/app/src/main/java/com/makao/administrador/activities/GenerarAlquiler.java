package com.makao.administrador.activities;

import static androidx.core.view.ViewCompat.setBackgroundTintList;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Entities.Cuartos;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.adapters.listaPagos;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerarAlquiler extends AppCompatActivity {

    private List<Cuartos> listaCuartos = new ArrayList<>();
    private List<String> nombresCuartos = new ArrayList<>();
    AutoCompleteTextView searchView;
    ImageView imgAtras;
    EditText
            edtNombreCompleto,
            edtDNI,
            edtFechaIngreso,
            edtCelular,
            edtMensualidad,
            edtNombreUsuario,
            edtContrasena,
            edtGarantia;
    AppCompatButton btnGenerarAlquiler, btnAgregarInquilino;
    private Calendar calendar;
    private ProgresDialogPersonalizado progressDialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayAdapter<String> adapter;
    listaPagos adapter2;
    Cuartos cuartos;
    int codigo;
    String idUsuario, idCuarto;
    private RequestQueue requestQueue;

    @Override
    protected void onResume() {
        super.onResume();
        //recuperar cuartos libres
        consulta();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_alquiler);
        // Asignar referencias a las vistas
        imgAtras = findViewById(R.id.imageView);

        searchView = findViewById(R.id.srcDepartamentosLibres);

        edtNombreCompleto = findViewById(R.id.edtNombreCompleto);
        edtDNI = findViewById(R.id.edtDNI);
        edtFechaIngreso = findViewById(R.id.edtFechaIngreso);
        edtCelular = findViewById(R.id.edtCelular);
        edtMensualidad = findViewById(R.id.edtMensualidad);
        edtNombreUsuario = findViewById(R.id.edtNombreUsuario);
        edtContrasena = findViewById(R.id.edtContrasena);
        edtGarantia = findViewById(R.id.edtGarantia);

        calendar = Calendar.getInstance();

        btnGenerarAlquiler = findViewById(R.id.btnGenerarAlquiler);
        btnAgregarInquilino = findViewById(R.id.btnAgregarInquilino);

        btnGenerarAlquiler.setEnabled(false);
        btnGenerarAlquiler.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.naranja_paleta2)));

        // Configurar el clic en el EditText para mostrar el DatePickerDialog
        edtFechaIngreso.setOnClickListener(v -> mostrarDatePickerDialog());

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

        btnAgregarInquilino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el texto de cada campo de entrada
                String dni = edtDNI.getText().toString();
                String nombreCompleto = edtNombreCompleto.getText().toString();
                String contrasena = edtContrasena.getText().toString();
                String nombreUsuario = edtNombreUsuario.getText().toString();
                String celular = edtCelular.getText().toString();
                String fechaIngreso = edtFechaIngreso.getText().toString();
                // Verificar si alguno de los campos está vacío
                if (dni.isEmpty() || nombreCompleto.isEmpty() || contrasena.isEmpty() ||
                        nombreUsuario.isEmpty() || celular.isEmpty() || fechaIngreso.isEmpty()) {
                    // Al menos uno de los campos está vacío, muestra un mensaje de error o toma alguna acción adecuada
                    Toast.makeText(GenerarAlquiler.this, "Debe Rellenar todos los Datos del Inquilino.", Toast.LENGTH_SHORT).show();

                } else {
                    // Todos los campos están llenos, procede con el procesamiento de los datos
                    insertarNuevoInquilino(
                            edtDNI.getText().toString(),
                            edtNombreCompleto.getText().toString(),
                            edtContrasena.getText().toString(),
                            edtNombreUsuario.getText().toString(),
                            edtCelular.getText().toString(),
                            edtFechaIngreso.getText().toString());
                    btnGenerarAlquiler.setEnabled(true);
                }

            }
        });

        btnGenerarAlquiler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                insertarNuevoInquilinoaCuarto(idUsuario, idCuarto);
            }

        });

    }

    private void consulta() {
        String url = Utils.RUTA_APIS + "ConsultarTodosCuartosLibres.php";
        url = url.replace(" ", "%20");
        request = Volley.newRequestQueue(GenerarAlquiler.this);
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
                        cuartos.setCostoCua(jsonObject.getInt("COSTO_CUA"));
                        cuartos.setPisoCua(jsonObject.getString("PISO_CUA"));
                        cuartos.setCodigoCua(jsonObject.getString("CODIGO_CUA"));
                        listaCuartos.add(cuartos);
                    }
                    // Crear un adaptador y configurarlo con los nombres de los cuartos
                    adapter = new ArrayAdapter<>(GenerarAlquiler.this, android.R.layout.simple_dropdown_item_1line, nombresCuartos);
                    // Establecer el adaptador en el AutoCompleteTextView
                    searchView.setAdapter(adapter);
                } catch (JSONException e) {
                    Toast.makeText(GenerarAlquiler.this, "No hay Cuartos disponibles en este momento", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GenerarAlquiler.this, "Ocurrio un Problema, Intentelo más tarde.", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        request.add(jsonObjectRequest);
    }

    private void buscarCuarto(String letras, String numeros) {
        // Buscar en la lista de cuartos por el texto ingresado

        for (Cuartos cuarto : listaCuartos) {
            if (cuarto.getPisoCua().equals(numeros) && cuarto.getCodigoCua().toString().equals(letras)) {
                // Coincidencia encontrada, realizar alguna acción con el cuarto encontrado
                edtMensualidad.setText(cuarto.getCostoCua() + "");
                edtGarantia.setText(cuarto.getGarantia_Cua() + "");
                codigo = cuarto.getIdCuarto();
                idCuarto = cuarto.getIdCuarto() + "";
                break; // Terminar el bucle una vez que se encuentre una coincidencia
            }
        }
    }

    private void insertarNuevoInquilino(String DNI, String Nombre, String Contrasena,
                                        String Nombre_Usuario, String Telefono, String Fecha) {
        // URL del archivo PHP en tu servidor
        String url = Utils.RUTA_APIS + "AgregarInquilinoNuevo.php";
        showProgressDialog();

        // Crear una solicitud POST utilizando Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor
                        progressDialog.hide();
                        idUsuario = response;
                        if (response.equals("El nombre de usuario ya existe")) {
                            Toast.makeText(GenerarAlquiler.this, "El nombre de Usuario ya Existe", Toast.LENGTH_LONG).show();
                        } else {
                            btnAgregarInquilino.setEnabled(false);
                            btnGenerarAlquiler.setEnabled(true);
                            btnGenerarAlquiler.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.naranja_paleta)));
                            btnAgregarInquilino.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.morado_oscuro2)));
                            Toast.makeText(GenerarAlquiler.this, "Inquilino Agregado. Ahora puede Generar el Alquiler.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        progressDialog.hide();
                        Toast.makeText(GenerarAlquiler.this, "Ocurrio un Problema, Intentelo más tarde.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para los parámetros de la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("dni", DNI);
                params.put("nombre", Nombre);
                params.put("contrasena", Contrasena);
                params.put("nomusuario", Nombre_Usuario);
                params.put("telefono", Telefono);
                params.put("fechaingreso", Fecha);
                params.put("pagos", "0");
                params.put("deudas", "0");
                params.put("solicitudes", "0");
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void insertarNuevoInquilinoaCuarto(String IDU, String IDC) {
        // URL del archivo PHP en tu servidor
        String url = Utils.RUTA_APIS + "AsignarCuartoInquilino.php";
        showProgressDialog();

        // Crear una solicitud POST utilizando Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor

                        ActualizarDatosCuarto(
                                idCuarto,
                                edtMensualidad.getText().toString(),
                                edtGarantia.getText().toString());

//                        Toast.makeText(GenerarAlquiler.this, "Mensaje: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        progressDialog.hide();
                        Toast.makeText(GenerarAlquiler.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para los parámetros de la solicitud POST
                Map<String, String> params = new HashMap<>();
                params.put("id_inquilino", IDU);
                params.put("id_cuarto", IDC);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showProgressDialog() {
        progressDialog = new ProgresDialogPersonalizado(GenerarAlquiler.this);
        progressDialog.show();
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
                    edtFechaIngreso.setText(fechaSeleccionada);
                }, year, month, dayOfMonth);

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    public void ActualizarDatosCuarto(String IDC, String Costo, String Garantia) {
        String url = Utils.RUTA_APIS + "Actualizar_Cuarto_costo_garantia.php";
        request = Volley.newRequestQueue(GenerarAlquiler.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GenerarAlquiler.this, "Se Genero un Nuevo Alquiler.", Toast.LENGTH_LONG).show();
                        // Aquí puedes manejar la respuesta del servidor
                        progressDialog.hide();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                limpiarCampos();

                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                recreate();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }, 1000); // 3000 milisegundos = 3 segundos


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GenerarAlquiler.this, "ERROR " + error.toString(), Toast.LENGTH_SHORT).show();
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
        request.add(stringRequest);
    }

    private void limpiarCampos() {
        edtNombreCompleto.setText("");
        edtDNI.setText("");
        edtFechaIngreso.setText("");
        edtCelular.setText("");
        edtMensualidad.setText("");
        edtNombreUsuario.setText("");
        edtContrasena.setText("");
        edtGarantia.setText("");
        searchView.setText("");
    }

}
