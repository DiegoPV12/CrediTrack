package com.sabine.proyecto_final.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Adapters.RecyclerCreditCardAdapter;
import com.sabine.proyecto_final.activities.DAO.TarjetaDao;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Tarjeta;

import java.util.List;


public class CreditCardActivity extends AppCompatActivity {
    ImageView ivPayment;
    ImageView ivPurchase;
    ImageView ivRecord;
    Button btnRegister;
    private RecyclerView creditCardRecyclerView;
    private SharedPreferences sharedPreferences;
    private List<Tarjeta> tarjetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        ivPayment = findViewById(R.id.ivPayment);
        ivPurchase = findViewById(R.id.ivPurchase);
        ivRecord = findViewById(R.id.ivRecord);
        btnRegister = findViewById(R.id.btnRegister);

        creditCardRecyclerView = findViewById(R.id.creditCardReycler);

        AppDatabase db = AppDatabase.getInstance(this);
        TarjetaDao tarjetaDao = db.tarjetaDao();
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        int idUsuario = sharedPreferences.getInt("loggedUserId", -1);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_cards);

        Thread thread_cards = new Thread(new Runnable() {
            @Override
            public void run() {
                tarjetas = tarjetaDao.getByUsuario(idUsuario);
            }
        });
        thread_cards.start();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        return true;
                    case R.id.nav_cards:
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                }
                return false;
            }
        });

        ivPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tarjetas.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe registrar una tarjeta primero.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), RegisterPaymentActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ivPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tarjetas.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe registrar una tarjeta primero.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), RegisterPurchaseActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tarjetas.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe registrar una tarjeta primero.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterCardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        new Thread(() -> {

            // Si loggedUserId es -1, significa que no había ningún ID de usuario en SharedPreferences.
            if (idUsuario == -1) {
                // No se ha iniciado sesión
                Toast.makeText(this, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show();
                return;
            }
            List<Tarjeta> tarjetas = tarjetaDao.getByUsuario(idUsuario);

            runOnUiThread(() -> {
                LinearLayoutManager cardLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                creditCardRecyclerView.setLayoutManager(cardLayoutManager);
                creditCardRecyclerView.setAdapter(new RecyclerCreditCardAdapter(tarjetas));
            });
        }).start();
    }

}
