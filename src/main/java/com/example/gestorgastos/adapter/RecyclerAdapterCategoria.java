package com.example.gestorgastos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestorgastos.R;

import java.util.List;

import modelo.Categoria;

public class RecyclerAdapterCategoria extends RecyclerView.Adapter<RecyclerAdapterCategoria.ViewHolder> {
    private List<Categoria> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    //Posición del ítem seleccionado, utilizada para resaltar el ítem seleccionado.
    private int selectedPosition = RecyclerView.NO_POSITION;

    public RecyclerAdapterCategoria(Context context, List<Categoria> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = mData.get(position);
        holder.myImagen.setImageBitmap(categoria.getBitmap());
        holder.textoCategoria.setText(categoria.getNombre());

        //Cambia el color de fondo del LinearLayout dependiendo de si el ítem está seleccionado.
        holder.myImagen.setBackgroundColor(categoria.getIntColor());

        if (position == selectedPosition) {
            holder.linearLayout.setBackgroundColor(categoria.getIntColor());
        } else {
            holder.linearLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImagen;
        TextView textoCategoria;
        LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            myImagen = itemView.findViewById(R.id.imageViewCategoria);
            textoCategoria = itemView.findViewById(R.id.textViewCategoryName);
            linearLayout = itemView.findViewById(R.id.linearLayoutCategoria);
            itemView.setOnClickListener(this);
        }
        //Cambia el color de fondo del LinearLayout dependiendo de si el ítem está seleccionado.
        @Override
        public void onClick(View view) {
            int previousPosition = selectedPosition;
            selectedPosition = getAdapterPosition();
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public Categoria getItem(int position) {
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

    public void addData(Categoria categoria) {
        mData.add(categoria);
        notifyDataSetChanged();
    }
}
