package com.example.gestorgastos.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestorgastos.R;

import java.text.SimpleDateFormat;
import java.util.List;

import modelo.Categoria;
import modelo.Movimiento;


public class RecyclerAdapterMovimiento extends RecyclerView.Adapter<RecyclerAdapterMovimiento.ViewHolder> {
    private List<Movimiento> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public RecyclerAdapterMovimiento(Context context, List<Movimiento> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movimiento movimiento = mData.get(position);
        holder.myImagen.setImageBitmap(movimiento.getBitmap());
        holder.textoCategoria.setText(movimiento.getCategoria());
        holder.cantidad.setText(movimiento.getCantidad()+" "+movimiento.getDivisa());
        holder.myImagen.setBackgroundColor(movimiento.getIntColor());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = sdf.format(movimiento.getFecha());
        holder.fechaMovimento.setText(fechaFormateada);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
        ImageView myImagen;
        TextView textoCategoria;
        TextView cantidad;
        TextView fechaMovimento;
        CardView linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            myImagen = itemView.findViewById(R.id.imageViewCategoriaMovimiento);
            textoCategoria = itemView.findViewById(R.id.textViewNombreCategoriaMovimiento);
            cantidad=itemView.findViewById(R.id.textViewSaldoMovimiento);
            fechaMovimento=itemView.findViewById(R.id.textViewfechaMovimiento);
            linearLayout=itemView.findViewById(R.id.cardViewItemMovimientos);
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
            menu.add(getAdapterPosition(), 104, 1, "Borrar");
        }
    }

    public Movimiento getItem(int position) {
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

    public void addData(Movimiento cotegoria) {
        mData.add(cotegoria);
        notifyDataSetChanged();
    }
}

