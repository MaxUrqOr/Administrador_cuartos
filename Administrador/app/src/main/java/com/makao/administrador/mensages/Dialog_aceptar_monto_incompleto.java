package com.makao.administrador.mensages;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.makao.administrador.R;

public class Dialog_aceptar_monto_incompleto extends DialogFragment {


    Button btnAceptar, btnCancelar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        // Crear un constructor de diálogo utilizando el constructor de AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflar el diseño del diálogo
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_preguntar_monto_irregular, null);

        // Obtener referencias a las vistas del diseño
        btnAceptar = view.findViewById(R.id.btnAceptar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        // Configurar el diseño personalizado en el constructor de diálogo
        builder.setView(view);

        // Crear y devolver el diálogo
        return builder.create();
    }

}
