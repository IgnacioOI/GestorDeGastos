package com.example.gestorgastos.crud.divisa;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import modelo.Divisa;

public class VerDivisas {
    private Context context;

    public VerDivisas(Context context) {
        this.context = context;
    }

    public void obtenerDivisas(final OnDivisasResultListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Divisas/VerDivisas.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Divisa> divisas = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject divisaJson = response.getJSONObject(i);
                                int id = divisaJson.getInt("id");
                                String nombre = divisaJson.getString("nombre");
                                String simbolo = divisaJson.getString("simbolo");
                                Divisa divisa = new Divisa(id, nombre, simbolo);
                                divisas.add(divisa);
                            }
                            listener.onDivisasResult(divisas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onDivisasError("Error al procesar la respuesta del servidor");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onDivisasError("Error de conexión");
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }

    public interface OnDivisasResultListener {
        void onDivisasResult(List<Divisa> divisas);

        void onDivisasError(String message);
    }
}
