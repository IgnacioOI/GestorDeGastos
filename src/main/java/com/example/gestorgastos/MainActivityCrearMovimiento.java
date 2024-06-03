package com.example.gestorgastos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gestorgastos.adapter.RecyclerAdapterCategoria;
import com.example.gestorgastos.crud.categoria.ObtenerCategoriaFiltrada;
import com.example.gestorgastos.crud.movimineto.CrearMovimiento;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import modelo.Categoria;


public class MainActivityCrearMovimiento extends AppCompatActivity implements  CrearMovimiento.OnMovimientoCreatedListener, ObtenerCategoriaFiltrada.OnCategoriasResultListener, RecyclerAdapterCategoria.ItemClickListener {

    private EditText editTextCantidad, editTextFecha, editTextDescripcion;
    private RadioGroup radioGroupTipoMovimiento;
    private RadioButton radioButtonGasto, radioButtonIngreso;
    private RecyclerView recyclerViewCategorias;
    private Button buttonCrearMovimiento;
    private RecyclerAdapterCategoria categoriaAdapter;
    private int idCategoriaEscogido;
    private String tipoMovimiento;
    private List<Categoria> listaCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_crear_movimiento);
        iniciarDatos();

        setDefaultDate();

        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonCrearMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearMovimiento();
            }
        });

        // Listener para el RadioGroup
        radioGroupTipoMovimiento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonEditarMoviminetoGasto) {
                    tipoMovimiento = "Gasto";
                } else if (checkedId == R.id.radioButtonEditarMovimientoIngreso) {
                    tipoMovimiento = "Ingreso";
                }
                obtenerCategoriasFiltradas();
            }
        });

        obtenerCategoriasFiltradas();
    }

    private void iniciarDatos() {
        editTextCantidad = findViewById(R.id.editTextCantidad);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        radioGroupTipoMovimiento = findViewById(R.id.radioGroupEditMovimientoTipoMovimiento);
        radioButtonGasto = findViewById(R.id.radioButtonEditarMoviminetoGasto);
        radioButtonIngreso = findViewById(R.id.radioButtonEditarMovimientoIngreso);
        recyclerViewCategorias = findViewById(R.id.recyclerViewEditarMoviminetoCategorias);
        buttonCrearMovimiento = findViewById(R.id.buttonCrearMovimiento);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));

        radioButtonGasto.setChecked(true);
        tipoMovimiento = "Gasto";
    }

    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        editTextFecha.setText(date);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextFecha.setText(date);
                    }
                },
                year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void crearMovimiento() {
        String cantidad = editTextCantidad.getText().toString();
        String fecha = editTextFecha.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();

        if (cantidad.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa una cantidad válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedRadioId = radioGroupTipoMovimiento.getCheckedRadioButtonId();

        if (selectedRadioId == radioButtonGasto.getId()) {
            tipoMovimiento = "Gasto";
        } else if (selectedRadioId == radioButtonIngreso.getId()) {
            tipoMovimiento = "Ingreso";
        } else {
            Toast.makeText(this, "Por favor, selecciona un tipo de movimiento.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = getIntent();
        int idCuenta = intent.getIntExtra("idCuentaMov", 0);
        int idUsuario = intent.getIntExtra("idUsuarioMov", 0);

        double cantidadDouble = Double.parseDouble(cantidad);

        CrearMovimiento crearMovimiento = new CrearMovimiento(this);
        crearMovimiento.crearMovimiento(tipoMovimiento, idCuenta, idUsuario, idCategoriaEscogido, cantidadDouble, fecha, descripcion, this);
    }

    @Override
    public void onCategoriasResult(List<Categoria> categorias) {
        listaCategorias = categorias;
        categoriaAdapter = new RecyclerAdapterCategoria(this, categorias);
        categoriaAdapter.setClickListener(this);
        // Configurar el FlexboxLayoutManager
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);

        recyclerViewCategorias.setLayoutManager(flexboxLayoutManager);
        recyclerViewCategorias.setAdapter(categoriaAdapter);
    }

    @Override
    public void onCategoriasError(String message) {
        Log.i("Error", "Error " + message);
    }

    @Override
    public void onMovimientoCreated() {
        Log.i("Movimiento CREADO", "Movimiento creado");
        finish();
    }

    @Override
    public void onMovimientoCreateError(String message) {
        Log.i("Error al crear movimiento", "Error " + message);
    }

    @Override
    public void onItemClick(View view, int position) {

        Categoria categoria = categoriaAdapter.getItem(position);
        Toast.makeText(this, "Categoría seleccionada: " + categoria.getNombre(), Toast.LENGTH_SHORT).show();
        idCategoriaEscogido = categoria.getId();
    }

    private void obtenerCategoriasFiltradas() {
        ObtenerCategoriaFiltrada obtenerCategoriaFiltrada = new ObtenerCategoriaFiltrada(this);
        obtenerCategoriaFiltrada.obtenerCategorias(tipoMovimiento, this);
        Log.i("Categorias Filtradas", listaCategorias.toString());
    }
}
