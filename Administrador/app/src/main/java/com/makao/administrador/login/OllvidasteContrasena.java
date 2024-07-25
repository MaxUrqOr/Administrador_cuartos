package com.makao.administrador.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.makao.administrador.R;

public class OllvidasteContrasena extends AppCompatActivity {

    EditText edtCorreo;
    AppCompatButton btnRegresar, btnSolicitar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ollvidaste_contrasena);

        edtCorreo = findViewById(R.id.edtCorreo);
        btnRegresar = findViewById(R.id.appbuttonRegresarRC);
        btnSolicitar = findViewById(R.id.appbuttonSolicitarCRC);




        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OllvidasteContrasena.this, Completado.class);

                startActivity(i);
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}