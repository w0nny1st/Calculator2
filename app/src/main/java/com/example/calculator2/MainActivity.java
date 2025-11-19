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
    private String previousValue = "";
    private String currentOperator = "";
    private boolean resetOnNextInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        display2 = findViewById(R.id.display2);

        if (savedInstanceState != null) {
            currentDisplayValue = savedInstanceState.getString("DISPLAY_VALUE", "0");
            previousValue = savedInstanceState.getString("PREVIOUS_VALUE", "");
            currentOperator = savedInstanceState.getString("CURRENT_OPERATOR", "");
            Log.d(TAG, "Восстановлено полное состояние калькулятора");
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
            clearAll();
        });

        findViewById(R.id.btn_add).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btn_subtract).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btn_multiply).setOnClickListener(v -> setOperator("*"));
        findViewById(R.id.btn_divide).setOnClickListener(v -> setOperator("/"));
        findViewById(R.id.btn_equals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btn_decimal).setOnClickListener(v -> addDecimalPoint());
    }

    private void addNumber(String number) {
        if (resetOnNextInput || currentDisplayValue.equals("0")) {
            currentDisplayValue = number;
            resetOnNextInput = false;
        } else {
            currentDisplayValue += number;
        }
        display2.setText(currentDisplayValue);
        Log.d(TAG, "Добавлена цифра: " + number + ", текущее значение: " + currentDisplayValue);
    }

    private void addDecimalPoint() {
        if (resetOnNextInput) {
            currentDisplayValue = "0.";
            resetOnNextInput = false;
        } else if (!currentDisplayValue.contains(".")) {
            currentDisplayValue += ".";
        }
        display2.setText(currentDisplayValue);
    }

    private void setOperator(String operator) {

        if (!previousValue.isEmpty() && !currentOperator.isEmpty() && !resetOnNextInput) {
            calculateResult();
        }

        previousValue = currentDisplayValue;
        currentOperator = operator;
        resetOnNextInput = true;
        Log.d(TAG, "Установлен оператор: " + operator);
    }

    private void calculateResult() {
        if (previousValue.isEmpty() || currentOperator.isEmpty()) {
            return;
        }

        try {
            double firstNum = Double.parseDouble(previousValue);
            double secondNum = Double.parseDouble(currentDisplayValue);
            double result = 0;

            switch (currentOperator) {
                case "+": result = firstNum + secondNum; break;
                case "-": result = firstNum - secondNum; break;
                case "*": result = firstNum * secondNum; break;
                case "/":
                    if (secondNum != 0) result = firstNum / secondNum;
                    else {
                        display2.setText("Ошибка");
                        clearAll();
                        return;
                    }
                    break;
            }

            if (result == (long) result) {
                currentDisplayValue = String.valueOf((long) result);
            } else {
                currentDisplayValue = String.valueOf(result);
                if (currentDisplayValue.length() > 10) {
                    currentDisplayValue = String.format("%.6f", result).replace(",", ".");
                }
            }

            display2.setText(currentDisplayValue);
            previousValue = currentDisplayValue;
            resetOnNextInput = true;

        } catch (Exception e) {
            display2.setText("Ошибка");
            clearAll();
        }
    }

    private void clearAll() {
        currentDisplayValue = "0";
        previousValue = "";
        currentOperator = "";
        resetOnNextInput = false;
        display2.setText(currentDisplayValue);
        Log.d(TAG, "Полный сброс калькулятора");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("DISPLAY_VALUE", currentDisplayValue);
        outState.putString("PREVIOUS_VALUE", previousValue);
        outState.putString("CURRENT_OPERATOR", currentOperator);
        Log.d(TAG, "Сохранено значение: " + currentDisplayValue);
        makeToast("Состояние сохранено");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentDisplayValue = savedInstanceState.getString("DISPLAY_VALUE", "0");
        previousValue = savedInstanceState.getString("PREVIOUS_VALUE", "");
        currentOperator = savedInstanceState.getString("CURRENT_OPERATOR", "");
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
}



