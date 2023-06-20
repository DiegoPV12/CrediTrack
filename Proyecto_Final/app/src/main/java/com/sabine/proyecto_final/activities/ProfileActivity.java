package com.sabine.proyecto_final.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Usuario;

public class ProfileActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editEmail;
    private Button editBtn;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editBtn = findViewById(R.id.editBtn);
        MaterialButton logoutBtn = findViewById(R.id.logoutBtn);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

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

                        return true;
                }
                return false;
            }
        });





        // Obtener el ID del usuario loggeado
        int userId = getUserIdFromSharedPreferences();

        // Obtener los datos del usuario de la base de datos
        getUserFromDatabase(userId);

        // Mostrar los datos del usuario en los EditText
        showUserData();

        // Configurar el botón de editar perfil
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la pantalla de edición de perfil
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }


    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("loggedUserId");
        editor.apply();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private int getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getInt("loggedUserId", -1);
    }

    private void getUserFromDatabase(int userId) {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = db.usuarioDao().getById(userId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Mostrar los datos del usuario en los EditText
                        showUserData();
                    }
                });
            }
        }).start();
    }

    private void showUserData() {
        if (user != null) {
            editName.setText(user.getUsername());
            editEmail.setText(user.getEmail());
        }
    }
}
