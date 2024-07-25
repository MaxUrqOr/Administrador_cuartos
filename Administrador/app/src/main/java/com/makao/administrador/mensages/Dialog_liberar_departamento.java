package com.makao.administrador.mensages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Inicio;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.activities.Detalle_Departamento;
import com.makao.administrador.activities.Detalle_Pagos;

import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.Ref;

public class Dialog_liberar_departamento extends Dialog {


    String idInquilino;
    private Handler handler = new Handler();
    private View decorView; // Variable de miembro para la vista de decoración
    private int originalHeight; // Variable para almacenar la altura original
    private RequestQueue mRequestQueue;

    public Dialog_liberar_departamento(Context context, String idInquilino) {
        super(context, android.R.style.Theme_DeviceDefault);
        this.idInquilino = idInquilino;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_liberar);

        // Configura la vista de decoración y almacena la altura original
        decorView = getWindow().getDecorView();
        originalHeight = decorView.getHeight();

        getWindow().setWindowAnimations(R.style.SlideUpAnimation);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnAceptar = findViewById(R.id.btnAceptar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Liberado Inquilino: "+idInquilino, Toast.LENGTH_SHORT).show();
                LiberarCuarto(idInquilino);
                dismissWithAnimation();

                Intent intent = new Intent(decorView.getContext(), Inicio.class);
                decorView.getContext().startActivity(intent);


            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Proceso Cancelado", Toast.LENGTH_SHORT).show();
                dismissWithAnimation();
            }
        });
    }

    private void dismissWithAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        animation.setDuration(600); // Ajusta la duración según tu animación "fade_out"

        View view = getWindow().getDecorView();
        view.startAnimation(animation);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 600); // Ajusta el tiempo de retardo según la duración de la animación
    }

    public void LiberarCuarto(String IDC) {
        String url = Utils.RUTA_APIS + "LiberarDepartamento.php";
        mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Cuarto Libre.", Toast.LENGTH_SHORT).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Ocurrio un Problema, Intente luego." + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("inquilino_id", IDC);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        mRequestQueue.add(stringRequest);
    }


}