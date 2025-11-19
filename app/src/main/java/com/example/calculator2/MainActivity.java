package com.example.calculator2;

import static android.content.ContentValues.TAG;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.activity.OnBackPressedCallback;

public class MainActivity extends AppCompatActivity {

    private TextView display2;
    private String currentDisplayValue = "0";
    private String previousValue = "";
    private String currentOperator = "";
    private boolean resetOnNextInput = false;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_with_drawer);

        initNavigationDrawer();

        setupBackPressedCallback();

        EdgeToEdge.enable(this);

        display2 = findViewById(R.id.display2);

        if (savedInstanceState != null) {
            currentDisplayValue = savedInstanceState.getString("DISPLAY_VALUE", "0");
            previousValue = savedInstanceState.getString("PREVIOUS_VALUE", "");
            currentOperator = savedInstanceState.getString("CURRENT_OPERATOR", "");
            Log.d(TAG, "Восстановлено полное состояние калькулятора");
        }

        display2.setText(currentDisplayValue);
        setupAllButtons();

        int orientation = getResources().getConfiguration().orientation;
        String orientationText = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? "ЛАНДШАФТ" : "ПОРТРЕТ";
        Log.d(TAG, "Текущая ориентация: " + orientationText);
        makeToast("Запуск - " + orientationText);
    }

    private void setupBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    MainActivity.super.onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void initNavigationDrawer() {
        try {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);
            }

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.nav_calculator) {
                        makeToast("Калькулятор");
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else if (id == R.id.nav_about) {
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    return true;
                }
            });

            makeToast("Навигационное меню готово");

        } catch (Exception e) {
            Log.e(TAG, "Ошибка инициализации навигационного меню", e);
            makeToast("Ошибка меню");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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