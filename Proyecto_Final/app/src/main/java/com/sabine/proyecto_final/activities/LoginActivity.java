package com.sabine.proyecto_final.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.DAO.UsuarioDao;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editMail;
    private EditText editPasswordLogin;
    private CheckBox savePassword;
    private Button buttonLogin;
    private TextView tvRegister;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editMail = findViewById(R.id.editMail);
        editPasswordLogin = findViewById(R.id.editPasswordLogin);
        savePassword = findViewById(R.id.savePassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        tvRegister = findViewById(R.id.tvRegister);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        usuarioDao = db.usuarioDao();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginUser();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser() {
        final String email = editMail.getText().toString();
        final String password = editPasswordLogin.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Por favor, introduzca un correo válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, introduzca una contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length()<8) {
            Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Usuario existingUser = usuarioDao.findByEmail(email);
                if (existingUser == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                if (!existingUser.getPassword().equals(password)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        if (savePassword.isChecked()) {
                            editor.putString("email", email);
                            editor.putString("password", password); // No es recomendable guardar contraseñas en SharedPreferences por temas de seguridad.
                        } else {
                            editor.remove("email");
                            editor.remove("password");
                        }

                        // Guarda el ID del usuario en SharedPreferences
                        editor.putInt("loggedUserId", existingUser.getId());

                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });


            }
        }).start();
    }

}
