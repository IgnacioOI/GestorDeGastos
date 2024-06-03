package com.example.gestorgastos.crud.movimineto;

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

public class BorrarMovimiento {

    private Context context;

    public BorrarMovimiento(Context context) {
        this.context = context;
    }

    public void borrarMovimiento(int idMovimiento, final BorrarMovimientoCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Movimientos/BorrarMovimiento.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            callback.onSuccessBorrarDatos(message);
                        } catch (JSONException e) {
                            callback.onErrorBorrarDatos(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorBorrarDatos(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_movimiento", String.valueOf(idMovimiento));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public interface BorrarMovimientoCallback {
        void onSuccessBorrarDatos(String message);
        void onErrorBorrarDatos(String error);
    }
}
