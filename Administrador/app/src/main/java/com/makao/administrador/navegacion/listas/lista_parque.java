package com.makao.administrador.navegacion.listas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Entities.Pagos;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.adapters.listaCuartos_tipo1;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_parque extends Fragment {
    RecyclerView rclParque;
    listaCuartos_tipo1 adapter;
    ArrayList<Pagos> listaCuartos = new ArrayList<>();

    private ProgresDialogPersonalizado progressDialog;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    public void onResume() {
        super.onResume();
        listaCuartos.clear();
        consulta("PID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_lista_parque, container, false);

        // Inicializar RecyclerView
        rclParque = vista.findViewById(R.id.rclParque);

//        listaCuartos.clear();
//
//        consulta("PID");

        return vista;
    }
    public void consulta(String codigo){
        String url = Utils.RUTA_APIS + "ConsultarDatosPago.php?codigo_cuarto="+codigo;
        url = url.replace(" ", "%20");
        request = Volley.newRequestQueue(getActivity());
        showProgressDialog();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Pagos pagos;
                try {
                    JSONArray jsonArray = response.getJSONArray("pagos");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        pagos = new Pagos();

                        pagos.setIdPago(jsonObject.getInt("ID_PAGO"));
                        pagos.setIdCuarto(jsonObject.getInt("ID_CUOTA"));
                        pagos.setIdInquilino(jsonObject.getInt("ID_INQUILINO"));

                        pagos.setFechaPago(jsonObject.getString("FECHA_PAG"));
                        pagos.setMontoPago(jsonObject.getInt("MONTO_PAG"));
                        pagos.setEstadoPago(jsonObject.getString("ESTADO_PAG"));
                        pagos.setCodigoCua(jsonObject.getString("CODIGO_CUA"));
                        pagos.setPisoCua(jsonObject.getString("PISO_CUA"));
                        pagos.setCostoCua(jsonObject.getInt("COSTO_CUA"));

                        pagos.setFecha_Vencimiento(jsonObject.getString("FECHA_VENCIMIENTO"));
                        pagos.setEstado_Cuo(jsonObject.getString("ESTADO_CUO"));
                        pagos.setMonto_Cuo(jsonObject.getString("MONTO_CUO"));

                        pagos.setNombreInqui(jsonObject.getString("NOMBRE_INQ"));

                        listaCuartos.add(pagos);
                    }

                    // Configurar el adaptador después de agregar los datos a la lista
                    adapter = new listaCuartos_tipo1(listaCuartos, getActivity());
                    rclParque.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rclParque.setAdapter(adapter);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });

        request.add(jsonObjectRequest);
    }
    private void showProgressDialog() {
        progressDialog = new ProgresDialogPersonalizado(getActivity());
        progressDialog.show();
    }
}