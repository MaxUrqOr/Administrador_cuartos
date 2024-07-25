package com.makao.administrador.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.makao.administrador.Entities.Cuartos;
import com.makao.administrador.R;
import com.makao.administrador.activities.Detalle_Departamento;

import java.util.ArrayList;
import java.util.List;

public class listaCuartos_tipo2 extends BaseAdapter implements Filterable {
    private Context context;
    private List<Cuartos> data;
    private List<Cuartos> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public listaCuartos_tipo2(List<Cuartos> data, Context context) {
        this.data = data;
        this.filteredData = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listacuartos_tipo2, viewGroup, false);
        }

        Cuartos item = filteredData.get(i);

        //referencia a los datos del adaptador
        TextView txtDepartamento = view.findViewById(R.id.txtDepartamento);
        TextView txtDepatamentoEstado = view.findViewById(R.id.txtEstadoDepartamento);
        LinearLayout cardView = view.findViewById(R.id.crdContenedor);
        ImageView imgDepartamentoPlantilla = view.findViewById(R.id.imgPagoLicoreria);
        ConstraintLayout constraintLayoutCambiarColor = view.findViewById(R.id.constraintCambiarColor);

        txtDepartamento.setText(item.getCodigoCua() + " " + item.getPisoCua());
        txtDepatamentoEstado.setText(item.getEstado_Cua());

        switch (txtDepatamentoEstado.getText().toString()) {
            case "Libre":
                constraintLayoutCambiarColor.setBackgroundResource(R.drawable.background_oval_departamentos_libre);
                txtDepartamento.setTextColor(ContextCompat.getColor(context, R.color.morado_oscuro));
                txtDepatamentoEstado.setTextColor(ContextCompat.getColor(context, R.color.morado_oscuro));
                cardView.setBackgroundResource(R.drawable.background_lista_cuartos_2_libre);
                imgDepartamentoPlantilla.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.morado_oscuro)));
                break;
            case "Al Día":
                cardView.setBackgroundResource(R.drawable.background_al_dia_inquilinos);
                constraintLayoutCambiarColor.setBackgroundResource(R.drawable.background_oval);
                txtDepartamento.setTextColor(ContextCompat.getColor(context, R.color.white));
                txtDepatamentoEstado.setTextColor(ContextCompat.getColor(context, R.color.white));
                imgDepartamentoPlantilla.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPagado)));
                break;
            case "En Deuda":
                cardView.setBackgroundResource(R.drawable.background_en_deuda_inquilinos);
                constraintLayoutCambiarColor.setBackgroundResource(R.drawable.background_oval);
                txtDepartamento.setTextColor(ContextCompat.getColor(context, R.color.white));
                txtDepatamentoEstado.setTextColor(ContextCompat.getColor(context, R.color.white));
                imgDepartamentoPlantilla.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorDeuda)));
                break;
            case "Morosidad Inminente":
                cardView.setBackgroundResource(R.drawable.background_morosidad_inminente_inqulinos);
                constraintLayoutCambiarColor.setBackgroundResource(R.drawable.background_oval);
                txtDepartamento.setTextColor(ContextCompat.getColor(context, R.color.white));
                txtDepatamentoEstado.setTextColor(ContextCompat.getColor(context, R.color.white));
                imgDepartamentoPlantilla.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorMorosidad)));
                break;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Detalle_Departamento.class);
                i.putExtra("idI", "" + item.getIdInquilino());
                i.putExtra("idC", "" + item.getIdCuarto());
                i.putExtra("dep", item.getCodigoCua() + " " + item.getPisoCua());
                i.putExtra("estado", item.getEstado_Cua());
                i.putExtra("nombre", item.getNombreInq());
                i.putExtra("dni", item.getDniInq());
                i.putExtra("fecha", item.getFechaIngresoInq());
                i.putExtra("costo", "S/." + item.getCostoCua());
                i.putExtra("telefono", item.getTelefonoInq());
                i.putExtra("garantia", "S/." + item.getGarantia_Cua());

                context.startActivity(i);
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final List<Cuartos> list = data;
            int count = list.size();
            final List<Cuartos> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Cuartos cuartos = list.get(i);
                String departamentoText = cuartos.getCodigoCua() + " " + cuartos.getPisoCua();
                if (departamentoText.toLowerCase().contains(filterString)) {
                    nlist.add(cuartos);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<Cuartos>) results.values;
            notifyDataSetChanged();
        }
    }
}
