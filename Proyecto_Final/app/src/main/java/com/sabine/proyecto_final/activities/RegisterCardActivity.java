package com.sabine.proyecto_final.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.Adapters.RecyclerCreditCardAdapter;
import com.sabine.proyecto_final.activities.DAO.TarjetaDao;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Tarjeta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterCardActivity extends AppCompatActivity {

    private EditText editCardNumber;
    private EditText editCardName;
    private EditText editExpDate;
    private EditText editLimit;
    private EditText editCSV;
    private Spinner spinnerCurrency;
    private EditText editBankName;
    private Spinner spinnerColor;
    private Button registerCardButton;
    private TarjetaDao tarjetaDao;
    private SharedPreferences sharedPreferences;
    private RecyclerCreditCardAdapter adapter;
    private Tarjeta currentCard = new Tarjeta();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);

        editCardNumber = findViewById(R.id.editCardNumber);
        editCardName = findViewById(R.id.editCardName);
        editExpDate = findViewById(R.id.editExpDate);
        editCSV = findViewById(R.id.editCSV);
        editLimit = findViewById(R.id.editLimit);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        editBankName = findViewById(R.id.editBankName);
        spinnerColor = findViewById(R.id.spinnerColor);
        registerCardButton = findViewById(R.id.registerCardButton);

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "miapp").build();

        tarjetaDao = db.tarjetaDao();

        ArrayAdapter<CharSequence> adapterCurrency = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapterCurrency);

        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapterColor);


        editCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
            }
        });

        registerCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCard();
            }
        });
    }



    private void registerCard() {
        final String cardNumber = editCardNumber.getText().toString();
        final String expDate = editExpDate.getText().toString();
        final String limitStr = editLimit.getText().toString();
        final String bankName = editBankName.getText().toString();
        final String currency = spinnerCurrency.getSelectedItem().toString();
        final String color = spinnerColor.getSelectedItem().toString();
        final String titular = editCardName.getText().toString();
        final String CSV = editCSV.getText().toString();

        Pattern pattern = Pattern.compile("^[0-9 ]+$");
        Matcher matcher = pattern.matcher(cardNumber);

        // Verificar que los campos no estén vacíos
        if (cardNumber.isEmpty() || expDate.isEmpty() || limitStr.isEmpty() || bankName.isEmpty() || titular.isEmpty() ||CSV.isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que el número de tarjeta es válido
        if (cardNumber.length() != 19 || !matcher.matches()) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese un número de tarjeta válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que la fecha de expiración es válida
        if (!expDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese una fecha de expiración válida (MM/AA).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar que el límite es un número válido
        float limit;
        try {
            limit = Float.parseFloat(limitStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese un límite válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del usuario de SharedPreferences.
        // El segundo parámetro es un valor por defecto en caso de que "idUsuario" no exista en SharedPreferences.
        int idUsuario = sharedPreferences.getInt("loggedUserId", -1);

        // Si loggedUserId es -1, significa que no había ningún ID de usuario en SharedPreferences.
        if (idUsuario == -1) {
            // No se ha iniciado sesión
            Toast.makeText(this, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show();
            return;
        }
        final Tarjeta newCard = new Tarjeta(titular,cardNumber, expDate, bankName, limit, currency, idUsuario, color,CSV);

        new Thread(new Runnable() {
            @Override
            public void run() {
                tarjetaDao.insert(newCard);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Tarjeta registrada con éxito.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), CreditCardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }
}