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
import modelo.Cuenta;

public class VerCuentas {
    private Context context;
    private RequestQueue queue;

    public VerCuentas(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void obtenerCuentasUsuario(int idUsuario, final OnCuentasResultListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Cuenta/VerCuentas2.php?idUsuario=" + idUsuario;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                ArrayList<Cuenta> cuentas = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject cuentaJson = response.getJSONObject(i);
                                    int idCuenta = cuentaJson.getInt("id_cuenta");
                                    int idUsuario = cuentaJson.getInt("id_usuario");
                                    String nombre = cuentaJson.getString("nombre");
                                    float saldo = (float) cuentaJson.getDouble("saldo_total");
                                    String simbolo = cuentaJson.getString("simbolo");
                                    Cuenta cuenta = new Cuenta(idCuenta, idUsuario, nombre, saldo, simbolo.charAt(0));
                                    cuentas.add(cuenta);
                                    Log.i("Cuentas", "Cuenta: " + cuenta.getSimbolo()+" "+cuenta.getNombre());
                                }
                                listener.onCuentasResult(cuentas);
                                Log.i("Cuentas", "Cuentas obtenidas correctamente");
                            } else {
                                listener.onCuentasNotFound("No se encontraron cuentas para el usuario con ID: " + idUsuario);
                                Log.i("Cuentas", "No se encontraron cuentas para el usuario con ID: " + idUsuario);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCuentasError("Error al procesar la respuesta del servidor: " + e.getMessage());
                            Log.e("Cuentas", "Error al procesar la respuesta del servidor: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onCuentasError("Error de conexión: " + error.getMessage());
                Log.e("Cuentas", "Error de conexión: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public interface OnCuentasResultListener {
        void onCuentasResult(ArrayList<Cuenta> cuentas);

        void onCuentasNotFound(String message);

        void onCuentasError(String message);
    }
}
