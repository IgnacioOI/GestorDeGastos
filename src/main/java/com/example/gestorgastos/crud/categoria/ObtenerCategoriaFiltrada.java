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
        import java.util.Base64;
        import java.util.List;

        import modelo.Categoria;

public class ObtenerCategoriaFiltrada {
    private Context context;

    public ObtenerCategoriaFiltrada(Context context) {
        this.context = context;
    }

    public void obtenerCategorias(String tipoMovimiento, final OnCategoriasResultListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Categorias/VerCategoriaFiltradas.php?tipo_movimiento=" + tipoMovimiento;

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

                                String imagenBase64 = categoriaJson.getString("icono");
                                byte[] imagen = Base64.getDecoder().decode(imagenBase64);

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

