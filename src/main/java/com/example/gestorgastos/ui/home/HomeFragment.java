package com.example.gestorgastos.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestorgastos.MainActivityCrearMovimiento;
import com.example.gestorgastos.MainActivityEditarMovimiento;
import com.example.gestorgastos.R;
import com.example.gestorgastos.adapter.RecyclerAdapterMovimiento;
import com.example.gestorgastos.crud.categoria.SacarPorcentajeFiltrado;
import com.example.gestorgastos.crud.cuenta.VerUnaCuenta;
import com.example.gestorgastos.crud.movimineto.BorrarMovimiento;
import com.example.gestorgastos.crud.movimineto.VerMovimientosFiltrados;
import com.example.gestorgastos.databinding.FragmentHomeBinding;
import com.example.gestorgastos.utils.Calculos;
import com.example.gestorgastos.utils.DateUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import androidx.core.util.Pair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import modelo.Cuenta;
import modelo.Movimiento;
import modelo.Porcentaje;

public class HomeFragment extends Fragment implements VerMovimientosFiltrados.OnMovimientosResultListener, RecyclerAdapterMovimiento.ItemClickListener, SacarPorcentajeFiltrado.PorcentajeFiltradoListener, BorrarMovimiento.BorrarMovimientoCallback, VerUnaCuenta.OnCuentasResultListener {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RecyclerAdapterMovimiento recyclerAdapterMovimiento;
    private int idUsuario;
    private int idCuenta;
    private List<Movimiento> listaMovimiento = new ArrayList<>();
    private VerMovimientosFiltrados verMovimientosFiltrados;
    private String tipoMovimiento = "Ingreso";
    private Button buttonAniadirMovimiento;
    private Cuenta cuenta;
    private TextView saldoTotal;
    private TextView textViewCenterSaldo;
    private String startDate = null;
    private String endDate = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.recyclerViewMoviemientos);
        buttonAniadirMovimiento = root.findViewById(R.id.buttonAniadirMovimiento);
        saldoTotal = root.findViewById(R.id.textViewTotalDinero);
        textViewCenterSaldo = binding.textViewCenterSaldo;

        setupPieChart(new ArrayList<>());

        Intent intent = getActivity().getIntent();
        idCuenta = intent.getIntExtra("idCuenta", 0);
        idUsuario = intent.getIntExtra("idUsuario", 0);
        Log.i("pruebas"," "+idCuenta);
        cargarCuenta();

        TabLayout tabLayout = binding.tabLayout;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tipoMovimiento = tab.getText().toString();
                Toast.makeText(getContext(), "Estás en la pestaña: " + tipoMovimiento, Toast.LENGTH_SHORT).show();
                cargarMovimientosFiltrados(startDate, endDate);
                cargarPorcentajesFiltrados(startDate, endDate);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        TabLayout innerTabLayout = binding.innerTabLayout;
        innerTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedTabText = tab.getText().toString();

                switch (selectedTabText) {
                    case "Todo":
                        startDate = null;
                        endDate = null;
                        break;
                    case "Día":
                        String currentDate = DateUtils.getCurrentDate();
                        startDate = currentDate;
                        endDate = currentDate;
                        break;
                    case "Semana":
                        String[] currentWeekDates = DateUtils.getCurrentWeekDates();
                        startDate = currentWeekDates[0];
                        endDate = currentWeekDates[1];
                        break;
                    case "Mes":
                        String[] currentMonthDates = DateUtils.getCurrentMonthDates();
                        startDate = currentMonthDates[0];
                        endDate = currentMonthDates[1];
                        break;
                    case "Año":
                        String[] currentYearDates = DateUtils.getCurrentYearDates();
                        startDate = currentYearDates[0];
                        endDate = currentYearDates[1];
                        break;
                    case "Periodo":
                        showDateRangePicker();
                        break;
                }

                cargarMovimientosFiltrados(startDate, endDate);
                cargarPorcentajesFiltrados(startDate, endDate);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        buttonAniadirMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), MainActivityCrearMovimiento.class);
                intent1.putExtra("idCuentaMov", idCuenta);
                intent1.putExtra("idUsuarioMov", idUsuario);
                startActivity(intent1);
            }
        });

        cargarMovimientosFiltrados(startDate, endDate);
        cargarPorcentajesFiltrados(startDate, endDate);
        return root;
    }

    private void setupPieChart(List<Porcentaje> porcentajes) {
        PieChart pieChart = binding.pieChart;

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (Porcentaje porcentaje : porcentajes) {
            entries.add(new PieEntry((float) porcentaje.getPorcentaje(), porcentaje.getNombreCategoria()));
            colors.add(porcentaje.getIntColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, tipoMovimiento);
        PieData data = new PieData(dataSet);
        dataSet.setColors(colors);

        // Ajustes para el aspecto del gráfico
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(3f); // Espacio entre los segmentos del gráfico
        dataSet.setSelectionShift(5f); // Cuánto se desplaza el segmento seleccionado

        // Personaliza el formato del texto de los valores
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.2f%%", value);
            }
        });

        pieChart.setHoleRadius(60f); // Tamaño del agujero en el centro
        pieChart.setTransparentCircleRadius(65f); // Radio del círculo transparente sobre el agujero central
        pieChart.setHoleColor(Color.TRANSPARENT); // Establece el color del agujero central como transparente

        pieChart.setBackgroundColor(Color.TRANSPARENT); // Establece el fondo del PieChart como transparente

        pieChart.getLegend().setTextColor(Color.WHITE); // Establece el color del texto de la leyenda como blanco
        pieChart.getLegend().setFormToTextSpace(8f); // Ajusta la distancia entre la barra de color y el texto en la leyenda

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false); // Desactiva la descripción del gráfico
        pieChart.setDrawEntryLabels(false); // Desactiva las etiquetas de entrada
        pieChart.invalidate(); // Actualiza el gráfico
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void cargarMovimientosFiltrados(String startDate, String endDate) {
        verMovimientosFiltrados = new VerMovimientosFiltrados(getContext());
        verMovimientosFiltrados.obtenerMovimientos(idUsuario, idCuenta, tipoMovimiento, startDate, endDate, this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Movimiento movimiento = recyclerAdapterMovimiento.getItem(position);
        Toast.makeText(getContext(), "" + movimiento.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivityEditarMovimiento.class);
        intent.putExtra("MOVIMIENTO_EXTRA", movimiento);
        startActivity(intent);
    }

    @Override
    public void onMovimientosFiltradosResult(List<Movimiento> movimientos) {
        listaMovimiento.clear();
        if (movimientos.isEmpty()) {
            Toast.makeText(getContext(), "No hay movimientos filtrados para mostrar", Toast.LENGTH_SHORT).show();
            recyclerAdapterMovimiento.notifyDataSetChanged();
            textViewCenterSaldo.setText("0 " + (cuenta != null ? cuenta.getSimbolo() : ""));
        } else {
            listaMovimiento.addAll(movimientos);
            //Toast.makeText(getContext(), "Movimientos filtrados cargados correctamente", Toast.LENGTH_SHORT).show();
        }

        if (recyclerAdapterMovimiento == null) {
            recyclerAdapterMovimiento = new RecyclerAdapterMovimiento(getContext(), listaMovimiento);
            recyclerAdapterMovimiento.setClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerAdapterMovimiento);
        } else {
            recyclerAdapterMovimiento.notifyDataSetChanged();
        }

        Log.i("Movimientos filtrados", listaMovimiento.toString());
        textViewCenterSaldo.setText(Calculos.suma(listaMovimiento) + " " + (cuenta != null ? cuenta.getSimbolo() : ""));
    }

    @Override
    public void onMovimientosFiltradosError(String message) {
        Log.i("Error movimientos filtrados", message);
        listaMovimiento.clear();
        if (recyclerAdapterMovimiento != null) {
            recyclerAdapterMovimiento.notifyDataSetChanged();
        }
        textViewCenterSaldo.setText("0 " + (cuenta != null ? cuenta.getSimbolo() : ""));
        Toast.makeText(getContext(), "No se encontraron movimientos", Toast.LENGTH_SHORT).show();
    }

    private void cargarPorcentajesFiltrados(String startDate, String endDate) {
        SacarPorcentajeFiltrado sacarPorcentajeFiltrado = new SacarPorcentajeFiltrado(getContext());
        sacarPorcentajeFiltrado.obtenerPorcentajes(tipoMovimiento, idCuenta, startDate, endDate, this);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 104:
                int position = item.getGroupId();
                Movimiento movimiento = recyclerAdapterMovimiento.getItem(position);
                BorrarMovimiento borrarMovimiento = new BorrarMovimiento(getContext());
                borrarMovimiento.borrarMovimiento(movimiento.getIdMovimiento(), this);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onSuccessBorrarDatos(String message) {
        Toast.makeText(getContext(), "Movimiento borrado: " + message, Toast.LENGTH_SHORT).show();
        Log.i("movimiento borrado", message);
        cargarMovimientosFiltrados(startDate, endDate);
        cargarPorcentajesFiltrados(startDate, endDate);
        cargarCuenta();
    }

    @Override
    public void onErrorBorrarDatos(String error) {
        Toast.makeText(getContext(), "Error al borrar movimiento: " + error, Toast.LENGTH_SHORT).show();
        Log.i("Error borrar movimiento", error);
        cargarMovimientosFiltrados(startDate, endDate);
        cargarPorcentajesFiltrados(startDate, endDate);
        cargarCuenta();
    }

    private void showDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Selecciona un rango de fechas");

        final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(getParentFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Long startMillis = selection.first;
                Long endMillis = selection.second;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                startDate = sdf.format(startMillis);
                endDate = sdf.format(endMillis);

                cargarCuenta();

                cargarMovimientosFiltrados(startDate, endDate);
                cargarPorcentajesFiltrados(startDate, endDate);
            }
        });
    }

    @Override
    public void onSuccessPorcentajeFiltrado(List<Porcentaje> porcentajes) {
        if (porcentajes.isEmpty()) {
            //Toast.makeText(getContext(), "No hay datos para mostrar en el gráfico", Toast.LENGTH_SHORT).show();
            Log.i("Error porcentaje filtrados", "No hay datos ");
            listaMovimiento.clear();
            if (recyclerAdapterMovimiento != null) {
                recyclerAdapterMovimiento.notifyDataSetChanged();
            }
            textViewCenterSaldo.setText("0 " + (cuenta != null ? cuenta.getSimbolo() : ""));
            //Toast.makeText(getContext(), "No se encontraron movimientos", Toast.LENGTH_SHORT).show();
            Log.i("Error porcentaje filtrados", "No hay datos ");

            setupPieChart(new ArrayList<>()); // Vaciar el gráfico
        } else {
            setupPieChart(porcentajes);
        }
    }

    @Override
    public void onErrorPorcentajeFiltrado(String message) {
        //Toast.makeText(getContext(), "Error porcentaje filtrado: " + message, Toast.LENGTH_SHORT).show();

        setupPieChart(new ArrayList<>()); // Vaciar el gráfico
    }

    @Override
    public void onUnaCuentaResult(ArrayList<Cuenta> cuentas) {
        if (!cuentas.isEmpty()) {
            cuenta = cuentas.get(0);
            saldoTotal.setText(cuenta.getSaldo() + " " + cuenta.getSimbolo());
        } else {
            cuenta = null;
            saldoTotal.setText("0");
        }
    }

    @Override
    public void onUnaCuentaNotFound(String message) {
        Toast.makeText(getContext(), "Error en cuenta: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnaCuentaError(String message) {
        Toast.makeText(getContext(), "Error en cuenta: " + message, Toast.LENGTH_SHORT).show();
    }

    private void cargarCuenta() {
        VerUnaCuenta verUnaCuenta = new VerUnaCuenta(getContext());
        verUnaCuenta.obtenerCuenta(idUsuario, idCuenta, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarMovimientosFiltrados(startDate, endDate);
        cargarPorcentajesFiltrados(startDate, endDate);
        cargarCuenta();
    }
}
