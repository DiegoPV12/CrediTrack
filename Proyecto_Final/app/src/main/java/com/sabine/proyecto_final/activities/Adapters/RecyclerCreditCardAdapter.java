package com.sabine.proyecto_final.activities.Adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Models.Tarjeta;

import java.util.List;

public class RecyclerCreditCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Tarjeta> tarjetas;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_CARD = 1;

    public RecyclerCreditCardAdapter(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_EMPTY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_card, parent, false);
            return new EmptyViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recycler, parent, false);
            return new CardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_CARD) {
            Tarjeta tarjeta = tarjetas.get(position);
            CardViewHolder cardViewHolder = (CardViewHolder) holder;

            cardViewHolder.tvBanco.setText(tarjeta.getBanco());
            cardViewHolder.tvNombre.setText(tarjeta.getNombre());
            cardViewHolder.tvNumero.setText(tarjeta.getNumero());
            cardViewHolder.tvFecha.setText(tarjeta.getFecha());
            cardViewHolder.tvCSV.setText(tarjeta.getCsv());
            String color = tarjeta.getColor();
            switch (color) {
                case "Amarillo":
                    cardViewHolder.linearLayoutColorFront.setBackgroundResource(R.drawable.gradient_yellow);
                    cardViewHolder.linearLayoutColorBack.setBackgroundResource(R.drawable.gradient_yellow);
                    break;
                case "Azul":
                    cardViewHolder.linearLayoutColorFront.setBackgroundResource(R.drawable.gradient_blue);
                    cardViewHolder.linearLayoutColorBack.setBackgroundResource(R.drawable.gradient_blue);
                    break;
                case "Naranja":
                    cardViewHolder.linearLayoutColorFront.setBackgroundResource(R.drawable.gradient_orange);
                    cardViewHolder.linearLayoutColorBack.setBackgroundResource(R.drawable.gradient_orange);
                    break;
                case "Púrpura":
                    cardViewHolder.linearLayoutColorFront.setBackgroundResource(R.drawable.gradient_purple);
                    cardViewHolder.linearLayoutColorBack.setBackgroundResource(R.drawable.gradient_purple);
                    break;
                case "Rojo":
                    cardViewHolder.linearLayoutColorFront.setBackgroundResource(R.drawable.gradient_red);
                    cardViewHolder.linearLayoutColorBack.setBackgroundResource(R.drawable.gradient_red);
                    break;
                case "Verde":
                    cardViewHolder.linearLayoutColorFront.setBackgroundResource(R.drawable.gradient_green);
                    cardViewHolder.linearLayoutColorBack.setBackgroundResource(R.drawable.gradient_green);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return tarjetas.isEmpty() ? 1 : tarjetas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return tarjetas.isEmpty() ? VIEW_TYPE_EMPTY : VIEW_TYPE_CARD;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvNumero, tvFecha, tvBanco, tvCSV;
        LinearLayout linearLayoutColorFront, linearLayoutColorBack;
        ViewFlipper viewFlipper;

        CardViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.cardName);
            tvNumero = itemView.findViewById(R.id.cardNumber);
            tvFecha = itemView.findViewById(R.id.cardExp);
            tvBanco = itemView.findViewById(R.id.bankName);
            tvCSV = itemView.findViewById(R.id.csv);
            linearLayoutColorFront = itemView.findViewById(R.id.linearLayoutColorFront);
            linearLayoutColorBack = itemView.findViewById(R.id.linearLayoutColorBack);
            viewFlipper = itemView.findViewById(R.id.viewFlipper);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(viewFlipper.getDisplayedChild() == 0) {
                        viewFlipper.showNext();
                    } else {
                        viewFlipper.showPrevious();
                    }
                }
            });
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
            // Aquí puedes inicializar las vistas de tu tarjeta en blanco si es necesario
        }
    }
}
