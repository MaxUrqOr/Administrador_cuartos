package com.makao.inquilino.login;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.makao.inquilino.R;

public class RecuperarCActivity extends AppCompatActivity {

    EditText edtCorreo;
    AppCompatButton btnRegresar, btnSolicitar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_cactivity);

        edtCorreo = findViewById(R.id.edtCorreo);
        btnRegresar = findViewById(R.id.appbuttonRegresarRC);
        btnSolicitar = findViewById(R.id.appbuttonSolicitarCRC);

    }
}