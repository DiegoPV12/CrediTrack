package com.sabine.proyecto_final.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Usuario;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editName, editEmail;
    private Button saveEdit;
    private Usuario user;
    private AppDatabase db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        saveEdit = findViewById(R.id.saveEdit);
        db = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);


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


        loadUserInfo();




        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        CardView cardView = findViewById(R.id.changePass);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadUserInfo() {
        // Obtiene el ID del usuario loggeado
        int userId = sharedPreferences.getInt("loggedUserId", -1);
        if (userId == -1) {
            // No se ha iniciado sesión
            Toast.makeText(this, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        // Busca el usuario en la base de datos
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = db.usuarioDao().getById(userId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Ahora llena los campos con la información del usuario
                        editName.setText(user.getUsername());
                        editEmail.setText(user.getEmail());
                    }
                });
            }
        }).start();
    }

    private void saveChanges() {
        String newUsername = editName.getText().toString();
        String newEmail = editEmail.getText().toString();

        // Validaciones
        if (newUsername.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Por favor, introduce tu nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newEmail.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Por favor, introduce tu email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualiza la información del usuario
        user.setUsername(newUsername);
        user.setEmail(newEmail);

        // Guarda los cambios en la base de datos
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.usuarioDao().update(user);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(EditProfileActivity.this, "Cambios guardados", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }
}
