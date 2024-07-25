package com.makao.administrador.adapters;//package com.example.departamentos.adapters;
//
//import android.content.Context;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.departamentos.Entities.Solicitudes;
//import com.example.departamentos.R;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//public class listaSolicitudes extends RecyclerView.Adapter<listaSolicitudes.SolicitudesHolder>{
//    private List<Solicitudes> lista;
//    private Context context;
//
//    public listaSolicitudes(List<Solicitudes> lista, Context context) {
//        this.lista = lista;
//        this.context = context;
//    }
////
//    @NonNull
//    @Override
//    public SolicitudesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout
//                .lista_solicitudes, parent, false);
//        vista.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        vista.setForegroundGravity(Gravity.CENTER);
//
//        return new SolicitudesHolder(vista);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SolicitudesHolder holder, int position) {
//        Solicitudes solicitudes = lista.get(position);
//
//        holder.txtDepartamento.setText(solicitudes.getCodigoCua()+" "+solicitudes.getHabitacionCua());
//        holder.txtInquilino.setText(solicitudes.getNombreUsu());
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//        try {
//            // Convertir la fecha de texto a objeto Date
//            Date fechaDada = sdf.parse(solicitudes.getFechaSolicitud());
//
//            // Obtener la fecha actual
//            Date fechaActual = new Date();
//
//            // Calcular la diferencia en milisegundos
//            long diferenciaEnMs = fechaDada.getTime() - fechaActual.getTime();
//
//            // Convertir la diferencia de milisegundos a días
//            long diferenciaEnDias = TimeUnit.DAYS.convert(diferenciaEnMs, TimeUnit.MILLISECONDS) * -1;
//
//            // Imprimir la diferencia en días
//            System.out.println("Diferencia en días: " + diferenciaEnDias);
//            holder.txtFechaSolicitud.setText(diferenciaEnDias+" días atras");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        holder.txtMeses.setText("-");
//        holder.txtMontoAdelanto.setText("S/. "+solicitudes.getMontoSolicitud());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return lista.size();
//    }
//
//    public class SolicitudesHolder extends RecyclerView.ViewHolder {
//        TextView txtDepartamento,txtInquilino,txtFechaSolicitud,txtTipoSolicitud,txtMeses,
//                txtMontoAdelanto,txtTipoSolicitudtextoplano,txtFecharecibido;
//        Button btnRechazar,btnAceptar;
//        public SolicitudesHolder(@NonNull View itemView) {
//            super(itemView);
//
//            txtDepartamento = itemView.findViewById(R.id.txtDepartamento);
//            txtInquilino = itemView.findViewById(R.id.txtInquilino);
//            txtFechaSolicitud = itemView.findViewById(R.id.txtFechaSolicitud);
//            txtTipoSolicitud = itemView.findViewById(R.id.txtTipoSolicitud);
//            txtMeses = itemView.findViewById(R.id.txtMeses);
//            txtMontoAdelanto = itemView.findViewById(R.id.txtMontoAdelanto);
//            txtTipoSolicitudtextoplano = itemView.findViewById(R.id.txtTipoSolicitudtextoplano);
//            txtFecharecibido = itemView.findViewById(R.id.txtFecharecibido);
//            btnRechazar = itemView.findViewById(R.id.btnRechazar);
//            btnAceptar = itemView.findViewById(R.id.btnPagos);
//        }
//    }
//}
