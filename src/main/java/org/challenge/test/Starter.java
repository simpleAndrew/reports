package org.challenge.test;

import org.challenge.test.data.Employee;
import org.challenge.test.fs.EmployeeDataHolder;
import org.challenge.test.report.AverageIncomeByAgeReportBuilder;
import org.challenge.test.report.MedianAgeByDepartmentReportBuilder;
import org.challenge.test.report.PercentileIncomeReportBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class Starter {

    public static final int UNKNOWN_AGE = 0;
    public static final String UNKNOWN_DEPARTMENT = "Unknown Department";

    public static void main(String[] args) throws IOException {
        assert args.length > 0;

        String givenRootDirectoryPath = args[0];

        Path rootPath = Paths.get(givenRootDirectoryPath);

        DataHoldersFactory dataHoldersFactory = new DataHoldersFactory(rootPath);

        ReportWritersFactory reportWritersFactory = new ReportWritersFactory(
                rootPath,
                deptId -> getDepartmentsName(dataHoldersFactory, deptId)
        );

        Function<Employee, Integer> ageProvider = employee -> getEmployeesAge(dataHoldersFactory, employee);

        EmployeeDataHolder employeeDataHolder = dataHoldersFactory.getEmployeeHolder();

        reportWritersFactory.getMedianAgeByDepartmentWriter()
                .writeReport(
                        new MedianAgeByDepartmentReportBuilder(ageProvider).buildReport(employeeDataHolder.getEmployeeStream())
                );

        reportWritersFactory.getMedianIncomeByDepartmentWriter()
                .writeReport(
                        PercentileIncomeReportBuilder.ofMedian().buildReport(employeeDataHolder.getEmployeeStream())
                );

        reportWritersFactory.get95thPercentileIncomeByDepartmentWriter()
                .writeReport(
                        PercentileIncomeReportBuilder.ofPercentile(0.95).buildReport(employeeDataHolder.getEmployeeStream())
                );

        reportWritersFactory.getIncomeAverageByAge()
                .writeReport(
                        new AverageIncomeByAgeReportBuilder(ageProvider).buildReport(employeeDataHolder.getEmployeeStream())
                );
    }

    private static Integer getEmployeesAge(DataHoldersFactory dataHoldersFactory, Employee employee) {
        return dataHoldersFactory.getAgeHolder().lookForAge(employee.getName()).orElse(UNKNOWN_AGE);
    }

    private static String getDepartmentsName(DataHoldersFactory dataHoldersFactory, Integer deptId) {
        return dataHoldersFactory.getDepartmentHolder().lookForDepartment(deptId).orElse(UNKNOWN_DEPARTMENT);
    }

}
