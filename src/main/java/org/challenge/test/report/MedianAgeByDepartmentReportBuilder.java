package org.challenge.test.report;

import org.challenge.test.data.Employee;
import org.challenge.test.statistics.Statistics;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class MedianAgeByDepartmentReportBuilder implements EmployeeReportBuilder<Integer, Integer> {

    private static final Statistics<Integer> STATS = Statistics.ofIntegers();

    private final Function<Employee, Integer> ageExtractor;

    public MedianAgeByDepartmentReportBuilder(Function<Employee, Integer> ageProvider) {
        this.ageExtractor = ageProvider;
    }

    @Override
    public Map<Integer, Integer> buildReport(Stream<Employee> employeeStream) {

        return employeeStream
                .collect(
                        groupingBy(
                                Employee::getDepartmentId,
                                collectingAndThen(
                                        mapping(ageExtractor, toList()),
                                        this::calculateMedian
                                )
                        )
                );
    }

    private Integer calculateMedian(List<Integer> ages) {
        return STATS.calculatePercentile(ages,  0.5);
    }
}
