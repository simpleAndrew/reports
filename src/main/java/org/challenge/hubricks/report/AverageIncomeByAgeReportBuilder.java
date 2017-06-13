package org.challenge.hubricks.report;

import org.challenge.hubricks.data.Employee;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class AverageIncomeByAgeReportBuilder implements EmployeeReportBuilder<Double> {

    private final Function<Employee, Integer> ageProvider;
    private final int bucketSize;

    public AverageIncomeByAgeReportBuilder(Function<Employee, Integer> ageProvider, int bucketSize) {
        this.ageProvider = ageProvider;
        this.bucketSize = bucketSize;
    }

    @Override
    public Map<Integer, Double> buildReport(Stream<Employee> employeeStream) {
        return employeeStream
                .collect(
                        groupingBy(
                                this::extractAgeBucket,
                                Collectors.averagingDouble(Employee::getSalary)
                        )
                );
    }

    private Integer extractAgeBucket(Employee employee) {
        return 1 + ageProvider.apply(employee) / bucketSize;
    }
}
