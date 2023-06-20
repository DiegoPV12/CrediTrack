package com.sabine.proyecto_final.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.DAO.UsuarioDao;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Usuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editUsername;
    private EditText editEmail;
    private EditText editPassword;
    private EditText confirmPassword;
    private Button registerButton;
    private TextView tvLogin;
    private AppDatabase db;


    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        tvLogin = findViewById(R.id.tvLogin);
        registerButton = findViewById(R.id.registerButton);

        db = AppDatabase.getInstance(getApplicationContext());


        usuarioDao = db.usuarioDao();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        final String name = editName.getText().toString();
        final String username = editUsername.getText().toString();
        final String email = editEmail.getText().toString();
        final String password = editPassword.getText().toString();
        final String confirmPasswordStr = confirmPassword.getText().toString();

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile("^[a-zA-Z ]+$");
        matcher = pattern.matcher(name);
        if (name.isEmpty() || !matcher.matches()) {
            Toast.makeText(getApplicationContext(), "Por favor, introduzca un nombre válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        matcher = pattern.matcher(username);
        if (username.isEmpty() || !matcher.matches()) {
            Toast.makeText(getApplicationContext(), "Por favor, introduzca un nombre de usuario válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Por favor, introduzca un correo válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty() || password.length()<8) {
            Toast.makeText(getApplicationContext(), "Por favor, introduzca una contraseña válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPasswordStr)) {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Las operaciones de base de datos deben realizarse en un hilo separado del hilo principal de la interfaz de usuario.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Comprueba si ya existe un usuario con este correo electrónico.
                Usuario existingUser = usuarioDao.findByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ya existe un usuario con este correo electrónico.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                // Si llegamos a este punto, los datos de entrada son válidos y no existe un usuario con este correo electrónico.
                // Ahora podríamos intentar registrar el usuario en la base de datos.
                Usuario nuevoUsuario = new Usuario(name, email, username, password);  // Asume que el ID se genera automáticamente.
                usuarioDao.insert(nuevoUsuario);

                Usuario newUser = usuarioDao.findByEmail(email);
                SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("loggedUserId", newUser.getId());
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), LoginSuccessActivity.class);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(getApplicationContext(), "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
