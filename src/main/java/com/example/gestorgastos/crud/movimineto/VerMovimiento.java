package com.example.gestorgastos.crud.movimineto;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import modelo.Movimiento;

public class VerMovimiento {
    private Context context;

    public VerMovimiento(Context context) {
        this.context = context;
    }

    public void obtenerMovimientos(int idUsuario, int idCuenta, String tipoMovimiento, final OnMovimientosResultListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Movimientos/Veo.php";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray movimientosArray = new JSONArray(response);
                            List<Movimiento> movimientos = new ArrayList<>();
                            for (int i = 0; i < movimientosArray.length(); i++) {
                                JSONObject movimientoJson = movimientosArray.getJSONObject(i);
                                int idMovimiento = movimientoJson.getInt("id_movimiento");
                                int idCuenta = movimientoJson.getInt("id_cuenta");
                                int idUsuario = movimientoJson.getInt("id_usuario");
                                String tipoMovimiento = movimientoJson.getString("tipo_movimiento");
                                int idCategoria = movimientoJson.getInt("id_categoria");
                                double cantidad = movimientoJson.getDouble("cantidad");
                                String fechaString = movimientoJson.getString("fecha");
                                Date fecha= inputFormat.parse(fechaString);

                                String descripcion = movimientoJson.getString("descripcion");
                                String iconoBase64 = movimientoJson.getString("icono"); // Suponiendo que "icono" es un campo en Base64
                                String color = movimientoJson.getString("color");
                                String nombre = movimientoJson.getString("nombre");



                                byte[] icono = android.util.Base64.decode(iconoBase64, android.util.Base64.DEFAULT);

                                Movimiento movimiento = new Movimiento(idMovimiento, idCuenta, idUsuario, tipoMovimiento,
                                        idCategoria,nombre , fecha, descripcion, icono, cantidad, color);
                                movimientos.add(movimiento);

                               }
                            listener.onMovimientosResult(movimientos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onMovimientosError("Error al procesar la respuesta del servidor");
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onMovimientosError("Error de conexión");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("id_cuenta", String.valueOf(idCuenta));
                params.put("tipo_movimiento", tipoMovimiento);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    public interface OnMovimientosResultListener {
        void onMovimientosResult(List<Movimiento> movimientos);

        void onMovimientosError(String message);
    }
}
