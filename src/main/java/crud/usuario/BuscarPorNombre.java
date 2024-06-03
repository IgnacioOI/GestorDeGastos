package crud.usuario;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BuscarPorNombre {
    private Context context;
    private RequestQueue queue;

    public BuscarPorNombre(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context); // Inicializa la cola de solicitudes
    }

    public void buscarUsuarioPorNombre(String nombre, final OnUsuarioResultListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Usuario/BuscarNombreUsuario.php?nombre=" + nombre;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("nombre")) {
                                String nombre = response.getString("nombre");
                                listener.onUsuarioResultBusqueda(nombre);
                            } else {
                                listener.onUsuarioNotFoundBusqueda("No se encontró ningún usuario con ese nombre.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUsuarioErrorBusqeuda("Error al procesar la respuesta del servidor");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onUsuarioErrorBusqeuda("Error de conexión");
            }
        });

        queue.add(jsonObjectRequest);
    }

    public interface OnUsuarioResultListener {
        void onUsuarioResultBusqueda(String usuario);

        void onUsuarioNotFoundBusqueda(String message);

        void onUsuarioErrorBusqeuda(String message);
    }
}
