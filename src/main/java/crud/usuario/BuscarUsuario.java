package crud.usuario;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import modelo.Usuario;

public class BuscarUsuario {
    private Context context;
    private RequestQueue queue;

    public BuscarUsuario(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void buscarUsuario(String nombre, String contrasena, final OnUsuarioResultListener listener) {
        // URL del servidor donde se encuentra el script PHP para buscar el usuario
        String url = "https://migransitio000.000webhostapp.com/Gestor_Gastos/Usuario/BuscarUsuario.php?nombre="+nombre +"&contra="+contrasena;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Verificar si la respuesta contiene datos de usuario
                            if (response.length() > 0) {
                                JSONObject usuario = response.getJSONObject(0);
                                String nombre = usuario.getString("nombre");
                                String contrasena = usuario.getString("contrasena");
                                int idUsuario=usuario.getInt("id_usuario");
                                Usuario usuarioEncontrado = new Usuario( nombre, contrasena,idUsuario);
                                listener.onUsuarioResult(usuarioEncontrado);
                            } else {
                                listener.onUsuarioNotFound("No se encontró ningún usuario con ese nombre y contraseña.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onUsuarioError("Error al procesar la respuesta del servidor");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onUsuarioError("Error de conexión");
            }
        });

        queue.add(jsonArrayRequest);
    }

    // Interfaz para manejar los resultados de la búsqueda de usuario
    public interface OnUsuarioResultListener {
        void onUsuarioResult(Usuario usuario);

        void onUsuarioNotFound(String message);

        void onUsuarioError(String message);
    }
}
