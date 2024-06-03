package com.example.gestorgastos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gestorgastos.adapter.RecyclerAdapterCuentas;
import com.example.gestorgastos.crud.cuenta.BorrarCuenta;
import com.example.gestorgastos.crud.cuenta.VerCuentas;

import java.util.ArrayList;
import java.util.List;

import modelo.Cuenta;

public class MainActivityCuentas extends AppCompatActivity implements VerCuentas.OnCuentasResultListener, BorrarCuenta.OnCuentaDeletedListener, RecyclerAdapterCuentas.ItemClickListener {
    private Button buttonCrearCuenta;
    private RecyclerView recyclerViewCuentas;
    private List<Cuenta> listaCuentas = new ArrayList<>();
    private int idUsuario;
    private String nombreUsuario;
    private RecyclerAdapterCuentas recyclerAdapterCuentas;
    private static final int REQUEST_CODE_CREAR_CUENTA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cuentas);
        iniciarDatos();

        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("idUsuario", 0);
        nombreUsuario = intent.getStringExtra("nombreUsuario");

        iniciarRecycler();

        buttonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityCuentas.this, MainActivityCrearCuenta.class);
                intent.putExtra("idUsuario", idUsuario);
                startActivityForResult(intent, REQUEST_CODE_CREAR_CUENTA);
            }
        });
    }

    private void iniciarDatos() {
        buttonCrearCuenta = findViewById(R.id.buttonCrearCuenta);
        recyclerViewCuentas = findViewById(R.id.recyclerViewCuentas);
    }

    private void iniciarRecycler() {
        VerCuentas verCuentas = new VerCuentas(MainActivityCuentas.this);
        verCuentas.obtenerCuentasUsuario(idUsuario, MainActivityCuentas.this);
    }

    @Override
    public void onCuentasResult(ArrayList<Cuenta> cuentas) {
        listaCuentas = cuentas;
        recyclerAdapterCuentas = new RecyclerAdapterCuentas(MainActivityCuentas.this, listaCuentas);
        recyclerAdapterCuentas.setClickListener(this);
        recyclerViewCuentas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCuentas.setAdapter(recyclerAdapterCuentas);
    }

    @Override
    public void onCuentasNotFound(String message) {
        Toast.makeText(this, "Error en el funcionamiento de la cuenta" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCuentasError(String message) {
        Toast.makeText(this, "Error en la cuenta" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREAR_CUENTA && resultCode == RESULT_OK) {
            iniciarRecycler();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Cuenta cuenta = recyclerAdapterCuentas.getItem(position);
        //Toast.makeText(this, "Nombre de la cuenta: " + cuenta.getNombre(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivityCuentas.this, Main2ActivityInicio.class);
        intent.putExtra("idCuenta", cuenta.getIdCuenta());
        intent.putExtra("idUsuario", cuenta.getIdUsuario());
        intent.putExtra("usuarioNombre", nombreUsuario);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 103:
                int position = item.getGroupId();
                Cuenta cuenta = recyclerAdapterCuentas.getItem(position);
                BorrarCuenta borrarCuenta = new BorrarCuenta(this);
                borrarCuenta.borrarCuenta(cuenta.getIdCuenta(), idUsuario, MainActivityCuentas.this);
                recyclerAdapterCuentas.removeData(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCuentaDeleted() {
        Toast.makeText(this, "Cuenta borrada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCuentaDeleteError(String message) {
        Toast.makeText(this, "Error al borrar cuenta: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciarRecycler();
    }
}
