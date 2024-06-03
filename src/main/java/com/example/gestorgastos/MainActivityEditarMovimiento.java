package com.example.gestorgastos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestorgastos.adapter.RecyclerAdapterCategoria;
import com.example.gestorgastos.crud.categoria.ObtenerCategoriaFiltrada;
import com.example.gestorgastos.crud.movimineto.EditarMovimiento;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import modelo.Categoria;
import modelo.Movimiento;

public class MainActivityEditarMovimiento extends AppCompatActivity implements
        ObtenerCategoriaFiltrada.OnCategoriasResultListener,
        EditarMovimiento.OnMovimientoEditedListener,
        RecyclerAdapterCategoria.ItemClickListener {

    private EditText editTextAmount, editTextDescription, editTextDate;
    private RadioGroup radioGroupTipoMovimiento;
    private RadioButton radioButtonIngreso, radioButtonGasto;
    private RecyclerView recyclerViewCategorias;
    private RecyclerAdapterCategoria recyclerAdapterCategoria;
    private List<Categoria> listaCategoria = new ArrayList<>();
    private Button buttonSave, buttonBack;
    private Movimiento movimiento;
    private int idCategoria;
    private final Calendar calendar = Calendar.getInstance();
    private String tipoMovimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_editar_movimiento);

        inicializarVistas();

        configurarDatePicker();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MOVIMIENTO_EXTRA")) {
            movimiento = intent.getParcelableExtra("MOVIMIENTO_EXTRA");
            idCategoria = movimiento.getIdMovimiento();
            tipoMovimiento=movimiento.getTipoMovimiento();
            if (movimiento != null) {

                cargarDatos();
                Toast.makeText(this, "Movimiento: " + movimiento.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Movimiento es null", Toast.LENGTH_SHORT).show();
            }
        }

        // Establecer el listener para cambios en los RadioButtons
        radioGroupTipoMovimiento.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonEditarMoviminetoGasto) {
                tipoMovimiento = "Gasto";
                cargarCategorias(tipoMovimiento);
            } else if (checkedId == R.id.radioButtonEditarMovimientoIngreso) {
                tipoMovimiento = "Ingreso";
                cargarCategorias(tipoMovimiento);
            }
        });

        cargarRecyclerView();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recogerDatosYEditarMovimiento();
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void inicializarVistas() {
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        radioGroupTipoMovimiento = findViewById(R.id.radioGroupTipoMovimiento);
        radioButtonIngreso = findViewById(R.id.radioButtonEditarMovimientoIngreso);
        radioButtonGasto = findViewById(R.id.radioButtonEditarMoviminetoGasto);
        recyclerViewCategorias = findViewById(R.id.recyclerViewEditarMoviminetoCategorias);
        buttonSave = findViewById(R.id.EditarMovimientoGuardar);
        buttonBack=findViewById(R.id.buttonEditatMovimientoSalir);
    }

    private void configurarDatePicker() {
        final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarEditTextFecha();
        };

        editTextDate.setOnClickListener(v -> new DatePickerDialog(MainActivityEditarMovimiento.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void actualizarEditTextFecha() {
        String formatoFecha = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha, Locale.US);
        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void cargarDatos() {
        if (movimiento != null) {
            editTextAmount.setText(String.valueOf(movimiento.getCantidad()));
            editTextDescription.setText(movimiento.getDescripcion());
            editTextDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(movimiento.getFecha()));

            if ("Ingreso".equals(movimiento.getTipoMovimiento())) {
                radioButtonIngreso.setChecked(true);
                cargarCategorias("Ingreso");
            } else if ("Gasto".equals(movimiento.getTipoMovimiento())) {
                radioButtonGasto.setChecked(true);
                cargarCategorias("Gasto");
            }
        }
    }

    private void cargarCategorias(String tipoMovimiento) {
        ObtenerCategoriaFiltrada obtenerCategoriaFiltrada = new ObtenerCategoriaFiltrada(this);
        obtenerCategoriaFiltrada.obtenerCategorias(tipoMovimiento, this);
    }

    private void cargarRecyclerView() {
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterCategoria = new RecyclerAdapterCategoria(this, listaCategoria);
        recyclerAdapterCategoria.setClickListener(this);
        // Configurar el FlexboxLayoutManager
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);

        recyclerViewCategorias.setLayoutManager(flexboxLayoutManager);
        recyclerViewCategorias.setAdapter(recyclerAdapterCategoria);

    }

    private void recogerDatosYEditarMovimiento() {
        String cantidadStr = editTextAmount.getText().toString();
        String descripcionStr = editTextDescription.getText().toString();
        String fechaStr = editTextDate.getText().toString();

        if (idCategoria == 0) {
            Toast.makeText(this, "Seleccione una categoría", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tipoMovimiento == null || tipoMovimiento.isEmpty()) {
            Toast.makeText(this, "Seleccione el tipo de movimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        EditarMovimiento editarMovimiento = new EditarMovimiento(MainActivityEditarMovimiento.this);
        editarMovimiento.editarMovimiento(
                String.valueOf(movimiento.getIdMovimiento()),
                tipoMovimiento,
                idCategoria,
                Double.parseDouble(cantidadStr),
                fechaStr,
                descripcionStr,
                MainActivityEditarMovimiento.this
        );
    }

    @Override
    public void onCategoriasResult(List<Categoria> categorias) {
        listaCategoria.clear();
        listaCategoria.addAll(categorias);
        recyclerAdapterCategoria.notifyDataSetChanged();
    }

    @Override
    public void onCategoriasError(String message) {
        Log.i("error categorias", message);
        Toast.makeText(MainActivityEditarMovimiento.this, "Error en categorias"+ message, Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onMovimientoEdited() {
        Toast.makeText(MainActivityEditarMovimiento.this, "Movimiento editado", Toast.LENGTH_SHORT).show();
        Log.i("Movimiento editado", "Hecho");
    }

    @Override
    public void onMovimientoEditError(String message) {
        Log.i("error editando movimiento", message);
        Toast.makeText(MainActivityEditarMovimiento.this, "error editando movimiento", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(View view, int position) {
        Categoria categoria=recyclerAdapterCategoria.getItem(position);
        idCategoria=categoria.getId();
        Toast.makeText(this,"Categoria escogida "+categoria.getNombre(),Toast.LENGTH_SHORT);
    }
}
