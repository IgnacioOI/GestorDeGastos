package com.example.gestorgastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestorgastos.MainActivityCrearUusario;
import com.example.gestorgastos.R;

import crud.usuario.BuscarUsuario;
import modelo.Usuario;

public class MainActivity extends AppCompatActivity implements BuscarUsuario.OnUsuarioResultListener {
    private EditText nombre, contrasena;
    private TextView signup;
    private Button login;
    private Usuario miUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarDatos();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = nombre.getText().toString();
                String contraseñaUsuario = contrasena.getText().toString();

                BuscarUsuario buscarUsuario = new BuscarUsuario(MainActivity.this);
                buscarUsuario.buscarUsuario(nombreUsuario, contraseñaUsuario, MainActivity.this);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivityCrearUusario.class);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onUsuarioResult(Usuario usuario) {
        //Toast.makeText(this, "Usuario encontrado: " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
        miUsuario = usuario;
        comprobarUsuario();
    }

    @Override
    public void onUsuarioNotFound(String message) {
        Toast.makeText(this, "Usuario no encontrado: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUsuarioError(String message) {
        Toast.makeText(this, "Error al buscar usuario: " + message, Toast.LENGTH_SHORT).show();
    }

    private void iniciarDatos(){
        nombre = findViewById(R.id.editMainUsername1);
        contrasena = findViewById(R.id.editMainPassword1);
        signup = findViewById(R.id.signupText1);
        login = findViewById(R.id.buttonMainLogin1);
    }

    private void comprobarUsuario(){
        String nombreComprobacion = nombre.getText().toString();
        String contrasenaComprobacion = contrasena.getText().toString();
        Intent intent = new Intent(MainActivity.this, MainActivityCuentas.class);

        if (miUsuario != null && miUsuario.getNombre().equalsIgnoreCase(nombreComprobacion) && miUsuario.getContrasena().equalsIgnoreCase(contrasenaComprobacion)) {
            intent.putExtra("nombreUsuario", nombreComprobacion);
            intent.putExtra("idUsuario",miUsuario.getId());
            //Toast.makeText(this, "Hola "+miUsuario.getId(), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else {
            Toast.makeText(this, "Este usuario no existe " + nombreComprobacion + " " + contrasenaComprobacion, Toast.LENGTH_SHORT).show();
        }
    }
}
