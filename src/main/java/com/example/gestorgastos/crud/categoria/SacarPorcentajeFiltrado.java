package com.example.gestorgastos.crud.categoria;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.Porcentaje;

public class SacarPorcentajeFiltrado {

    private Context context;

    public SacarPorcentajeFiltrado(Context context) {
        this.context = context;
    }

    public interface PorcentajeFiltradoListener {
        void onSuccessPorcentajeFiltrado(List<Porcentaje> porcentajes);
        void onErrorPorcentajeFiltrado(String message);
    }

    public void obtenerPorcentajes(String tipoMovimiento, int idCuenta, String fechaInicio, String fechaFin, PorcentajeFiltradoListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Categorias/SacarPorcentajeFiltrador.php"; // Reemplaza con tu URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            List<Porcentaje> porcentajes = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonPorcentaje = jsonArray.getJSONObject(i);
                                String nombreCategoria = jsonPorcentaje.getString("nombre");
                                String color = jsonPorcentaje.getString("color");
                                double porcentaje = jsonPorcentaje.getDouble("porcentaje_gasto");
                                Porcentaje p = new Porcentaje(nombreCategoria, color, porcentaje);
                                porcentajes.add(p);
                            }

                            listener.onSuccessPorcentajeFiltrado(porcentajes);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onErrorPorcentajeFiltrado("Error al procesar la respuesta JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onErrorPorcentajeFiltrado("Error de conexión"+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tipo_movimiento", tipoMovimiento);
                params.put("id_cuenta", String.valueOf(idCuenta));
                if (fechaInicio != null && !fechaInicio.isEmpty()) {
                    params.put("fecha_inicio", fechaInicio);
                }
                if (fechaFin != null && !fechaFin.isEmpty()) {
                    params.put("fecha_fin", fechaFin);
                }
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
}
