package com.makao.inquilino.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.makao.inquilino.Entities.Cuotas;
import com.makao.inquilino.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class listaPagos extends RecyclerView.Adapter<listaPagos.listaPagosHolder> {
    private List<Cuotas> lista;
    private Context context;
    String fechaFormateada;
    private List<Integer> idPagosSeleccionados = new ArrayList<>(); // Lista para almacenar los ID de los elementos seleccionados
    private Set<Integer> selectedPositions = new HashSet<>();

    public listaPagos(List<Cuotas> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public listaPagosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.listapagos, parent, false);
        vista.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        vista.setForegroundGravity(Gravity.CENTER);

        return new listaPagosHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull listaPagosHolder holder, int position) {
        Cuotas pagos = lista.get(position);

        // Cambiar el color de fondo solo si el elemento está seleccionado
        if (selectedPositions.contains(position)) {
            holder.llyFondo.setBackground(context.getResources().getDrawable(R.drawable.background_resumen_rcv_red)); // Color seleccionado
        } else {
            holder.llyFondo.setBackground(context.getResources().getDrawable(R.drawable.background_resumen_rcv)); // Color no seleccionado (transparente)
        }

        // Definir el formato de entrada (de "dd/MM/yyyy" a "yyyy-MM-dd")
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Definir el formato de salida
        SimpleDateFormat formatoSalida = new SimpleDateFormat("'Mensualidad de' MMMM yyyy", Locale.getDefault());

        try {
            // Parsear la fecha de texto plano
            Date fecha = formatoEntrada.parse(pagos.getFechaVencimiento());

            // Formatear la fecha en el formato deseado
            fechaFormateada = formatoSalida.format(fecha);

            // Imprimir la fecha formateada
            System.out.println(fechaFormateada); // Salida: 2024-10-20
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convertir la fecha de texto a LocalDate
        LocalDate fecha = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fecha = LocalDate.parse(pagos.getFechaPago().toString());
        }

        // Restar un mes
        LocalDate fechaNueva = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaNueva = fecha;
        }


        // Formatear la nueva fecha en el formato deseado (dd-MM-yyyy)
        DateTimeFormatter formatter = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            formatter = DateTimeFormatter.ofPattern("d 'de'"+ " MMMM"+ "' del' yyyy", new Locale("es", "ES"));


        }
        String fechaFormateada2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaFormateada2 = "Pago realizado el "+fechaNueva.format(formatter)+".";
        }
        //aqui colocamos los datos segun el item
        holder.txtFechaPago.setText(fechaFormateada);
        holder.txtCosto.setText("S/." + pagos.getCostoCua());

        holder.txtFechaDeuda.setText(fechaFormateada2);

        holder.txtEstado.setText(pagos.getEstadoCuota());
        // Configurar clics en vistas
        // Manejar el clic en el elemento de RecyclerView para seleccionarlo o deseleccionarlo
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositions.contains(position)) {
                    // Si la posición ya está seleccionada, quítala del conjunto de posiciones seleccionadas
                    selectedPositions.remove(position);
                    // También quita el ID de pago correspondiente de la lista de IDs seleccionados
                    idPagosSeleccionados.remove((Integer) pagos.getIdCuota());
                } else {
                    // Si la posición no está seleccionada, agrégala al conjunto de posiciones seleccionadas
                    selectedPositions.add(position);
                    // También agrega el ID de pago correspondiente a la lista de IDs seleccionados
                    idPagosSeleccionados.add(pagos.getIdCuota());
                }
                // Notificar al adaptador que los datos han cambiado
                notifyDataSetChanged();
            }
        });
        //Mostrar Dialog de Seccion

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class listaPagosHolder extends RecyclerView.ViewHolder {
        //datos escribibles
        TextView txtFechaPago, txtFechaDeuda, txtCosto, txtEstado;
        ConstraintLayout llyFondo;

        public listaPagosHolder(@NonNull View itemView) {
            super(itemView);
            //referencias
            txtFechaPago = itemView.findViewById(R.id.txtFechaPago);
            txtFechaDeuda = itemView.findViewById(R.id.txtFechaDeuda);
            txtCosto = itemView.findViewById(R.id.txtCosto);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            llyFondo = itemView.findViewById(R.id.llyFondo);
        }
    }

    // Método para obtener las posiciones seleccionadas
    public Set<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    // Método para obtener los IDs de pago seleccionados
    public List<Integer> getSelectedIds() {
        return idPagosSeleccionados;
    }
}