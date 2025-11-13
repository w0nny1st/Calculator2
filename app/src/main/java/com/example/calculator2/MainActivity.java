package com.example.calculator2;

import static android.content.ContentValues.TAG;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView display2;
    private String currentDisplayValue = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        display2 = findViewById(R.id.display2);

        if (savedInstanceState != null) {
            currentDisplayValue = savedInstanceState.getString("DISPLAY_VALUE", "0");
            Log.d(TAG, "Восстановлено значение: " + currentDisplayValue);
        }

        display2.setText(currentDisplayValue);

        setupAllButtons();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int orientation = getResources().getConfiguration().orientation;
        String orientationText = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? "ЛАНДШАФТ" : "ПОРТРЕТ";
        Log.d(TAG, "Текущая ориентация: " + orientationText);
        makeToast("Запуск - " + orientationText);
    }

    private void setupAllButtons() {
        findViewById(R.id.btn_0).setOnClickListener(v -> addNumber("0"));
        findViewById(R.id.btn_1).setOnClickListener(v -> addNumber("1"));
        findViewById(R.id.btn_2).setOnClickListener(v -> addNumber("2"));
        findViewById(R.id.btn_3).setOnClickListener(v -> addNumber("3"));
        findViewById(R.id.btn_4).setOnClickListener(v -> addNumber("4"));
        findViewById(R.id.btn_5).setOnClickListener(v -> addNumber("5"));
        findViewById(R.id.btn_6).setOnClickListener(v -> addNumber("6"));
        findViewById(R.id.btn_7).setOnClickListener(v -> addNumber("7"));
        findViewById(R.id.btn_8).setOnClickListener(v -> addNumber("8"));
        findViewById(R.id.btn_9).setOnClickListener(v -> addNumber("9"));

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            currentDisplayValue = "0";
            display2.setText(currentDisplayValue);
            Log.d(TAG, "Экран очищен");
        });

        int[] otherButtonIds = {
                R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide,
                R.id.btn_equals, R.id.btn_decimal, R.id.btn_delete
        };

        for (int buttonId : otherButtonIds) {
            findViewById(buttonId).setOnClickListener(v -> {
                String buttonText = ((Button) v).getText().toString();
                Log.d(TAG, "Нажата кнопка: " + buttonText);
                makeToast("Кнопка " + buttonText + " нажата");
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("DISPLAY_VALUE", currentDisplayValue);
        Log.d(TAG, "Сохранено значение: " + currentDisplayValue);
        makeToast("Состояние сохранено");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentDisplayValue = savedInstanceState.getString("DISPLAY_VALUE", "0");
        display2.setText(currentDisplayValue);
        Log.d(TAG, "Восстановлено после поворота: " + currentDisplayValue);
        makeToast("Состояние восстановлено");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String orientationText = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) ? "ЛАНДШАФТ" : "ПОРТРЕТ";
        Log.d(TAG, "Ориентация изменена на: " + orientationText);
        makeToast("Ориентация: " + orientationText);
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }

    private void addNumber(String number) {
        if (currentDisplayValue.equals("0")) {
            currentDisplayValue = number;
        } else {
            currentDisplayValue += number;
        }
        display2.setText(currentDisplayValue);
        Log.d(TAG, "Добавлена цифра: " + number + ", текущее значение: " + currentDisplayValue);
    }
}