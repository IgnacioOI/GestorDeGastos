package com.example.gestorgastos.crud.cuenta;

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

import modelo.CuentaResumen;

public class VerCuentasResumen {
    private Context context;
    private RequestQueue queue;

    public VerCuentasResumen(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void obtenerCuentasResumen(int idUsuario, final OnCuentasResumenListener listener) {
        String url = "http://tu_servidor/tu_script.php?id_usuario=" + idUsuario;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                ArrayList<CuentaResumen> cuentasResumen = new ArrayList<>();

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject cuentaJson = response.getJSONObject(i);
                                    int idCuenta = cuentaJson.getInt("id_cuenta");
                                    String nombre = cuentaJson.getString("nombre");
                                    double saldoTotal = cuentaJson.getDouble("saldo_total");


                                    CuentaResumen cuentaResumen = new CuentaResumen(idCuenta, nombre, saldoTotal);
                                    cuentasResumen.add(cuentaResumen);
                                    Log.i("CuentasResumen", "Cuenta: " + cuentaResumen.getNombre() + " Saldo: " + cuentaResumen.getSaldoTotal());
                                }
                                listener.onCuentasResumenResult(cuentasResumen);
                                Log.i("CuentasResumen", "Cuentas obtenidas correctamente");
                            } else {
                                listener.onCuentasResumenNotFound("No se encontraron cuentas para el usuario con ID: " + idUsuario);
                                Log.i("CuentasResumen", "No se encontraron cuentas para el usuario con ID: " + idUsuario);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCuentasResumenError("Error al procesar la respuesta del servidor: " + e.getMessage());
                            Log.e("CuentasResumen", "Error al procesar la respuesta del servidor: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onCuentasResumenError("Error de conexión: " + error.getMessage());
                Log.e("CuentasResumen", "Error de conexión: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public interface OnCuentasResumenListener {
        void onCuentasResumenResult(ArrayList<CuentaResumen> cuentasResumen);

        void onCuentasResumenNotFound(String message);

        void onCuentasResumenError(String message);
    }
}
