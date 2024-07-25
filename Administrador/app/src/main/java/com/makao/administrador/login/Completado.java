package com.makao.administrador.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.makao.administrador.R;

public class Completado extends AppCompatActivity {
    Button btnRegresarInicio, btnContinuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completado);

        btnRegresarInicio = findViewById(R.id.btnRegresarInicio);
        btnContinuar = findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Completado.this, Login.class);

                startActivity(i);
                finish();
            }
        });

        btnRegresarInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Completado.this, Login.class);

                startActivity(i);
                finish();
            }
        });
    }
}