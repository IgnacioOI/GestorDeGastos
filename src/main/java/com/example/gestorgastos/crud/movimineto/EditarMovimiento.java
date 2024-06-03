package com.example.gestorgastos.crud.movimineto;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditarMovimiento {
    private Context context;
    private RequestQueue queue;

    // Constructor
    public EditarMovimiento(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }
    public void editarMovimiento(String idMovimiento, String tipoMovimiento, int idCategoria, double cantidad, String fecha, String descripcion, final OnMovimientoEditedListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Movimientos/EditarMovimiento.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("error")) {
                            listener.onMovimientoEditError(response);
                        } else {
                            listener.onMovimientoEdited();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                listener.onMovimientoEditError("Error de conexión: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_movimiento", idMovimiento);
                params.put("nuevo_tipo_movimiento", tipoMovimiento);
                params.put("nuevo_id_categoria", String.valueOf(idCategoria));
                params.put("nueva_cantidad", String.valueOf(cantidad));
                params.put("nueva_fecha", fecha);
                params.put("nueva_descripcion", descripcion);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public interface OnMovimientoEditedListener {
        void onMovimientoEdited();
        void onMovimientoEditError(String message);
    }
}
