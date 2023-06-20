package com.sabine.proyecto_final.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationBarView;
import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Adapters.RecyclerTransactionAdapter;
import com.sabine.proyecto_final.activities.DAO.TarjetaDao;
import com.sabine.proyecto_final.activities.DAO.TransaccionDao;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Tarjeta;
import com.sabine.proyecto_final.activities.Models.Transaccion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private TarjetaDao tarjetaDao;
    private TransaccionDao transaccionDao;
    private Spinner cardSpinner;

    private SharedPreferences sharedPreferences;

    private ImageView pagosSelect,comprasSelect,allSelect;
    private TextView transactionTitle;

    private TextView totalAmount;
    private List<Tarjeta> tarjetas;
    private List<Transaccion> transacciones;
    private RecyclerView transactionsRecyclerView;

    private Tarjeta selectedCard;

    boolean flag = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

       AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        // DAOS
        tarjetaDao = AppDatabase.getInstance(this).tarjetaDao();
        transaccionDao = AppDatabase.getInstance(this).transaccionDao();
        //

        //VISTAS
        cardSpinner = findViewById(R.id.card_spinner);
        pagosSelect = findViewById(R.id.ivPayment);
        comprasSelect = findViewById(R.id.ivPurchase);
        allSelect = findViewById(R.id.ivRecord);
        transactionTitle = findViewById(R.id.header);
        transactionsRecyclerView = findViewById(R.id.transactions);
        totalAmount = findViewById(R.id.totalAmount);

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cards);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        return true;
                    case R.id.nav_cards:
                        startActivity(new Intent(getApplicationContext(), CreditCardActivity.class));
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                }
                return false;
            }
        });

        //CHIP
        Chip orderChip = findViewById(R.id.order_chip);
        ColorStateList originalColor = orderChip.getChipBackgroundColor();

        int userId = getLoggedInUserId();


        orderChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (transacciones == null || transacciones.isEmpty()) {
                    return;
                }
                if (isChecked) {
                    orderChip.setText("Ascendente");
                    orderChip.setChipIconResource(R.drawable.arrowup);
                    orderChip.setChipBackgroundColorResource(R.color.theme);
                    if (!transacciones.isEmpty()) {
                        Collections.sort(transacciones, (t1, t2) -> t1.getFecha().compareTo(t2.getFecha()));
                        transactionsRecyclerView.getAdapter().notifyDataSetChanged();
                    }

                } else {
                    orderChip.setText("Descendente");
                    orderChip.setChipIconResource(R.drawable.arrowdown);
                    orderChip.setChipBackgroundColor(originalColor);
                    if (!transacciones.isEmpty()) {
                        // Ordenar las transacciones en orden descendente
                        Collections.sort(transacciones, (t1, t2) -> t2.getFecha().compareTo(t1.getFecha()));
                        // Notificar al adaptador del RecyclerView que los datos han cambiado
                        transactionsRecyclerView.getAdapter().notifyDataSetChanged();
                    }

                }

            }
        });



        // Obtener las tarjetas del usuario en un hilo separado
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tarjetas = tarjetaDao.getByUsuario(userId);

                // Actualizar la interfaz de usuario con las tarjetas obtenidas
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Crear una lista de números de tarjeta
                        List<String> cardNumbers = new ArrayList<>();
                        cardNumbers.add("Todas");
                        for (Tarjeta tarjeta : tarjetas) {
                            String cardnumber = tarjeta.getNumero().substring(15);
                            String text = tarjeta.getBanco() + " - "+ cardnumber;
                            cardNumbers.add(text);
                        }

                        // Crear un ArrayAdapter para el spinner y asignar la lista de números de tarjeta
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(RecordActivity.this, android.R.layout.simple_spinner_item, cardNumbers);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cardSpinner.setAdapter(spinnerAdapter);
                    }
                });
            }
        });
        thread.start();

        // Establecer un listener para el spinner
        cardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (tarjetas != null && tarjetas.size() > position) {
                    // Obtener la tarjeta seleccionada desde la lista de tarjetas
                     selectedCard = tarjetas.get(position);
                     if(selectedCard == tarjetas.get(0)){
                         flag = true;
                     } else {
                         flag = false;
                     }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Manejar el caso en el que no se haya seleccionado ninguna tarjeta
            }
        });


        pagosSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    // Obtener el ID del usuario logueado desde SharedPreferences

                    // Actualizar las transacciones con las transacciones que resultan de la consulta
                    if(flag){
                        transacciones = transaccionDao.getAllTransactions(userId,false);
                    } else{
                        transacciones = transaccionDao.getAllTransactionsByType(userId, false, selectedCard.getId());
                    }


                    // Actualizar la interfaz de usuario en el hilo principal
                    runOnUiThread(() -> {
                        LinearLayoutManager transactionsLayoutManager = new LinearLayoutManager(RecordActivity.this, LinearLayoutManager.VERTICAL, false);
                        transactionsRecyclerView.setLayoutManager(transactionsLayoutManager);
                        transactionsRecyclerView.setAdapter(new RecyclerTransactionAdapter(transacciones, tarjetas));
                        transactionTitle.setText("Últimos Pagos");
                    });
                }).start();

                new Thread(() -> {
                    // Obtener el ID del usuario logueado desde SharedPreferences
                    float suma;
                    if(flag){
                        suma = transaccionDao.sumaTransaccionPorUsuario(userId, false);
                    } else{
                        suma = transaccionDao.sumaTransaccionPorTarjeta(selectedCard.getId(), false);
                    }

                    // Actualizar la interfaz de usuario en el hilo principal
                    runOnUiThread(() -> {
                        totalAmount.setText("Total: " + String.valueOf(suma));
                    });
                }).start();

            }
        });

        comprasSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    // Obtener el ID del usuario logueado desde SharedPreferences

                    // Actualizar las transacciones con las transacciones que resultan de la consulta
                    if(flag){
                        transacciones = transaccionDao.getAllTransactions(userId,true);
                    } else{
                        transacciones = transaccionDao.getAllTransactionsByType(userId, true, selectedCard.getId());
                    }

                    // Actualizar la interfaz de usuario en el hilo principal
                    runOnUiThread(() -> {
                        LinearLayoutManager transactionsLayoutManager = new LinearLayoutManager(RecordActivity.this, LinearLayoutManager.VERTICAL, false);
                        transactionsRecyclerView.setLayoutManager(transactionsLayoutManager);
                        transactionsRecyclerView.setAdapter(new RecyclerTransactionAdapter(transacciones, tarjetas));
                        transactionTitle.setText("Últimas Compras");
                    });
                }).start();

                new Thread(() -> {
                    // Obtener el ID del usuario logueado desde SharedPreferences
                    float suma;
                    if(flag){
                        suma = transaccionDao.sumaTransaccionPorUsuario(userId, true);
                    } else{
                        suma = transaccionDao.sumaTransaccionPorTarjeta(selectedCard.getId(), true);
                    }

                    // Actualizar la interfaz de usuario en el hilo principal
                    runOnUiThread(() -> {
                        totalAmount.setText("Total: " + String.valueOf(suma));
                    });
                }).start();
            }
        });

        allSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    // Obtener el ID del usuario logueado desde SharedPreferences

                    // Actualizar las transacciones con las transacciones que resultan de la consulta
                    if(flag){
                        transacciones = transaccionDao.getTransactionsByUser(userId);
                    } else {
                        transacciones = transaccionDao.getAllTransactionsByUserAndCard(userId, selectedCard.getId());
                    }

                    // Actualizar la interfaz de usuario en el hilo principal
                    runOnUiThread(() -> {
                        LinearLayoutManager transactionsLayoutManager = new LinearLayoutManager(RecordActivity.this, LinearLayoutManager.VERTICAL, false);
                        transactionsRecyclerView.setLayoutManager(transactionsLayoutManager);
                        transactionsRecyclerView.setAdapter(new RecyclerTransactionAdapter(transacciones, tarjetas));
                        transactionTitle.setText("Últimas Transacciones");
                        totalAmount.setText("");
                    });
                }).start();
            }
        });








    }
    private int getLoggedInUserId() {
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getInt("loggedUserId", -1);
    }

}