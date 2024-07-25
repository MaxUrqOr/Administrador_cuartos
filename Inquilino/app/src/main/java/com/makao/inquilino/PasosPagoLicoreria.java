package com.makao.inquilino;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
public class PasosPagoLicoreria extends AppCompatActivity {

    AppCompatButton btnVerMapa;
    AppCompatButton btnRegresar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pasos_pago_licoreria); // Asegúrate de que el nombre del layout sea el correcto

        btnVerMapa = findViewById(R.id.btnVerMapa);
        btnRegresar = findViewById(R.id.btnRegresar);

        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PasosPagoLicoreria.this, MapaLicoreria.class);
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