package com.sabine.proyecto_final.activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.sabine.proyecto_final.R;

import java.util.List;

public class RecyclerIconAdapter extends RecyclerView.Adapter<RecyclerIconAdapter.ViewHolder> {

    private List<Integer> iconList;
    private int checkedPosition = -1; // posición inicial
    private Integer selectedIcon = null;

    public RecyclerIconAdapter(List<Integer> iconList) {
        this.iconList = iconList;
    }

    public Integer getSelectedIcon() {
        return selectedIcon;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ShapeableImageView iconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer icon = iconList.get(position);
        holder.iconImageView.setImageResource(icon);

        if (checkedPosition == position) {
            holder.iconImageView.setStrokeWidth(5); // resalta la imagen seleccionada
            selectedIcon = icon; // almacena el ícono seleccionado
        } else {
            holder.iconImageView.setStrokeWidth(0); // deselecciona la imagen
        }

        holder.iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }
}

