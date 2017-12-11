package org.challenge.test.utils;

import org.challenge.test.data.Employee;

public class EmployeeBuilder {

    public static final String SEX = "any";
    private double currentSalary = 0.0;
    private int currentDepartment;

    public EmployeeBuilder fromDepartment(int newDepartment) {
        currentDepartment = newDepartment;
        return this;
    }


    public EmployeeBuilder earning(double newSalary) {
        currentSalary = newSalary;
        return this;
    }

    public Employee called(String individualName) {
        return new Employee(currentDepartment, individualName, SEX, currentSalary);
    }

}
