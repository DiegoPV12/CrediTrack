package com.sabine.proyecto_final.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Adapters.RecyclerCreditCardAdapter;
import com.sabine.proyecto_final.activities.Adapters.RecyclerTransactionAdapter;
import com.sabine.proyecto_final.activities.DAO.TarjetaDao;
import com.sabine.proyecto_final.activities.DAO.TransaccionDao;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Tarjeta;
import com.sabine.proyecto_final.activities.Models.Transaccion;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView creditCardRecyclerView;
    private RecyclerView transactionsRecyclerView;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        creditCardRecyclerView = findViewById(R.id.creditCards);
        transactionsRecyclerView = findViewById(R.id.transactions);

        AppDatabase db = AppDatabase.getInstance(this);
        TarjetaDao tarjetaDao = db.tarjetaDao();
        TransaccionDao transaccionDao = db.transaccionDao();
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
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

        new Thread(() -> {
            int idUsuario = sharedPreferences.getInt("loggedUserId", -1);

            // Si loggedUserId es -1, significa que no había ningún ID de usuario en SharedPreferences.
            if (idUsuario == -1) {
                // No se ha iniciado sesión
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show());
                return;
            }

            List<Tarjeta> tarjetas = tarjetaDao.getByUsuario(idUsuario);

            runOnUiThread(() -> {
                LinearLayoutManager cardLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                creditCardRecyclerView.setLayoutManager(cardLayoutManager);
                creditCardRecyclerView.setAdapter(new RecyclerCreditCardAdapter(tarjetas));
            });
        }).start();

        new Thread(() -> {
            int idUsuario = sharedPreferences.getInt("loggedUserId", -1);

            // Si loggedUserId es -1, significa que no había ningún ID de usuario en SharedPreferences.
            if (idUsuario == -1) {
                // No se ha iniciado sesión
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show());
                return;
            }

            List<Transaccion> transacciones = transaccionDao.getTransactionsByUser(idUsuario);
            List<Tarjeta> tarjetas = tarjetaDao.getByUsuario(idUsuario);

            runOnUiThread(() -> {
                LinearLayoutManager transactionsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                transactionsRecyclerView.setLayoutManager(transactionsLayoutManager);
                transactionsRecyclerView.setAdapter(new RecyclerTransactionAdapter(transacciones, tarjetas));
            });
        }).start();

    }
}
