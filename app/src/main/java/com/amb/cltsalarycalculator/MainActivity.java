package com.amb.cltsalarycalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private Button calculateButton;
    private EditText inputSalary;
    private EditText inputDependentsNumber;
    private EditText inputDiscounts;

    private SalaryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculateButton = findViewById(R.id.btCalculateSalary);
        inputSalary = findViewById(R.id.etRawSalary);
        inputDependentsNumber = findViewById(R.id.etDependentsNumber);
        inputDiscounts = findViewById(R.id.etOtherDiscounts);
        viewModel = new ViewModelProvider(this).get(SalaryViewModel.class);

        setViewListeners();
        setupObservers();
    }

    private void setViewListeners() {
        inputSalary.addTextChangedListener(new CurrencyMask(inputSalary));
        inputDiscounts.addTextChangedListener(new CurrencyMask(inputDiscounts));

        calculateButton.setOnClickListener(v -> {
            viewModel.validateAndCalculateLiquidSalary(
                    inputSalary.getText().toString(),
                    inputDependentsNumber.getText().toString(),
                    inputDiscounts.getText().toString()
            );
        });
    }

    private void setupObservers() {
        viewModel.getSalaryErrorLiveData().observe(this, value -> {
            if (value) {
                inputSalary.requestFocus();
                Toast.makeText(MainActivity.this, "Salário não pode estar vazio", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
