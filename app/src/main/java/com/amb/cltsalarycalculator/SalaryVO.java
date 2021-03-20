package com.amb.cltsalarycalculator;

public class SalaryVO {
     Double rawSalary;
     Integer dependents;
     Double discounts;

    public SalaryVO(Double rawSalary, Integer dependents, Double discounts) {
        this.rawSalary = rawSalary;
        this.dependents = dependents;
        this.discounts = discounts;
    }
}
