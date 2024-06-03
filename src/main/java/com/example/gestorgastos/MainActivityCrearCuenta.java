package com.example.gestorgastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gestorgastos.adapter.SpinnerAdapterDivisas;
import com.example.gestorgastos.crud.cuenta.CrearCuenta;
import com.example.gestorgastos.crud.divisa.VerDivisas;

import java.util.List;

import modelo.Divisa;

public class MainActivityCrearCuenta extends AppCompatActivity implements VerDivisas.OnDivisasResultListener, CrearCuenta.OnCuentaCreatedListener {
    private EditText editNombre, editSaldo;
    private Spinner spinnerDivisas;
    private Button botonCrear;
    private VerDivisas verDivisas;
    private SpinnerAdapterDivisas adapter;
    private int idDivisa;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_crear_cuenta);
        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("idUsuario", 0);
        iniciarDatos();
        cargarSpinner();
        Toast.makeText(MainActivityCrearCuenta.this, "Usuario recibido " + idUsuario, Toast.LENGTH_SHORT).show();

        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aniadirDatosCuenta();
            }
        });

        spinnerDivisas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Divisa divisa = adapter.getItem(position);
                idDivisa = divisa.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void iniciarDatos() {
        editNombre = findViewById(R.id.editTextNombreCuenta);
        editSaldo = findViewById(R.id.editTextNumberSaldoCuenta);
        botonCrear = findViewById(R.id.buttonGuardarCuenta);
        spinnerDivisas = findViewById(R.id.spinnerDivisasCuenta);
    }

    private void cargarSpinner() {
        verDivisas = new VerDivisas(this);
        verDivisas.obtenerDivisas(this);
    }

    private void aniadirDatosCuenta() {
        String nombre = editNombre.getText().toString();
        String saldoString = editSaldo.getText().toString();

        if (nombre.isEmpty() || saldoString.isEmpty()) {
            Toast.makeText(MainActivityCrearCuenta.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double saldo = Float.parseFloat(saldoString);

        CrearCuenta crearCuenta = new CrearCuenta(MainActivityCrearCuenta.this);
        crearCuenta.crearCuenta(nombre, saldo, idDivisa, idUsuario,MainActivityCrearCuenta.this);
    }

    @Override
    public void onDivisasResult(List<Divisa> divisas) {
        adapter = new SpinnerAdapterDivisas(this, divisas);
        spinnerDivisas.setAdapter(adapter);
    }

    @Override
    public void onDivisasError(String message) {
        Toast.makeText(MainActivityCrearCuenta.this, "Error al cargar divisas: " + message, Toast.LENGTH_SHORT).show();
        Log.e("Error en encontrar divisas ", message);
    }

    @Override
    public void onCuentaCreated() {
        Toast.makeText(MainActivityCrearCuenta.this, "Cuenta creada ", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onCuentaCreateError(String message) {
        Toast.makeText(MainActivityCrearCuenta.this, "Error en crear cuenta: " + message, Toast.LENGTH_SHORT).show();
        Log.e("Error en crear cuenta ", message);


    }

}
