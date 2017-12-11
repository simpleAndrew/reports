package org.challenge.test.report;

import org.challenge.test.data.Employee;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class AverageIncomeByAgeReportBuilder implements EmployeeReportBuilder<Integer, Double> {

    private static final int AGE_BUCKET_STEP = 10;

    private final Function<Employee, Integer> ageProvider;

    public AverageIncomeByAgeReportBuilder(Function<Employee, Integer> ageProvider) {
        this.ageProvider = ageProvider;
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

    /*
     * Assigns bucket index that holds the age of employee.
     * Splits it in a way that [0-10) goes to 1st bucket,[10-20) goes to 2nd, [20-30) - to 3rd, etc.
     */
    private Integer extractAgeBucket(Employee employee) {
        return 1 + ageProvider.apply(employee) / AGE_BUCKET_STEP;
    }
}
