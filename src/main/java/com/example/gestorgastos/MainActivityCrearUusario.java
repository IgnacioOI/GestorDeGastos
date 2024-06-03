package com.example.gestorgastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import crud.usuario.BuscarPorNombre;
import crud.usuario.CrearUsuario;

public class MainActivityCrearUusario extends AppCompatActivity implements CrearUsuario.OnUsuarioCreadoListener, BuscarPorNombre.OnUsuarioResultListener {
    private EditText nombreUsuario, contrasenaUsuario;
    private TextView errorNombre,errorContrasena;
    private Button buttonCrear;
    private String nombrRepetido="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_crear_uusario);
        iniciarDatos();
    }

    private void iniciarDatos() {
        nombreUsuario = findViewById(R.id.editMainUsername1);
        contrasenaUsuario = findViewById(R.id.editMainPassword1);
        buttonCrear = findViewById(R.id.buttonMainLogin1);
        errorNombre=findViewById(R.id.textViewCrearNombre);
        errorContrasena=findViewById(R.id.textViewCrearContrasena);

        nombreUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarNombreUsuario();
                }
            }
        });

        contrasenaUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarContrasena();
                }
            }
        });

        buttonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarNombreUsuario();
                validarContrasena();

                if (errorNombre.getText().toString().isEmpty() && errorContrasena.getText().toString().isEmpty()) {
                    String nombreCreado = nombreUsuario.getText().toString();
                    String contrasenaCreado = contrasenaUsuario.getText().toString();

                    if (!comprobacionBusqueda()) {
                        crearUsuario(nombreCreado, contrasenaCreado);
                        Intent intent=new Intent(MainActivityCrearUusario.this,MainActivity.class);
                        startActivity(intent);

                    }
                }
            }
        });
    }

    private void validarNombreUsuario() {
        String nombreCreado = nombreUsuario.getText().toString();
        if (nombreCreado.isEmpty()) {
            errorNombre.setText("El nombre de usuario es requerido");
        } else {
            errorNombre.setText("");
        }
    }

    private void validarContrasena() {
        String contrasenaCreada = contrasenaUsuario.getText().toString();
        if (contrasenaCreada.isEmpty()) {
            errorContrasena.setText("La contraseña es requerida");
        } else {
            errorContrasena.setText("");
        }
    }

    private void crearUsuario(String nombre, String contrasena) {
        CrearUsuario crearUsuario = new CrearUsuario(MainActivityCrearUusario.this);
        crearUsuario.crearUsuario(nombre, contrasena, MainActivityCrearUusario.this);
    }

    private boolean comprobacionBusqueda() {
        String nombreCreado = nombreUsuario.getText().toString();

        BuscarPorNombre buscarPorNombre=new BuscarPorNombre(MainActivityCrearUusario.this);
        buscarPorNombre.buscarUsuarioPorNombre(nombreCreado,MainActivityCrearUusario.this);
        if(nombrRepetido.equalsIgnoreCase("")){
            return false;
        }
        return true;
    }

    @Override
    public void onUsuarioCreado(String message) {
        Toast.makeText(MainActivityCrearUusario.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(MainActivityCrearUusario.this, "Usuario: " + message, Toast.LENGTH_SHORT).show();
        Log.i("error",message);
    }


    @Override
    public void onUsuarioResultBusqueda(String usuario) {
        nombrRepetido=usuario;
        Toast.makeText(this, "Resultado busqueda"+usuario, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUsuarioNotFoundBusqueda(String message) {
        //Toast.makeText(this, "No funciona la busqueda"+message, Toast.LENGTH_SHORT).show();
        Log.i("error no funciona busqueda",message);
    }

    @Override
    public void onUsuarioErrorBusqeuda(String message) {
        //Toast.makeText(this, "Error busqueda"+message, Toast.LENGTH_SHORT).show();
        Log.i("errorBusqueda",message);
    }
}

