package org.challenge.test.report.data;

import org.challenge.test.data.Employee;
import org.challenge.test.utils.EmployeeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SalaryEmployeeTestBean {

    public static SalaryEmployeeTestBean buildFrom(Stream<String[]> loadedValues) {
        final SalaryEmployeeTestBean SalaryEmployeeTestBean = new SalaryEmployeeTestBean();
        final EmployeeBuilder builder = new EmployeeBuilder();

        loadedValues.forEach(line -> {
            int department = Integer.parseInt(line[0]);
            String name = line[1];
            double salary = Double.parseDouble(line[2]);
            SalaryEmployeeTestBean.employees.add(builder
                    .fromDepartment(department)
                    .earning(salary)
                    .called(name));
        });

        return SalaryEmployeeTestBean;
    }

    private List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees() {
        return employees;
    }
}
