package com.sabine.proyecto_final.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.sabine.proyecto_final.R;
import com.sabine.proyecto_final.activities.DAO.TarjetaDao;
import com.sabine.proyecto_final.activities.DAO.TransaccionDao;
import com.sabine.proyecto_final.activities.Database.AppDatabase;
import com.sabine.proyecto_final.activities.Models.Tarjeta;
import com.sabine.proyecto_final.activities.Models.Transaccion;

import java.util.ArrayList;
import java.util.List;

public class RegisterPaymentActivity extends AppCompatActivity {

    private TarjetaDao tarjetaDao;
    private TransaccionDao transaccionDao;

    private AppDatabase db;

    private Spinner cardSpinner;
    private EditText totalEditText, descriptionEditText;
    private Button registrarPagoButton;

    private List<Tarjeta> tarjetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_payment);
        db = AppDatabase.getInstance(getApplicationContext());

        // Inicializar los DAOs
        tarjetaDao = AppDatabase.getInstance(this).tarjetaDao();
        transaccionDao = AppDatabase.getInstance(this).transaccionDao();

        // Obtener referencias a las vistas
        cardSpinner = findViewById(R.id.card_spinner);
        totalEditText = findViewById(R.id.totalEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        registrarPagoButton = findViewById(R.id.buttonLogin);



        TextInputEditText datePickerEditText = findViewById(R.id.date_picker_edit_text);
        datePickerEditText.setOnClickListener(view -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Selecciona una fecha");
            final MaterialDatePicker<Long> materialDatePicker = builder.build();

            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            materialDatePicker.addOnPositiveButtonClickListener(selection -> datePickerEditText.setText(materialDatePicker.getHeaderText()));
        });


        // Obtener el ID del usuario logueado desde SharedPreferences
        int userId = getLoggedInUserId();

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
                        for (Tarjeta tarjeta : tarjetas) {
                            String cardnumber = tarjeta.getNumero().substring(15);
                            String text = tarjeta.getBanco() + " - "+ cardnumber;
                            cardNumbers.add(text);
                        }

                        // Crear un ArrayAdapter para el spinner y asignar la lista de números de tarjeta
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(RegisterPaymentActivity.this, android.R.layout.simple_spinner_item, cardNumbers);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cardSpinner.setAdapter(spinnerAdapter);
                        Log.d("Numeros", cardNumbers.get(0));
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
                    Tarjeta selectedCard = tarjetas.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Manejar el caso en el que no se haya seleccionado ninguna tarjeta
            }
        });

        // Establecer un listener para el botón de registrar pago
        registrarPagoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener la tarjeta seleccionada del spinner
                int selectedPosition = cardSpinner.getSelectedItemPosition();
                if (tarjetas != null && tarjetas.size() > selectedPosition) {
                    Tarjeta selectedCard = tarjetas.get(selectedPosition);

                    // Obtener el monto y las notas ingresadas
                    String total = totalEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String fecha = datePickerEditText.getText().toString();

                    // Crear una nueva transacción
                    Transaccion transaccion = new Transaccion();
                    transaccion.setTipo(false); // Pago (0) o compra (1)
                    transaccion.setMonto(Float.parseFloat(total));
                    transaccion.setDescripcion(description);
                    transaccion.setFecha(fecha);
                    transaccion.setIdTarjeta(selectedCard.getId());
                    transaccion.setIcono(String.valueOf(R.drawable.paymentcheck2));

                    // Insertar la transacción en la base de datos en un hilo separado
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            transaccionDao.insert(transaccion);

                            Intent intent = new Intent(getApplicationContext(), CreditCardActivity.class);
                            startActivity(intent);
                            finish();
                            // Mostrar mensaje de éxito en el hilo principal
                            //showSuccessMessage("Transacción registrada exitosamente");
                        }
                    });
                    thread.start();

                    // Reiniciar los campos de monto y notas
                    totalEditText.setText("");
                    descriptionEditText.setText("");
                }
            }
        });
    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getInt("loggedUserId", -1);
    }



    private void showSuccessMessage(String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterPaymentActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
