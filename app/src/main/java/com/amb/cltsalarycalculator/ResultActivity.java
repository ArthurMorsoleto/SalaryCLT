package com.amb.cltsalarycalculator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.DecimalFormat;

import static com.amb.cltsalarycalculator.MainActivity.SALARY_RESULT;

public class ResultActivity extends AppCompatActivity {

    public TextView rawSalary;
    public TextView inssText;
    public TextView irffText;
    public TextView otherDiscountsText;
    public TextView liquidSalaryText;
    public TextView percentDiscountsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        rawSalary = findViewById(R.id.tvRawSalaryResult);
        inssText = findViewById(R.id.tvInssResult);
        irffText = findViewById(R.id.tvIrffResult);
        otherDiscountsText = findViewById(R.id.tvOtherDiscountsResult);
        liquidSalaryText = findViewById(R.id.tvLiquidSalaryResult);
        percentDiscountsText = findViewById(R.id.tvPercentResult);

        Serializable salaryResult = getIntent().getSerializableExtra(SALARY_RESULT);
        if (salaryResult != null) {
            initViews((LiquidSalaryVO) salaryResult);
        }
    }

    private void initViews(LiquidSalaryVO salaryResult) {
        rawSalary.setText(formatValue(salaryResult.rawSalary));
        inssText.setText(formatValue(salaryResult.inss));
        irffText.setText(formatValue(salaryResult.irrf));
        otherDiscountsText.setText(formatValue(salaryResult.discounts));
        liquidSalaryText.setText(formatValue(salaryResult.liquidSalary));
        percentDiscountsText.setText(String.format("%s%%", formatValue(salaryResult.discountsPercent)));
    }

    private String formatValue(double value) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(value);
    }
}
