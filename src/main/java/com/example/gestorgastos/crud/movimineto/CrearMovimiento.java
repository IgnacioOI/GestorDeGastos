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

public class CrearMovimiento {
    private Context context;
    private RequestQueue queue;

    public CrearMovimiento(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void crearMovimiento(String tipoMovimiento, int idCuenta, int idUsuario, int idCategoria, double cantidad, String fecha, String descripcion, final OnMovimientoCreatedListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Movimientos/CrearMovimiento.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("error")) {
                            listener.onMovimientoCreateError(response);
                        } else {
                            listener.onMovimientoCreated();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onMovimientoCreateError("Error de conexión: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tipo_movimiento", tipoMovimiento);
                params.put("id_cuenta", String.valueOf(idCuenta));
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("id_categoria", String.valueOf(idCategoria));
                params.put("cantidad", String.valueOf(cantidad));
                params.put("fecha", fecha);
                params.put("descripcion", descripcion);
                return params;
            }
        };


        queue.add(stringRequest);
    }

    public interface OnMovimientoCreatedListener {
        void onMovimientoCreated();
        void onMovimientoCreateError(String message);
    }
}
