package com.sabine.proyecto_final.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Usuario;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText editOldPassword;
    private TextInputEditText editNewPassword;
    private TextInputEditText editConfirmPassword;
    private MaterialButton buttonSaveChanges;
    private AppDatabase db;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editOldPassword = findViewById(R.id.editOldPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        db = AppDatabase.getInstance(getApplicationContext());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.nav_cards:
                        startActivity(new Intent(getApplicationContext(), CreditCardActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.nav_profile:

                        return true;
                }
                return false;
            }
        });


        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        String oldPassword = editOldPassword.getText().toString();
        String newPassword = editNewPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();

        if (oldPassword.isEmpty()) {
            editOldPassword.setError("Ingrese su antigua contraseña");
            return;
        }

        if (!oldPassword.equals(user.getPassword())) {
            editOldPassword.setError("La contraseña antigua no coincide");
            return;
        }

        if (newPassword.isEmpty()) {
            editNewPassword.setError("Ingrese la nueva contraseña");
            return;
        }

        if (newPassword.length() < 6) {
            editNewPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            editConfirmPassword.setError("Las contraseñas no coinciden");
            return;
        }

        // Actualizar la contraseña en la base de datos
        user.setPassword(newPassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.usuarioDao().update(user);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ChangePasswordActivity.this, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }

    private void getUserFromDatabase(int userId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                user = db.usuarioDao().getById(userId);
            }
        }).start();
    }

    private int getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getInt("loggedUserId", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int userId = getUserIdFromSharedPreferences();
        getUserFromDatabase(userId);
    }
}
