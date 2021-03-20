package com.amb.cltsalarycalculator;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class SalaryViewModelTest {

    public static final double DELTA = 1e-15;
    private SalaryViewModel subject;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        subject = new SalaryViewModel();
    }

    @Test
    public void validateSalary_EmptyValue_SalaryValueError() {
        subject.validateSalary("", "", "");
        assertEquals(true, subject.getSalaryErrorLiveData().getValue());
    }

    @Test
    public void validateSalary_CompleteValue() {
        SalaryVO result = subject.validateSalary("90,000.00", "", "");

        assertEquals(90000.0, result.rawSalary, DELTA);
        assertEquals(0, result.dependents, DELTA);
        assertEquals(0.0, result.discounts, DELTA);
        Assert.assertNull(subject.getSalaryErrorLiveData().getValue());
    }

    @Test
    public void validateSalary_Dependents2() {
        SalaryVO result = subject.validateSalary("9000.00", "2", "");

        assertEquals(9000.0, result.rawSalary, DELTA);
        assertEquals(2, result.dependents, DELTA);
        assertEquals(0.0, result.discounts, DELTA);
    }

    @Test
    public void validateSalary_Discounts200() {
        SalaryVO result = subject.validateSalary("9000.00", "2", "200.00");
        assertEquals(9000.0, result.rawSalary, DELTA);
        assertEquals(2, result.dependents, DELTA);
        assertEquals(200.0, result.discounts, DELTA);
    }

    @Test
    public void calculateInss_FirstRange() {
        double inss = subject.calculateINSS(1045.0);
        assertEquals(78.375, inss, DELTA);
    }

    @Test
    public void calculateInss_SecondRange() {
        double inss = subject.calculateINSS(1900.0);
        assertEquals(155.33, inss, DELTA);
    }

    @Test
    public void calculateInss_ThirdRange() {
        double inss = subject.calculateINSS(3000.0);
        assertEquals(281.64, inss, DELTA);
    }

    @Test
    public void calculateInss_ForthRange() {
        double inss = subject.calculateINSS(5000.0);
        assertEquals(558.95, inss, DELTA);
    }

    @Test
    public void calculateInss_FifthRange() {
        double inss = subject.calculateINSS(7000.0);
        assertEquals(713.1, inss, DELTA);
    }

    @Test
    public void calculateIrrf_ThirdRangeNoDependents() {
        double irrf = subject.calculateIRRF(3000.0, 0);
        assertEquals(61.08, irrf, DELTA);
    }
    //TODO REST of ranges
}