package com.amb.cltsalarycalculator;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static com.amb.cltsalarycalculator.Constants.DEPENDENTS_MULTIPLIER;
import static com.amb.cltsalarycalculator.Constants.FIRST_ALIQUOT;
import static com.amb.cltsalarycalculator.Constants.FIRST_ALIQUOT_IRFF;
import static com.amb.cltsalarycalculator.Constants.FIRST_DEDUCTION_IRFF;
import static com.amb.cltsalarycalculator.Constants.FIRST_RANGE_MAX_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.FIRST_RANGE_MAX_SALARY;
import static com.amb.cltsalarycalculator.Constants.FIRST_RANGE_MIN_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.FIRST_RANGE_MIN_SALARY;
import static com.amb.cltsalarycalculator.Constants.FORTH_ALIQUOT;
import static com.amb.cltsalarycalculator.Constants.FORTH_ALIQUOT_IRFF;
import static com.amb.cltsalarycalculator.Constants.FORTH_DEDUCTION;
import static com.amb.cltsalarycalculator.Constants.FORTH_DEDUCTION_IRFF;
import static com.amb.cltsalarycalculator.Constants.MAX_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.MAX_DEDUCTION;
import static com.amb.cltsalarycalculator.Constants.MIN_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.MIN_SALARY;
import static com.amb.cltsalarycalculator.Constants.ONE_HUNDRED_PERCENT;
import static com.amb.cltsalarycalculator.Constants.SECOND_ALIQUOT;
import static com.amb.cltsalarycalculator.Constants.SECOND_ALIQUOT_IRFF;
import static com.amb.cltsalarycalculator.Constants.SECOND_DEDUCTION;
import static com.amb.cltsalarycalculator.Constants.SECOND_DEDUCTON_IRFF;
import static com.amb.cltsalarycalculator.Constants.SECOND_RANGE_MAX_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.SECOND_RANGE_MAX_SALARY;
import static com.amb.cltsalarycalculator.Constants.SECOND_RANGE_MIN_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.SECOND_RANGE_MIN_SALARY;
import static com.amb.cltsalarycalculator.Constants.THIRD_ALIQUOT;
import static com.amb.cltsalarycalculator.Constants.THIRD_ALIQUOT_IRFF;
import static com.amb.cltsalarycalculator.Constants.THIRD_DEDUCTION;
import static com.amb.cltsalarycalculator.Constants.THIRD_DEDUCTION_IRFF;
import static com.amb.cltsalarycalculator.Constants.THIRD_RANGE_MAX_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.THIRD_RANGE_MAX_SALARY;
import static com.amb.cltsalarycalculator.Constants.THIRD_RANGE_MIN_BASE_IRFF;
import static com.amb.cltsalarycalculator.Constants.THIRD_RANGE_MIN_SALARY;

public class SalaryViewModel extends ViewModel {

    private final MutableLiveData<Boolean> salaryErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<LiquidSalaryVO> liquidSalaryLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> getSalaryErrorLiveData() {
        return salaryErrorLiveData;
    }

    public final MutableLiveData<LiquidSalaryVO> getLiquidSalaryLiveData() {
        return liquidSalaryLiveData;
    }

    public void validateAndCalculateLiquidSalary(String rawSalary, String dependents, String discounts) {
        double salaryValue = 0.0;
        double discountsValue = 0.0;
        int dependentsNumber = 0;

        if (rawSalary.isEmpty() || rawSalary.equals("$0.00")) {
            salaryErrorLiveData.postValue(true);
        } else {
            salaryValue = getValue(rawSalary);
        }

        if (!dependents.isEmpty()) {
            dependentsNumber = Integer.parseInt(dependents);
        }

        if (!discounts.isEmpty()) {
            discountsValue = getValue(discounts);
        }

        LiquidSalaryVO liquidSalaryVO = new LiquidSalaryVO();
        liquidSalaryVO.rawSalary = salaryValue;
        liquidSalaryVO.inss = calculateINSS(salaryValue);
        liquidSalaryVO.irrf = calculateIRRF(salaryValue, dependentsNumber);
        liquidSalaryVO.discounts = discountsValue;
        liquidSalaryVO.liquidSalary = liquidSalaryVO.calculateLiquidSalary();

        double totalDiscounts = liquidSalaryVO.discounts + liquidSalaryVO.irrf + liquidSalaryVO.inss;
        liquidSalaryVO.discountsPercent = totalDiscounts * 100 / salaryValue;

        liquidSalaryLiveData.postValue(liquidSalaryVO);
    }

