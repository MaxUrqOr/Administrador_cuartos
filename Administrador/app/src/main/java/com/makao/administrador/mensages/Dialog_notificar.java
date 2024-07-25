package com.makao.administrador.mensages;

import android.app.Dialog;
import android.content.Context;
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

import com.android.volley.RequestQueue;
import com.makao.administrador.R;

public class Dialog_notificar extends Dialog {

    String idAdmin;
    private Handler handler = new Handler();
    private View decorView; // Variable de miembro para la vista de decoración
    private int originalHeight; // Variable para almacenar la altura original
    private RequestQueue mRequestQueue;
    public Dialog_notificar(Context context, String idAdmin) {
        super(context, android.R.style.Theme_DeviceDefault);
        this.idAdmin = idAdmin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_notificar);

        // Configura la vista de decoración y almacena la altura original
        decorView = getWindow().getDecorView();
        originalHeight = decorView.getHeight();

        getWindow().setWindowAnimations(R.style.SlideUpAnimation);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnNotificar = findViewById(R.id.btnNotificar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnNotificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Notificar Inquilino: "+idAdmin, Toast.LENGTH_SHORT).show();

                dismissWithAnimation();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
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

}