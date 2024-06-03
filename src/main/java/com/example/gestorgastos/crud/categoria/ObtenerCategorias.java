package com.example.gestorgastos.crud.categoria;

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
import java.util.Base64; // Importa la clase Base64

import modelo.Categoria;

public class ObtenerCategorias {
    private Context context;

    public ObtenerCategorias(Context context) {
        this.context = context;
    }

    public void obtenerCategorias(final OnCategoriasResultListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Categorias/VerCategorias.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Categoria> categorias = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject categoriaJson = response.getJSONObject(i);
                                int id = categoriaJson.getInt("id_categoria");
                                String nombre = categoriaJson.getString("nombre");

                                String imagenBase64 = categoriaJson.getString("icono"); // Obtiene la imagen Base64
                                byte[] imagen = Base64.getDecoder().decode(imagenBase64); // Decodifica la imagen Base64

                                String color = categoriaJson.getString("color");
                                Categoria categoria = new Categoria(id, nombre, imagen, color);
                                categorias.add(categoria);
                                Log.i("datos categorias","datos"+categoria.getNombre());
                            }
                            listener.onCategoriasResult(categorias);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onCategoriasError("Error al procesar la respuesta del servidor");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onCategoriasError("Error de conexión");
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonArrayRequest);
    }

    public interface OnCategoriasResultListener {
        void onCategoriasResult(List<Categoria> categorias);

        void onCategoriasError(String message);
    }
}