    private double getValue(String inputText) {
        String valueText = inputText.replace("$", "").replace(",", "");
        if (valueText.isEmpty()) {
            return 0.0;
        } else {
            return Double.parseDouble(valueText);
        }
    }

    public double calculateINSS(double rawSalary) {
        if (rawSalary > THIRD_RANGE_MAX_SALARY) {
            return MAX_DEDUCTION;
        }
        double inss = 0;
        try {
            Pair<Double, Double> pair = aliquotAndDeductionValuesForINSS(rawSalary);
            inss = rawSalary * pair.first / ONE_HUNDRED_PERCENT - pair.second;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inss;
    }

    private Pair<Double, Double> aliquotAndDeductionValuesForINSS(double rawSalary) throws Exception {
        if (rawSalary <= MIN_SALARY) {
            return new Pair<>(FIRST_ALIQUOT, 0.0);
        } else if (rawSalary >= FIRST_RANGE_MIN_SALARY && rawSalary <= FIRST_RANGE_MAX_SALARY) {
            return new Pair<>(SECOND_ALIQUOT, SECOND_DEDUCTION);
        } else if (rawSalary >= SECOND_RANGE_MIN_SALARY && rawSalary <= SECOND_RANGE_MAX_SALARY) {
            return new Pair<>(THIRD_ALIQUOT, THIRD_DEDUCTION);
        } else if (rawSalary >= THIRD_RANGE_MIN_SALARY && rawSalary <= THIRD_RANGE_MAX_SALARY) {
            return new Pair<>(FORTH_ALIQUOT, FORTH_DEDUCTION);
        } else {
            throw new Exception("Invalid Salary");
        }
    }

    public double calculateIRRF(double rawSalary, int dependents) {
        double baseIRRF = rawSalary - calculateINSS(rawSalary) - dependents * DEPENDENTS_MULTIPLIER;
        double irrf = 0;
        try {
            Pair<Double, Double> pair = aliquotAndDeductionValuesForIRRF(baseIRRF);
            irrf = baseIRRF * pair.first / ONE_HUNDRED_PERCENT - pair.second;
        } catch (Exception e) {
            e.printStackTrace();
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        return Double.parseDouble(df.format(irrf).replace(",", "."));
    }

    private Pair<Double, Double> aliquotAndDeductionValuesForIRRF(double baseIRRF) throws Exception {
        if (baseIRRF <= MIN_BASE_IRFF) {
            return new Pair<>(0.0, 0.0);
        } else if (baseIRRF >= FIRST_RANGE_MIN_BASE_IRFF && baseIRRF <= FIRST_RANGE_MAX_BASE_IRFF) {
            return new Pair<>(FIRST_ALIQUOT_IRFF, FIRST_DEDUCTION_IRFF);
        } else if (baseIRRF >= SECOND_RANGE_MIN_BASE_IRFF && baseIRRF <= SECOND_RANGE_MAX_BASE_IRFF) {
            return new Pair<>(SECOND_ALIQUOT_IRFF, SECOND_DEDUCTON_IRFF);
        } else if (baseIRRF >= THIRD_RANGE_MIN_BASE_IRFF && baseIRRF <= THIRD_RANGE_MAX_BASE_IRFF) {
            return new Pair<>(THIRD_ALIQUOT_IRFF, THIRD_DEDUCTION_IRFF);
        } else if (baseIRRF > MAX_BASE_IRFF) {
            return new Pair<>(FORTH_ALIQUOT_IRFF, FORTH_DEDUCTION_IRFF);
        } else {
            throw new Exception("Invalid Base IRRF");
        }
    }

    //region INSS
    /*
       INSS

       Até um salário mínimo (R$ 1.045,00)     7,5%    -
       De R$ 1.045,01 até 2.089,60             9%      R$ 15,67
       De R$ 2.089,61 até 3.134,40             12%     R$ 78,36
       De R$ 3.134,41 até 6.101,06             14%     R$ 141,05
       Acima de R$6.101,06.                            R$ 713,10

       inss = rawSalary * aliquota - deducao
    */
    //endregion

    //region IRFF
    /*
        IRRF

        Base de cálculo = salário bruto – contribuição para o INSS – número de dependentes x 189,59

        Até R$ 1.903,98         0       -
        De R$ 1.903,99 até R$ 2.826,65  7,5%    R$ 142,80
        De R$ 2.826,66 até R$ 3.751,05  15,0%   R$ 354,80
        De R$ 3.751,06 até R$ 4.664,68  22,5%   R$ 636,13
        Acima de R$ 4.664,68            27,5%   R$ 869,36
     */
    //endregion
}
