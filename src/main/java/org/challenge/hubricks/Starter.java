package org.challenge.hubricks;

import org.challenge.hubricks.dao.AgesCsvDao;
import org.challenge.hubricks.dao.DepartmentCsvDao;
import org.challenge.hubricks.data.Employee;
import org.challenge.hubricks.dao.EmployeeCsvDao;
import org.challenge.hubricks.statistics.Statistics;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class Starter {

    public static void main(String[] args) throws IOException {
        assert args.length > 0;

        String givenRootDirectoryPath = args[0];

        Path rootPath = Paths.get(givenRootDirectoryPath);

        Path departmentsPath = rootPath.resolve("departments.csv");
        Path employeesPath = rootPath.resolve("employees.csv");
        Path agesPath = rootPath.resolve("ages.csv");

        DepartmentCsvDao departmentCsvDao = DepartmentCsvDao.buildDao(departmentsPath);
        EmployeeCsvDao employeeCsvDao = EmployeeCsvDao.buildDao(employeesPath);
        AgesCsvDao agesCsvDao = AgesCsvDao.buildDao(employeesPath);

        buildIncomeByDepartment(employeeCsvDao);
        buildSalaryPercentileByDepartment(employeeCsvDao);
        buildAverageSalaryByAge(employeeCsvDao, agesCsvDao);
//        buildMedianAgeByDepartment(employeeCsvDao, agesCsvDao);
    }



    private static Map<Integer, Double> buildAverageSalaryByAge(EmployeeCsvDao employeeCsvDao, AgesCsvDao agesCsvDao) {
        return employeeCsvDao.getEmployeeStream()
                .collect(
                        groupingBy(
                                employee -> {
                                    int age = agesCsvDao.getAge(employee.getName()).orElse(-1);
                                    return age / 10;
                                },
                                Collectors.averagingDouble(Employee::getSalary)
                        )
                );
    }

    private static Map<Integer, Double> buildSalaryPercentileByDepartment(EmployeeCsvDao employeeCsvDao) {
        Function<List<Double>, Double> calculateMedian = empl -> Statistics.ofDoubles().calculatePercentile(empl, 0.95);
        return employeeCsvDao.getEmployeeStream()
                .collect(
                        groupingBy(
                                Employee::getDepartmentId,
                                collectingAndThen(
                                        mapping(Employee::getSalary, toList()),
                                        calculateMedian
                                )
                        )
                );
    }

    private static Map<Integer, Double> buildIncomeByDepartment(EmployeeCsvDao employeeCsvDao) throws IOException {
        Statistics<Double> statistics = Statistics.ofDoubles();
        return employeeCsvDao.getEmployeeStream()
                .collect(
                        groupingBy(
                                Employee::getDepartmentId,
                                collectingAndThen(
                                        mapping(Employee::getSalary, toList()),
                                        statistics::calculateMedian
                                )
                        )
                );

    }
}
