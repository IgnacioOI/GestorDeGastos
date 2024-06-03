package com.example.gestorgastos.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestorgastos.R;

import java.util.List;

import modelo.Cuenta;

public class RecyclerAdapterCuentas extends RecyclerView.Adapter<RecyclerAdapterCuentas.ViewHolder> {
    private List<Cuenta> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public RecyclerAdapterCuentas(Context context, List<Cuenta> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cuenta cuenta = mData.get(position);
        holder.myTextView.setText(cuenta.getNombre());
        holder.textoSaldo.setText(String.valueOf(cuenta.getSaldo())  + cuenta.getSimbolo());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView myTextView;
        TextView textoSaldo;
        CardView linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textViewNombreCuenta);
            textoSaldo = itemView.findViewById(R.id.textViewSaldoCuenta);
            linearLayout = itemView.findViewById(R.id.carViewItemCuentas);
            linearLayout.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Opciones");
            menu.add(getAdapterPosition(), 103, 1, "Borrar");
        }
    }

    public Cuenta getItem(int position) {
        return mData.get(position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void addData(Cuenta cuenta) {
        mData.add(cuenta);
        notifyDataSetChanged();
    }
}
