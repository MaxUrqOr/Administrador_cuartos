package com.makao.inquilino.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.makao.inquilino.Entities.Cuotas;
import com.makao.inquilino.OpcionPago;
import com.makao.inquilino.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class listaCuartos_tipo1 extends RecyclerView.Adapter<listaCuartos_tipo1.Tipo1Holder> {
    private List<Cuotas> listaCuartosTipo1s;
    private Context context;
    String fechaFormateada, fechaPagoFormateada;

    public listaCuartos_tipo1(List<Cuotas> listaCuartosTipo1s, Context contextn) {
        this.listaCuartosTipo1s = listaCuartosTipo1s;
        this.context = context;
    }


    @NonNull
    @Override
    public Tipo1Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.listacuartos_tipo1, parent, false);
        vista.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        vista.setForegroundGravity(Gravity.CENTER);

        return new Tipo1Holder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Tipo1Holder holder, int position) {
        Cuotas cuarto = listaCuartosTipo1s.get(position);

        // Definir el formato de entrada (de "yyyy-MM-dd" a "MMMM yyyy")
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatoSalida = new SimpleDateFormat(" MMMM yyyy", Locale.getDefault());

        try {
            // Parsear la fecha de texto plano
            Date fechaPago = formatoEntrada.parse(cuarto.getFechaVencimiento());

            // Formatear la fecha en el formato deseado
            fechaPagoFormateada = formatoSalida.format(fechaPago);

            // Imprimir la fecha formateada
            System.out.println(fechaPagoFormateada); // Salida: Enero 2024
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convertir la fecha de texto a LocalDate
        LocalDate fechaVencimiento = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaVencimiento = LocalDate.parse(cuarto.getFechaVencimiento());
        }

        // Restar un mes
        LocalDate fechaNuevoVencimiento = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaNuevoVencimiento = fechaVencimiento;
        }

        // Formatear la nueva fecha en el formato deseado (yyyy-MM-dd)

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de '" + "MMMM" + " 'del' yyyy", new Locale("es", "ES"));
        String fechaFormateadaNuevoVencimiento = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaFormateadaNuevoVencimiento = fechaNuevoVencimiento.format(formatter);
        }

        // Aquí colocamos los datos según el ítem
        holder.txtDetapartamento.setText(fechaPagoFormateada);
        if (cuarto.getEstadoPago().equals("Revisando")) {
            holder.btnPagar.setEnabled(false);
            holder.btnPagar.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.naranja_paletadisabled)));


            holder.txtFechaMensualidad.setText(cuarto.getEstadoPago());
            holder.txtFechaMensualidad.setTextColor(ContextCompat.getColor(context, R.color.colorRevisando));

        } else {
            if(cuarto.getEstadoPago().toString().equals("Rechazado")){
                holder.txtFechaMensualidad.setText(cuarto.getEstadoPago());
                holder.txtFechaMensualidad.setTextColor(ContextCompat.getColor(context, R.color.rojoEstado));
            }else{
                holder.txtFechaMensualidad.setText(cuarto.getEstadoCuota());
                holder.txtFechaMensualidad.setTextColor(ContextCompat.getColor(context, R.color.rojoEstado));
            }

        }

        holder.txtMonto.setText("S/." + cuarto.getMontoCuo());
        holder.txtFechaPago.setText("Se vence el " + fechaFormateadaNuevoVencimiento + ".");

        holder.btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recuperar el ID de usuario almacenado en las SharedPreferences
                Intent i = new Intent(context, OpcionPago.class);
                if(cuarto.getEstadoPago().toString().equals("Rechazado")){
                    i.putExtra("idCuota", cuarto.getIdPago() + "");
                }else if(cuarto.getEstadoCuota().toString().equals("Pendiente") || cuarto.getEstadoCuota().toString().equals("Futuro")){
                    i.putExtra("idCuota", cuarto.getIdCuota() + "");
                }
                i.putExtra("Monto", cuarto.getMontoCuo() + "");
                if(cuarto.getEstadoPago().equals("Rechazado")){
                    i.putExtra("Estado", cuarto.getEstadoPago() + "");
                }else{
                    i.putExtra("Estado", cuarto.getEstadoCuota() + "");
                }
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
        ImageView imgDepartamento;
        Button btnPagar;

        public Tipo1Holder(@NonNull View itemView) {
            super(itemView);
            //referencias
            txtDetapartamento = itemView.findViewById(R.id.txtDetapartamento);
            txtFechaMensualidad = itemView.findViewById(R.id.txtFechaMensualidad);
            txtFechaPago = itemView.findViewById(R.id.txtFechaPago);
            txtMonto = itemView.findViewById(R.id.txtMonto);
            imgDepartamento = itemView.findViewById(R.id.imgDepartamento);
            btnPagar = itemView.findViewById(R.id.btnPagar);
        }
    }

}