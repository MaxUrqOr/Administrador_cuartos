package com.makao.administrador.mensages;

import static android.app.PendingIntent.getActivity;

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

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Inicio;
import com.makao.administrador.R;
import com.makao.administrador.Splash;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.activities.MontoIrregular;
import com.makao.administrador.login.Login;
import com.makao.administrador.navegacion.Home;

import java.util.HashMap;
import java.util.Map;

public class Dialog_rechazar_pago extends Dialog {

    int idInquilino, ID;
    private Handler handler = new Handler();
    private View decorView; // Variable de miembro para la vista de decoración
    private int originalHeight; // Variable para almacenar la altura original
    private RequestQueue mRequestQueue;
    String Departamento, Monto;

    public Dialog_rechazar_pago(Context context, int idInquilino, String Departamento, String Monto, int ID) {
        super(context, android.R.style.Theme_DeviceDefault);
        this.idInquilino = idInquilino;
        this.ID = ID;
        this.Departamento = Departamento;
        this.Monto = Monto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rechazar_pago);

        // Configura la vista de decoración y almacena la altura original
        decorView = getWindow().getDecorView();
        originalHeight = decorView.getHeight();

        getWindow().setWindowAnimations(R.style.SlideUpAnimation);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnFotoI = findViewById(R.id.btnFotoI);
        Button btnMontoI = findViewById(R.id.btnMontoI);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnFotoI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "FOTO INADECUADA", Toast.LENGTH_SHORT).show();
                ActualizarPago(idInquilino + "", "Rechazado", "Foto Inadecuada");
                dismissWithAnimation();

                Intent intent = new Intent(decorView.getContext(), Inicio.class);
                decorView.getContext().startActivity(intent);


            }
        });

        btnMontoI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "MONTO INCOMPLETO Pago: "+idInquilino, Toast.LENGTH_SHORT).show();
//                ActualizarPago(idInquilino+"","Rechazado","Monto Incompleto");
//                dismissWithAnimation();


                Intent intent = new Intent(getContext(), MontoIrregular.class);
                // Si necesitas pasar datos a la nueva actividad, puedes usar putExtra
                intent.putExtra("dep", Departamento);
                intent.putExtra("IDP", idInquilino);
                intent.putExtra("monto", Monto);
                intent.putExtra("IDI", ID);
                // Iniciar la nueva actividad
                getContext().startActivity(intent);
                // Cerrar el diálogo si es necesario
                dismiss();


            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
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

    public void ActualizarPago(String IDP, String Estado, String Razon) {
        String url = Utils.RUTA_APIS + "ActualizarPago.php";
        mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "El pago rechazado fue emitido al inquilino.", Toast.LENGTH_SHORT).show();
                        // Aquí puedes manejar la respuesta del servidor
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "ERROR " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pago_id", IDP);
                params.put("nuevo_estado", Estado);
                params.put("razon_pago", Razon);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        mRequestQueue.add(stringRequest);
    }


}