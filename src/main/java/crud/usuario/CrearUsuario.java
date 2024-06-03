package crud.usuario;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CrearUsuario {
    private Context context;
    private RequestQueue queue;

    public CrearUsuario(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void crearUsuario(String nombre, String contrasena, final OnUsuarioCreadoListener listener) {
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Usuario/CrearUsuario.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Usuario creado exitosamente.")) {
                            listener.onUsuarioCreado(response);
                        } else {
                            listener.onError(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError("Error de conexión: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("contrasena", contrasena);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public interface OnUsuarioCreadoListener {
        void onUsuarioCreado(String message);
        void onError(String message);
    }
}
