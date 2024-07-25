 package com.makao.administrador;

 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.os.CountDownTimer;

 import androidx.appcompat.app.AppCompatActivity;

 import com.makao.administrador.Utils.Utils;
 import com.makao.administrador.login.Login;

 public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioDatos", Context.MODE_PRIVATE);
        // Recupera el valor
        Boolean isregistred = sharedPreferences.getBoolean(Utils.USURAIO_REGISTRED, false);

        // Crear un cronómetro que cuenta desde 5 segundos hacia atrás con actualizaciones cada segundo
        CountDownTimer timer = new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Aquí puedes realizar acciones en cada tick del cronómetro si es necesario
            }

            @Override
            public void onFinish() {
                if (isregistred) {
                    Intent intent = new Intent(Splash.this, Inicio.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}