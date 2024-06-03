package com.example.gestorgastos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gestorgastos.R;
import modelo.Divisa;

import java.util.List;

public class SpinnerAdapterDivisas extends ArrayAdapter<Divisa> {

    private Context context;
    private List<Divisa> divisas;

    public SpinnerAdapterDivisas(Context context, List<Divisa> divisas) {
        super(context, 0, divisas);
        this.context = context;
        this.divisas = divisas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_divisas, parent, false);
        }

        TextView textViewNombre = convertView.findViewById(R.id.textViewNombreDivisa);
        TextView textViewSimbolo = convertView.findViewById(R.id.textViewSimboloDivisa);

        Divisa currentDivisa = getItem(position);

        if (currentDivisa != null) {
            textViewNombre.setText(currentDivisa.getNombre());
            textViewSimbolo.setText(currentDivisa.getSimbolo());
        }

        return convertView;
    }
}
