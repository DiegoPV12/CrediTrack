package com.sabine.proyecto_final.activities.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Models.Tarjeta;
import com.sabine.proyecto_final.activities.Models.Transaccion;

import java.util.List;

public class RecyclerTransactionAdapter extends RecyclerView.Adapter<RecyclerTransactionAdapter.TransactionViewHolder> {

    private List<Transaccion> transactionList;
    private List<Tarjeta> cardList;

    public RecyclerTransactionAdapter(List<Transaccion> transactions, List<Tarjeta> cards) {
        this.transactionList = transactions;
        this.cardList = cards;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_recycler, parent, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaccion transaction = transactionList.get(position);
        Tarjeta card = getCardById(transaction.getIdTarjeta());
        holder.bind(transaction, card);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView descriptionTextView;
        private TextView dayTextView;
        private TextView amountTextView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView6);
            descriptionTextView = itemView.findViewById(R.id.tvDescription);
            dayTextView = itemView.findViewById(R.id.tvDay);
            amountTextView = itemView.findViewById(R.id.textView16);
        }

        public void bind(Transaccion transaction, Tarjeta card) {
            int image = Integer.parseInt(transaction.getIcono());
            imageView.setImageResource(image);
            descriptionTextView.setText(transaction.getDescripcion() + " " + transaction.id);

            String lastFourDigits = getLastFourDigitsOfCard(card.getNumero());
            String fechaWithLastFourDigits = transaction.getFecha() + " \n" + lastFourDigits;

            dayTextView.setText(fechaWithLastFourDigits);
            boolean type = transaction.isTipo();
            if(type==false){
                amountTextView.setText("+ " + String.valueOf(transaction.getMonto())); // Convertir el monto a String
                amountTextView.setTextColor(Color.parseColor("#50C878"));
            } else {
                amountTextView.setText("- "+String.valueOf(transaction.getMonto())); // Convertir el monto a String
                amountTextView.setTextColor(Color.parseColor("#ff0000"));
            }

        }

        private String getLastFourDigitsOfCard(String cardNumber) {
            if (cardNumber.length() >= 4) {
                return cardNumber.substring(cardNumber.length() - 4);
            }
            return "";
        }
    }

    private Tarjeta getCardById(int idTarjeta) {
        for (Tarjeta card : cardList) {
            if (card.getId() == idTarjeta) {
                return card;
            }
        }
        return null;
    }
}
