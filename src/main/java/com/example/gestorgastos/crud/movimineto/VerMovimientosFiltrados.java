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

public class VerMovimientosFiltrados {
    private Context context;

    public VerMovimientosFiltrados(Context context) {
        this.context = context;
    }

    public void obtenerMovimientos(int idUsuario, int idCuenta, String tipoMovimiento, String fechaInicio, String fechaFin, final OnMovimientosResultListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Movimientos/VerMovimientosFiltrados.php";
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
                                Date fecha = inputFormat.parse(fechaString);

                                String descripcion = movimientoJson.getString("descripcion");
                                String iconoBase64 = "";
                                if (movimientoJson.has("icono")) {
                                    iconoBase64 = movimientoJson.getString("icono");
                                }
                                String color = movimientoJson.getString("color");
                                String nombreCategoria = movimientoJson.getString("nombre");
                                String simbolo = movimientoJson.getString("divisa");

                                byte[] icono = android.util.Base64.decode(iconoBase64, android.util.Base64.DEFAULT);

                                Movimiento movimiento = new Movimiento(idMovimiento, idCuenta, idUsuario, tipoMovimiento,
                                        idCategoria, nombreCategoria, fecha, descripcion, icono, cantidad, color, simbolo);
                                movimientos.add(movimiento);
                            }
                            listener.onMovimientosFiltradosResult(movimientos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onMovimientosFiltradosError("Error al procesar la respuesta del servidor"+e.getMessage());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onMovimientosFiltradosError("Error de conexión");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("id_cuenta", String.valueOf(idCuenta));
                params.put("tipo_movimiento", tipoMovimiento);
                if (fechaInicio != null) {
                    params.put("fecha_inicio", fechaInicio);
                }
                if (fechaFin != null) {
                    params.put("fecha_fin", fechaFin);
                }
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    public interface OnMovimientosResultListener {
        void onMovimientosFiltradosResult(List<Movimiento> movimientos);

        void onMovimientosFiltradosError(String message);
    }
}
