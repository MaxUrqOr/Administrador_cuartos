package com.makao.inquilino;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makao.inquilino.Utils.Utils;
import com.makao.inquilino.mensages.ProgresDialogPersonalizado;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OpcionPago extends AppCompatActivity {

    ImageView imgFoto;
    Bitmap photo;

    private ProgresDialogPersonalizado progressDialog;
    String IdCuota, montoPago, estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opcion_pago);

        Button btnSubir = findViewById(R.id.btnSubirFoto);
        Button btnTomar = findViewById(R.id.btnTomarFoto);
        Button btnLicoreria = findViewById(R.id.btnPagarLicoreria);
        ImageView imgAtras = findViewById(R.id.imgAtras);
        Button btnEnviar = findViewById(R.id.btnEnviar);


        imgFoto = findViewById(R.id.imgFoto);

        IdCuota = getIntent().getStringExtra("idCuota");
        montoPago = getIntent().getStringExtra("Monto");
        estado = getIntent().getStringExtra("Estado");

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lanzar la actividad de la galería para seleccionar una imagen
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((Activity) OpcionPago.this).startActivityForResult(galleryIntent, Utils.REQUEST_GALLERY);
            }
        });

        btnTomar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lanzar la actividad de la cámara para tomar una foto
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ((Activity) OpcionPago.this).startActivityForResult(cameraIntent, Utils.REQUEST_CAMERA);
            }
        });

        btnLicoreria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OpcionPago.this, PasosPagoLicoreria.class);
                startActivity(i);
            }
        });

        imgAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                Date date = new Date();

                // Crear un objeto SimpleDateFormat con el formato deseado
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                // Formatear la fecha actual con el formato especificado
                String currentDate = sdf.format(date);

                if (estado.equals("Rechazado")) {
                    ActualizarFoto(IdCuota, currentDate, photo);
                } else {
//                    Toast.makeText(OpcionPago.this, ""+IdCuota, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(OpcionPago.this, ""+currentDate, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(OpcionPago.this, ""+montoPago, Toast.LENGTH_SHORT).show();
                    Toast.makeText(OpcionPago.this, "La foto se esta cargando, espere un momento.", Toast.LENGTH_SHORT).show();
                    EnviarFoto(IdCuota, currentDate, montoPago, photo);

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificar si el resultado es de la galería
        if (requestCode == Utils.REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            // Obtener la URI de la imagen seleccionada
            Uri selectedImageUri = data.getData();

            try {
                // Convertir la URI en un Bitmap
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                // Ahora puedes utilizar el Bitmap como desees
                // Por ejemplo, puedes mostrarlo en un ImageView
                imgFoto.setImageBitmap(photo);

                // También puedes enviar el Bitmap al servidor, guardar en la base de datos, etc.
                // Luego, puedes enviar el Bitmap al servidor utilizando Volley, Retrofit u otra biblioteca de red.

            } catch (IOException e) {
                e.printStackTrace();
                // Manejar cualquier error que ocurra durante la conversión
            }
        }

        // Verificar si el resultado es de la cámara
        else if (requestCode == Utils.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            // Obtener la imagen capturada
            photo = (Bitmap) data.getExtras().get("data");

            // Establecer la imagen capturada en el ImageView
            imgFoto.setImageBitmap(photo);
        }
    }

    public void EnviarFoto(String IDCuo, String Fecha, String Monto, Bitmap tuBitmap) {
        if (tuBitmap == null) {
            // Manejar el caso en el que tuBitmap es nulo
            Toast.makeText(this, "Cargue o Tome una fotografía.", Toast.LENGTH_SHORT).show();
            return;
        }


        Bitmap bitmap = tuBitmap; // Tu Bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Comprimir el Bitmap a JPEG
        byte[] byteArrayImage = baos.toByteArray(); // Convertir a byte array
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT); // Convertir a base64

        int tiempoEspera = 10000; // 10 segundos

        String url = Utils.RUTA_APIS + "InsertarPagocFoto.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showProgressDialog();
                        // Manejar la respuesta del servidor

                        Toast.makeText(OpcionPago.this, "Se envio la foto", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(OpcionPago.this, MainActivity.class);
                        startActivity(i);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Toast.makeText(OpcionPago.this, "Problemas con la conectividad, intentelo más tarde." + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_CUOTA", IDCuo);
                params.put("FECHA_PAG", Fecha);
                params.put("MONTO_PAG", Monto);
                params.put("FOTO_PAG", encodedImage); // La imagen en base64
                params.put("ESTADO_PAG", "Revisando");
                params.put("RAZON_PAGO", "Pago de Mensualidad");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                tiempoEspera,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void ActualizarFoto(String IDCuo, String Fecha, Bitmap tuBitmap) {
        if (tuBitmap == null) {
            // Manejar el caso en el que tuBitmap es nulo
            Toast.makeText(this, "Cargue o Tome una fotografía.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "La foto se esta cargando, espere un momento.", Toast.LENGTH_SHORT).show();
        Bitmap bitmap = tuBitmap; // Tu Bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Comprimir el Bitmap a JPEG
        byte[] byteArrayImage = baos.toByteArray(); // Convertir a byte array
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT); // Convertir a base64

        int tiempoEspera = 10000; // 10 segundos

        String url1 = Utils.RUTA_APIS + "ActualizarFotoPagoRechazado.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor

                        Toast.makeText(OpcionPago.this, "Se envio la foto", Toast.LENGTH_SHORT).show();
                        Log.i("RESPONSE", response);
//                        progressDialog.hide();
                        Intent i = new Intent(OpcionPago.this, MainActivity.class);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Toast.makeText(OpcionPago.this, "Problemas de conectividad, intentelo más tarde." + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID_CUOTA", IDCuo); //esto es el id de pago
                params.put("FECHA_PAG", Fecha);
                params.put("FOTO_PAG", encodedImage); // La imagen en base64
                params.put("ESTADO_PAG", "Revisando");
                params.put("RAZON_PAGO", "Reenvio de Foto");
                //Log para verificar los parámetros
                Log.i("PARAMS", params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                tiempoEspera,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void showProgressDialog() {
        progressDialog = new ProgresDialogPersonalizado(OpcionPago.this);
        progressDialog.show();
    }
}