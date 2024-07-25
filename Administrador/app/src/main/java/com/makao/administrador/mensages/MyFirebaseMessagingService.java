package com.makao.administrador.mensages;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Verifica si el mensaje contiene datos.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Mensaje de datos recibido: " + remoteMessage.getData());

            // Aquí puedes manejar los datos del mensaje según tus necesidades.
        }

        // Verifica si el mensaje contiene una notificación.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Mensaje de notificación recibido: " + remoteMessage.getNotification().getBody());

            // Aquí puedes manejar la notificación según tus necesidades.
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Nuevo token de registro: " + token);

        // Aquí puedes enviar el token de registro del dispositivo a tu servidor para futuros usos.
    }

}
