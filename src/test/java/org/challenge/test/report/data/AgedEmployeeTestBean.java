package org.challenge.test.report.data;

import org.challenge.test.data.Employee;
import org.challenge.test.utils.EmployeeBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class AgedEmployeeTestBean {

    public static AgedEmployeeTestBean buildFrom(Stream<String[]> loadedValues) {
        final Map<String, Integer> ageByEmployee = new HashMap<>();
        final AgedEmployeeTestBean agedEmployeeTestBean = new AgedEmployeeTestBean();
        final EmployeeBuilder builder = new EmployeeBuilder();

        loadedValues.forEach(line -> {
            int department = Integer.parseInt(line[0]);
            String name = line[1];
            int age = Integer.parseInt(line[2]);
            double salary = line.length > 3 ? Double.parseDouble(line[3]) : 0.0;

            ageByEmployee.put(department + name, age);
            agedEmployeeTestBean.employees.add(builder
                    .fromDepartment(department)
                    .earning(salary)
                    .called(name));
        });

        agedEmployeeTestBean.ageProducer = e -> ageByEmployee.getOrDefault(e.getDepartmentId() + e.getName(), 0);
        return agedEmployeeTestBean;
    }

    private List<Employee> employees = new ArrayList<>();
    private Function<Employee, Integer> ageProducer;

    public List<Employee> getEmployees() {
        return employees;
    }

    public Function<Employee, Integer> getAgeProducer() {
        return ageProducer;
    }
}
