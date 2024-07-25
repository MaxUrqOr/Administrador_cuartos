package com.makao.administrador.navegacion.listas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.makao.administrador.Entities.Cuartos;
import com.makao.administrador.R;
import com.makao.administrador.Utils.Utils;
import com.makao.administrador.adapters.listaCuartos_tipo2;
import com.makao.administrador.mensages.ProgresDialogPersonalizado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class lista_parque_t2 extends Fragment {

    GridView grdListaParque;
    SearchView searchView;
    listaCuartos_tipo2 adapter;
    ArrayList<Cuartos> listaCuartos = new ArrayList<>();
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
        View vista = inflater.inflate(R.layout.fragment_lista_parque_t2, container, false);

        // Inicializar RecyclerView
        grdListaParque = vista.findViewById(R.id.grdListaParque);
        searchView = vista.findViewById(R.id.srcFiltrar);
//        listaCuartos.clear();
//
//        consulta("PID");

        // Configurar el SearchView para filtrar el adaptador del GridView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return vista;
    }

    public void consulta(String codigo) {
        String url = Utils.RUTA_APIS + "ConsultarCuartos.php?codigo_cuarto=" + codigo;
        url = url.replace(" ", "%20");
        request = Volley.newRequestQueue(getActivity());
        showProgressDialog();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Cuartos cuartos;
                try {
                    JSONArray jsonArray = response.getJSONArray("cuartos");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        cuartos = new Cuartos();

                        // Verificar y asignar ID_CUARTO
                        if (!jsonObject.isNull("ID_CUARTO")) {
                            cuartos.setIdCuarto(jsonObject.getInt("ID_CUARTO"));
                        } else {
                            cuartos.setIdCuarto(0); // Valor predeterminado
                        }

                        // Verificar y asignar ID_INQUILINO
                        if (!jsonObject.isNull("ID_INQUILINO")) {
                            cuartos.setIdInquilino(jsonObject.getInt("ID_INQUILINO"));
                        } else {
                            cuartos.setIdInquilino(0); // Valor predeterminado
                        }

                        // Verificar y asignar CODIGO_CUA
                        cuartos.setCodigoCua(jsonObject.optString("CODIGO_CUA", " ")); // Valor predeterminado como cadena vacía si es nulo

                        // Verificar y asignar TAMANO_CUA
                        cuartos.setTamanoCua(jsonObject.optString("TAMANO_CUA", " ")); // Valor predeterminado como cadena vacía si es nulo

                        // Verificar y asignar PISO_CUA
                        cuartos.setPisoCua(jsonObject.optString("PISO_CUA", " ")); // Valor predeterminado como cadena vacía si es nulo

                        // Verificar y asignar COSTO_CUA
                        cuartos.setCostoCua(jsonObject.optInt("COSTO_CUA", 0)); // Valor predeterminado como 0 si es nulo

                        // Verificar y asignar GARANTIA_CUA
                        cuartos.setGarantia_Cua(jsonObject.optInt("GARANTIA_CUA", 0)); // Valor predeterminado como 0 si es nulo

                        // Verificar y asignar ESTADO_CUA
                        cuartos.setEstado_Cua(jsonObject.optString("ESTADO_CUA", " ")); // Valor predeterminado como cadena vacía si es nulo

                        // Verificar y asignar DNI_INQ
                        cuartos.setDniInq(jsonObject.optString("DNI_INQ", " ")); // Valor predeterminado como cadena vacía si es nulo

                        // Verificar y asignar TELEFONO_INQ
                        cuartos.setNombreInq(jsonObject.optString("NOMBRE_INQ", "")); // Valor predeterminado como cadena vacía si es nulo

                        // Verificar y asignar TELEFONO_INQ
                        cuartos.setTelefonoInq(jsonObject.optString("TELEFONO_INQ", "")); // Valor predeterminado como cadena vacía si es nulo

                        // Verificar y asignar FECHAINGRESO_INQ
                        cuartos.setFechaIngresoInq(jsonObject.optString("FECHAINGRESO_INQ", "0000-00-00")); // Valor predeterminado como cadena vacía si es nulo

                        listaCuartos.add(cuartos);
                    }

                    // Configurar el adaptador después de agregar los datos a la lista
                    adapter = new listaCuartos_tipo2(listaCuartos, getContext());
                    grdListaParque.setAdapter(adapter);
                } catch (JSONException e) {
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