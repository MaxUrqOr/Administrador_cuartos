package com.makao.inquilino.mensages;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.makao.inquilino.R;

public class ProgresDialogPersonalizado extends Dialog {
    private TextView txtMensaje;
    private int dotCount = 0;
    private Handler handler;
    private Runnable updateTextRunnable;
    public ProgresDialogPersonalizado(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //PANTALLA
        //getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //NO TOCAR

        setContentView(R.layout.progress_dialog_personalizado);
        setCancelable(false);

        txtMensaje = findViewById(R.id.txtMensaje);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        handler = new Handler();

        // Runnable para actualizar el mensaje de texto
        updateTextRunnable = new Runnable() {
            @Override
            public void run() {
                updateText();
                handler.postDelayed(this, 1000); // Programa la actualización cada segundo
            }
        };

        handler.post(updateTextRunnable); // Inicia el proceso de actualización del mensaje de texto
    }

    private void updateText() {
        dotCount++; // Incrementa el contador de puntos
        if (dotCount > 3) {
            dotCount = 0; // Reinicia el contador cuando llega a 4
        }

        String dots = "";
        for (int i = 0; i < dotCount; i++) {
            dots += " ."; // Agrega un punto por cada valor de dotCount
        }

        txtMensaje.setText("Cargando" + dots);
    }

}
