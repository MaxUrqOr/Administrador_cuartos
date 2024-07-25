package com.makao.administrador.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.makao.administrador.Inicio;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MontoIrregular extends AppCompatActivity {
    ImageView imgFoto;
    String Departamento, Monto;
    double montoMensualidad, montoFoto, diferencia;
    int idPago, idInq, idAdm;
    private RequestQueue requestQueue;
    ProgresDialogPersonalizado progressDialog;
    private RequestQueue mRequestQueue;

    @Override
    protected void onResume() {
        super.onResume();
        ConsultarFotoPago(idPago+"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_irregular);

        // Obteniendo referencias a las vistas utilizando findViewById()
        ImageView imgRegresar = findViewById(R.id.imgRegresar);
        TextView txtDepartamento = findViewById(R.id.txtDepartamento);
        imgFoto = findViewById(R.id.imgFoto);
        TextView txtDescripción = findViewById(R.id.txtDescripción);
        EditText edtMontoFoto = findViewById(R.id.edtMontoFoto);
        EditText edtMontoMensualidad = findViewById(R.id.edtMontoMensualidad);
        TextView tvTotal = findViewById(R.id.tvTotal);
        Button btnActualizarMonto = findViewById(R.id.btnActualizarMonto);

        idPago = getIntent().getIntExtra("IDP",0);
        idInq = getIntent().getIntExtra("IDI",0);
        Departamento = getIntent().getStringExtra("dep");
        Monto = getIntent().getStringExtra("monto");

        // Obtener las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioDatos", this.MODE_PRIVATE);

        // Obtener el valor del ID de usuario almacenado en las preferencias compartidas
        idAdm = sharedPreferences.getInt(Utils.USURAIO_ID, -1); // -1 es el valor predeterminado si no se encuentra el ID de usuario

        //Recupera datos despues de crear actividad
        //ConsultarFotoPago(idPago+"");

        txtDepartamento.setText(Departamento);

        edtMontoMensualidad.setText("S/."+Monto);
        edtMontoMensualidad.setEnabled(false);

        edtMontoFoto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Verificar si el texto ingresado en edtMontoFoto no está vacío
                if (!TextUtils.isEmpty(editable)) {
                    // Obtener el valor del monto de la mensualidad desde el EditText
                    String montoMensualidadString = edtMontoMensualidad.getText().toString();

                    // Verificar si el monto de la mensualidad también está ingresado
                    if (!TextUtils.isEmpty(montoMensualidadString)) {
                        // Convertir los valores a números
                        montoMensualidad = Double.parseDouble(montoMensualidadString.replace("S/.", ""));
                        montoFoto = Double.parseDouble(editable.toString().replace("S/.", ""));

                        // Calcular la diferencia entre los montos
                        diferencia = montoMensualidad - montoFoto;

                        // Mostrar la diferencia en el TextView tvTotal
                        tvTotal.setText("S/." + String.format("%.0f", diferencia));
                    }
                }
            }
        });

        imgRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnActualizarMonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Después de inflar el diseño del diálogo
                Dialog dialog = new Dialog(MontoIrregular.this);
                dialog.setContentView(R.layout.dialog_preguntar_monto_irregular);

                // Referenciar los botones dentro del diálogo
                Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
                Button btnCancelar = dialog.findViewById(R.id.btnCancelar);

                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        ActualizarPago(idPago+"","Aprobado","Todo Correcto");
                    }
                });

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                // Mostrar el diálogo
                dialog.show();
            }
        });
    }

    public void ConsultarFotoPago(String IDP){
        String url = Utils.RUTA_APIS+"ConsultarFoto.php?pago_id="+IDP;
        // Inicializar la RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        showProgressDialog();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.hide();
                        try {
                            // Verificar si la imagen no es null
                            if (!response.isNull("foto_base64")) {
                                // Extraer la cadena base64 de la respuesta JSON
                                String url = Utils.RUTA_APIS + "Imagenes/" + response.getString("url_foto");

                                // Usa Glide para cargar la imagen desde la URL en el ImageView
                                Glide.with(MontoIrregular.this)
                                        .load(url)
                                        .into(imgFoto);
                            } else {
                                // Si la imagen es null, muestra un mensaje de error o realiza otra acción
                                Toast.makeText(MontoIrregular.this, "Problemas en cargar la imagen.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.hide();
                            Toast.makeText(MontoIrregular.this, "ERROR TRY", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(MontoIrregular.this, "Error al obtener la imagen del servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola
        requestQueue.add(request);
    }
    private void showProgressDialog() {
        progressDialog = new ProgresDialogPersonalizado(MontoIrregular.this);
        progressDialog.show();
    }

    public void ActualizarCuota(String IDI, String IDA, String NombreSol, String Descripcion, String Fecha, String Estado, String Monto){
        String url = Utils.RUTA_APIS+"AgregarSolicitud.php";
        mRequestQueue = Volley.newRequestQueue(MontoIrregular.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MontoIrregular.this, "Deuda actualizada emitida al inquilino", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MontoIrregular.this, Inicio.class);
                        startActivity(i);
                        finish();
                        // Aquí puedes manejar la respuesta del servidor
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MontoIrregular.this, "ERROR "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_inquilino", IDI);
                params.put("id_administrador", IDA);
                params.put("nombre_sol", NombreSol);
                params.put("descripcion_sol", Descripcion);
                params.put("fecha_sol", Fecha);
                params.put("estado_sol", Estado);
                params.put("monto_sol", Monto);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        mRequestQueue.add(stringRequest);
    }

    public void ActualizarPago(String IDP, String Estado, String Razon){
        String url = Utils.RUTA_APIS+"ActualizarPago.php";
        mRequestQueue = Volley.newRequestQueue(MontoIrregular.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MontoIrregular.this, "Pagos Actualizados.", Toast.LENGTH_SHORT).show();
                        if(diferencia<=0){
                            ActualizarCuota(
                                    idInq+"",
                                    idAdm+"",
                                    "Descuento",
                                    "Se desconto a la siguiente cuota por exceso de pago anterior",
                                    "2024-10-12",
                                    "Procesado",
                                    Math.abs(diferencia)+"");
                        }else{
                            ActualizarCuota(
                                    idInq+"",
                                    idAdm+"",
                                    "Adicionado",
                                    "Se adiciono a la siguiente cuota por falta de pago anterior",
                                    "2024-10-12",
                                    "Procesado",
                                    Math.abs(diferencia)+"");
                        }
                        // Aquí puedes manejar la respuesta del servidor
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MontoIrregular.this, "ERROR "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pago_id", IDP);
                params.put("nuevo_estado", Estado);
                params.put("razon_pago", Razon);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        mRequestQueue.add(stringRequest);
    }
}
