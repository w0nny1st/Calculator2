package com.example.calculator2;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView display2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        display2 = findViewById(R.id.display2);
        display2.setText("0");

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

        findViewById(R.id.btn_clear).setOnClickListener(v -> display2.setText("0"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        String instanceState;
        if (savedInstanceState == null) {
            instanceState = "Первый запуск!";
        } else {
            instanceState = "Повторный запуск!";
        }
        //Toast.makeText(this, instanceState + " - onCreate()", Toast.LENGTH_SHORT).show();
        makeToast(instanceState + " - onCreate()");
    }
    private void makeToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }

    private void addNumber(String number) {
        String current = display2.getText().toString();
        if (current.equals("0")) {
            display2.setText(number);
        } else {
            display2.setText(current + number);
        }

    }
}
