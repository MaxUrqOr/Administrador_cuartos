package com.makao.inquilino.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makao.inquilino.MainActivity;
import com.makao.inquilino.R;
import com.makao.inquilino.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsuario, edtContrasena;
    TextView txtRecuperar;
    AppCompatButton btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtContrasena = findViewById(R.id.edtContrasena);
        txtRecuperar = findViewById(R.id.tvOlvidasteC);
        btnIngresar = findViewById(R.id.appCompatButtonIngresarLogin);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUsuario.getText().toString().isEmpty() || edtContrasena.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    ValidarUsuario(edtUsuario.getText().toString(), edtContrasena.getText().toString());

                }
            }
        });
    }

    public void ValidarUsuario(final String usuario, final String contrasena) {
        // URL del servidor PHP
        String url = Utils.RUTA_APIS + "ValidarInquilinoUsuarioContrasena.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesa la respuesta del servidor
                        procesarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja errores de la solicitud
                        Toast.makeText(LoginActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("p_usuario", usuario);
                params.put("p_contrasena", contrasena);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void procesarRespuesta(String response) {
        try {
            // Extraer solo la parte JSON de la respuesta
            int jsonStartIndex = response.indexOf('{');
            int jsonEndIndex = response.lastIndexOf('}') + 1;

            if (jsonStartIndex != -1 && jsonEndIndex != -1) {
                String jsonResponse = response.substring(jsonStartIndex, jsonEndIndex);

                // Convertir la parte JSON a JSONObject
                JSONObject jsonObject = new JSONObject(jsonResponse);
                int resultado = jsonObject.getInt("p_valido");

                if (resultado == 1) {
                    // La validación es exitosa, obtén el ID de usuario
                    int idUsuario = jsonObject.getInt("p_ID_Usuario");

                    // Almacena el ID de usuario en las SharedPreferences para su uso posterior
                    SharedPreferences sharedPreferences = getSharedPreferences("UsuarioDatos", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Utils.USURAIO_ID, idUsuario);
                    editor.putBoolean(Utils.USURAIO_REGISTRED, true);
                    editor.apply();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    // La validación no fue exitosa, muestra un mensaje de error
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Respuesta no contiene un JSON válido
                Toast.makeText(this, "Respuesta inválida del servidor", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
        }
    }
}