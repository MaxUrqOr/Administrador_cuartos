package com.makao.administrador.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makao.administrador.Entities.Pagos;
import com.makao.administrador.R;
import com.makao.administrador.activities.Vaucherfoto;
import com.makao.administrador.mensages.Dialog_aceptar_pago;
import com.makao.administrador.mensages.Dialog_rechazar_pago;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class listaCuartos_tipo1 extends RecyclerView.Adapter<listaCuartos_tipo1.Tipo1Holder>{
    private List<Pagos> listaCuartosTipo1s;
    private Context context;
    String fechaFormateada, fechaFormateada2, fechaFormateada3;
    public listaCuartos_tipo1(List<Pagos> listaCuartosTipo1s, Context context) {
        this.listaCuartosTipo1s = listaCuartosTipo1s;
        this.context = context;
    }


    @NonNull
    @Override
    public Tipo1Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .listacuartos_tipo1, parent, false);
        vista.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        vista.setForegroundGravity(Gravity.CENTER);

        return new Tipo1Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Tipo1Holder holder, int position) {
        Pagos cuartos = listaCuartosTipo1s.get(position);

        // Definir el formato de entrada (de "dd/MM/yyyy" a "yyyy-MM-dd")
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Definir el formato de salida
        SimpleDateFormat formatoSalida = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        // Convertir la fecha de texto a LocalDate
        LocalDate fecha = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fecha = LocalDate.parse(cuartos.getFecha_Vencimiento().toString());
        }

        // Restar un mes
        LocalDate fechaNueva = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaNueva = fecha.minusMonths(1);
        }

        // Formatear la nueva fecha en el formato deseado (dd-MM-yyyy)
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern(" dd' de 'MMM");
        }
        fechaFormateada2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaFormateada2 = fechaNueva.format(formatter);
            fechaFormateada3 = fecha.format(formatter);
        }

        try {
            // Parsear la fecha de texto plano
            Date fechaParseada = formatoEntrada.parse(cuartos.getFecha_Vencimiento());

            // Formatear la fecha en el formato deseado
            fechaFormateada = formatoSalida.format(fechaParseada).toUpperCase();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Aquí colocamos los datos según el item
        holder.txtDetapartamento.setText(cuartos.getCodigoCua()+" Departamento "+cuartos.getPisoCua());
        holder.txtFechaMensualidad.setText(fechaFormateada);
        holder.txtMonto.setText("S/."+cuartos.getMonto_Cuo()+" soles");
        holder.txtFechaPago.setText("Pago del"+fechaFormateada2+" al"+fechaFormateada3);

        // Mostrar Dialog de Sección
        holder.btnPagoVerificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_aceptar_pago dialogAceptarPago = new Dialog_aceptar_pago
                        (context,cuartos.getIdPago());
                dialogAceptarPago.show();
            }
        });

        // Mostrar Dialog de Sección
        holder.btnPagoInvalido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_rechazar_pago dialogRechazarPago = new Dialog_rechazar_pago
                        (
                                context,
                                cuartos.getIdPago(),
                                cuartos.getCodigoCua() + " " + cuartos.getPisoCua(),
                                cuartos.getMonto_Cuo(),
                                cuartos.getIdInquilino());
                dialogRechazarPago.show();
            }
        });

        holder.imgVerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Vaucherfoto.class);
                i.putExtra("IDP", cuartos.getIdPago()+"");
                i.putExtra("fecha", cuartos.getFechaPago());
                i.putExtra("nombre", cuartos.getNombreInqui());
                i.putExtra("dep", cuartos.getCodigoCua() + " " + cuartos.getPisoCua());
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listaCuartosTipo1s.size();
    }

    public class Tipo1Holder extends RecyclerView.ViewHolder {
        //datos escribibles
        TextView txtDetapartamento, txtFechaMensualidad, txtFechaPago, txtMonto;
        ImageView imgDepartamento, imgVerFoto;
        Button btnPagoInvalido, btnPagoVerificado;
        public Tipo1Holder(@NonNull View itemView) {
            super(itemView);
            //referencias
            txtDetapartamento = itemView.findViewById(R.id.txtDetapartamento);
            txtFechaMensualidad = itemView.findViewById(R.id.txtFechaMensualidad);
            txtFechaPago = itemView.findViewById(R.id.txtFechaPago);
            txtMonto = itemView.findViewById(R.id.txtMonto);
            imgDepartamento = itemView.findViewById(R.id.imgDepartamento);
            btnPagoInvalido = itemView.findViewById(R.id.btnPagoInvalido);
            btnPagoVerificado = itemView.findViewById(R.id.btnPagoVerificado);
            imgVerFoto = itemView.findViewById(R.id.imgVerFoto);
        }
    }
}
