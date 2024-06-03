package com.example.gestorgastos.crud.cuenta;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CrearCuenta {
    private Context context;
    private RequestQueue queue;

    public CrearCuenta(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void crearCuenta(String nombre, double saldo, int idSimbolo, int idUsuario, final OnCuentaCreatedListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Cuenta/CrearCuenta.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (!jsonResponse.has("error")) {
                                listener.onCuentaCreated();
                            } else {
                                String errorMessage = jsonResponse.getString("error");
                                listener.onCuentaCreateError(errorMessage);
                            }
                        } catch (JSONException e) {
                            listener.onCuentaCreateError("Error de análisis JSON: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onCuentaCreateError("Error de conexión: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombreCuenta", nombre);
                params.put("saldoInicial", String.valueOf(saldo));
                params.put("divisaId", String.valueOf(idSimbolo));
                params.put("idUsuario", String.valueOf(idUsuario));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public interface OnCuentaCreatedListener {
        void onCuentaCreated();
        void onCuentaCreateError(String message);
    }
}
