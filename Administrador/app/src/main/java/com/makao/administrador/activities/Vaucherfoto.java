 package com.makao.administrador.activities;

 import android.os.Bundle;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

 import com.android.volley.DefaultRetryPolicy;
 import com.android.volley.Request;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.RetryPolicy;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.android.volley.toolbox.Volley;
 import com.bumptech.glide.Glide;
 import com.makao.administrador.R;
 import com.makao.administrador.Utils.Utils;
 import com.makao.administrador.mensages.ProgresDialogPersonalizado;

 import org.json.JSONException;
 import org.json.JSONObject;

 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.Locale;

 public class Vaucherfoto extends AppCompatActivity {
    TextView txtDepartamento, txtFecha, txtInquilino;
    ImageView imgRegresar, imgFoto;
    private RequestQueue requestQueue;
    String Nombre, Fecha, fechaFormateada, Departamento, IDP;
    ProgresDialogPersonalizado progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaucherfoto);

        txtDepartamento = findViewById(R.id.txtDepartamento);
        txtFecha = findViewById(R.id.txtFecha);
        txtInquilino = findViewById(R.id.txtDescripción);

        imgRegresar = findViewById(R.id.imgRegresar);
        imgFoto = findViewById(R.id.imgFoto);

        IDP = getIntent().getStringExtra("IDP");
        Fecha = getIntent().getStringExtra("fecha");
        Nombre = getIntent().getStringExtra("nombre");
        Departamento = getIntent().getStringExtra("dep");

        ConsultarFotoPago(IDP);

        // Definir el formato de entrada
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Definir el formato de salida
        SimpleDateFormat formatoSalida = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy", Locale.getDefault());

        try {
            // Parsear la fecha de texto plano
            Date fecha = formatoEntrada.parse(Fecha);

            // Formatear la fecha en el formato deseado
            fechaFormateada = formatoSalida.format(fecha);

            // Imprimir la fecha formateada
            System.out.println(fechaFormateada); // Salida: 20 de Octubre de 2024
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txtFecha.setText("Subido el " + fechaFormateada);
        txtDepartamento.setText(Departamento);
        txtInquilino.setText("Subido por " + Nombre);

        imgRegresar.setOnClickListener(new View .OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

     public void ConsultarFotoPago(String IDP){
         String url = Utils.RUTA_APIS+"ConsultarFoto.php?pago_id="+IDP;
         // Inicializar la RequestQueue
         RequestQueue requestQueue = Volley.newRequestQueue(this);
         showProgressDialog();

         // Crear una política de reintento personalizada con un tiempo de espera más largo
         int tiempoDeEspera = 20000; // 10 segundos
         RetryPolicy retryPolicy = new DefaultRetryPolicy(tiempoDeEspera, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

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
                                 Glide.with(Vaucherfoto.this)
                                         .load(url)
                                         .into(imgFoto);
                             } else {
                                 // Si la imagen es null, muestra un mensaje de error o realiza otra acción
                                 Toast.makeText(Vaucherfoto.this, "La imagen está vacía", Toast.LENGTH_SHORT).show();
                             }

                         } catch (JSONException e) {
                             progressDialog.hide();
                             Toast.makeText(Vaucherfoto.this, "ERROR TRY", Toast.LENGTH_SHORT).show();
                             e.printStackTrace();
                         }
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         progressDialog.hide();
                         Toast.makeText(Vaucherfoto.this, "Error al obtener la imagen del servidor", Toast.LENGTH_SHORT).show();
                     }
                 });

         // Establecer la política de reintento personalizada
         request.setRetryPolicy(retryPolicy);

         // Agregar la solicitud a la cola
         requestQueue.add(request);
     }

     private void showProgressDialog() {
         progressDialog = new ProgresDialogPersonalizado(Vaucherfoto.this);
         progressDialog.show();
     }
}