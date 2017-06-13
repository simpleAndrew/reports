package org.challenge.hubricks.report;

import org.challenge.hubricks.data.Employee;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class AverageAgeBuilder implements EmployeeReportBuilder<Double> {

    private final Function<Employee, Integer> ageProvider;

    public AverageAgeBuilder(Function<Employee, Integer> ageProvider) {
        this.ageProvider = ageProvider;
    }

    public Map<Integer, Double> buildReport(Stream<Employee> employeeStream) {
        return employeeStream
                .collect(
                        groupingBy(
                                Employee::getDepartmentId,
                                Collectors.averagingInt(ageProvider::apply)
                        )
                );
    }
}
