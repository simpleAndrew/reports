package org.challenge.hubricks.report;

import org.challenge.hubricks.data.Employee;
import org.challenge.hubricks.statistics.Statistics;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class PercentileIncomeReportBuilder implements EmployeeReportBuilder<Integer, Double> {

    public static final Statistics<Double> STATS = Statistics.ofDoubles();


    public static PercentileIncomeReportBuilder ofMedian() {
        return new PercentileIncomeReportBuilder(0.5);
    }

    public static PercentileIncomeReportBuilder ofPercentile(double percent) {
        return new PercentileIncomeReportBuilder(percent);
    }

    private final double percent;

    public PercentileIncomeReportBuilder(double percent) {
        this.percent = percent;
    }

    @Override
    public Map<Integer, Double> buildReport(Stream<Employee> employeeStream) {

        return employeeStream
                .collect(
                        groupingBy(
                                Employee::getDepartmentId,
                                collectingAndThen(
                                        mapping(Employee::getSalary, toList()),
                                        this::percentileCalculator
                                )
                        )
                );
    }

    private Double percentileCalculator(List<Double> employees) {
        return STATS.calculatePercentile(employees, percent);
    }
}
