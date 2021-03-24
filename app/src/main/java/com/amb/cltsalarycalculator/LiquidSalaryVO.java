package com.amb.cltsalarycalculator;

import java.io.Serializable;

public class LiquidSalaryVO implements Serializable {
    Double rawSalary;
    Double liquidSalary;
    Double inss;
    Double irrf;
    Double discounts;
    Double discountsPercent;

    public Double getRawSalary() {
        return rawSalary;
    }

    public void setRawSalary(Double rawSalary) {
        this.rawSalary = rawSalary;
    }

    public void setLiquidSalary(Double liquidSalary) {
        this.liquidSalary = liquidSalary;
    }

    public Double getInss() {
        return inss;
    }

    public void setInss(Double inss) {
        this.inss = inss;
    }

    public Double getIrrf() {
        return irrf;
    }

    public void setIrrf(Double irrf) {
        this.irrf = irrf;
    }

    public Double getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Double discounts) {
        this.discounts = discounts;
    }

    public Double getDiscountsPercent() {
        return discountsPercent;
    }

    public void setDiscountsPercent(Double discountsPercent) {
        this.discountsPercent = discountsPercent;
    }

    public Double calculateLiquidSalary() {
        return rawSalary - inss - irrf - discounts;
    }
}
