package org.challenge.hubricks;

import org.challenge.hubricks.data.Employee;
import org.challenge.hubricks.fs.AgeDataHolder;
import org.challenge.hubricks.fs.csv.CsvReportWriter;
import org.challenge.hubricks.fs.DepartmentDataHolder;
import org.challenge.hubricks.fs.EmployeeDataHolder;
import org.challenge.hubricks.report.AverageIncomeByAgeReportBuilder;
import org.challenge.hubricks.report.MedianAgeByDepartmentReportBuilder;
import org.challenge.hubricks.report.PercentileIncomeReportBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Starter {

    public static final String DEPARTMENTS_FILE_NAME = "departments.csv";
    public static final String EMPLOYEES_FILE_NAME = "employees.csv";
    public static final String AGES_FILE_NAME = "ages.csv";
    public static final String EMPLOYEE_AGE_BY_DEPARTMENT_REPORT_FILE_NAME = "employee-age-by-department.csv";
    public static final String INCOME_BY_DEPARTMENT_REPORT_FILE_NAME = "income-by-department.csv";
    public static final String INCOME_95_BY_DEPARTMENT_REPORT_FILE_NAME = "income-95-by-department.csv";
    public static final String INCOME_AVERAGE_BY_AGE_RANGE_REPORT_FILE_NAME = "income-average-by-age-range.csv";

    public static void main(String[] args) throws IOException {
        assert args.length > 0;

        String givenRootDirectoryPath = args[0];

        Path rootPath = Paths.get(givenRootDirectoryPath);

        Path departmentsPath = rootPath.resolve(DEPARTMENTS_FILE_NAME);
        Path employeesPath = rootPath.resolve(EMPLOYEES_FILE_NAME);
        Path agesPath = rootPath.resolve(AGES_FILE_NAME);

        DepartmentDataHolder departmentDataHolder = DepartmentDataHolder.buildDao(departmentsPath);
        EmployeeDataHolder employeeDataHolder = EmployeeDataHolder.buildDao(employeesPath);
        AgeDataHolder ageDataHolder = AgeDataHolder.buildDao(agesPath);

        Function<Employee, Integer> ageProvider = employee -> ageDataHolder.lookForAge(employee.getName()).orElse(0);
        Function<Integer, String> getDepartmentByIndex = deptId -> departmentDataHolder.lookForDepartment(deptId).orElse("Unknown Department");

        AverageIncomeByAgeReportBuilder incomeByAgeBuilder =
                new AverageIncomeByAgeReportBuilder(ageProvider, 10);

        MedianAgeByDepartmentReportBuilder medianAgeBuilder =
                new MedianAgeByDepartmentReportBuilder(ageProvider);

        PercentileIncomeReportBuilder medianIncomeBuilder =
                PercentileIncomeReportBuilder.ofMedian();

        PercentileIncomeReportBuilder percentileIncomeBuilder =
                PercentileIncomeReportBuilder.ofPercentile(0.95);

        CsvReportWriterBuilder csvReportWriterBuilder =
                new CsvReportWriterBuilder()
                        .withReportsRoot(rootPath)
                        .withFirstColumnResolvedAs(getDepartmentByIndex);

        csvReportWriterBuilder
                .withHeaders("Department", "Age Median")
                .toFile(EMPLOYEE_AGE_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofIntegers()
                .writeReport(medianAgeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));

        csvReportWriterBuilder
                .withHeaders("Department", "Median Income")
                .toFile(INCOME_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofDoubles()
                .writeReport(medianIncomeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));

        csvReportWriterBuilder
                .withHeaders("Department", "95th Percentile Income")
                .toFile(INCOME_95_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofDoubles()
                .writeReport(percentileIncomeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));

        csvReportWriterBuilder
                .withHeaders("Age Range", "Average Income")
                .withFirstColumnResolvedAs(ageBucket -> ageBucket - 1 + "0-" + ageBucket + "0")
                .toFile(INCOME_AVERAGE_BY_AGE_RANGE_REPORT_FILE_NAME)
                .ofDoubles()
                .writeReport(incomeByAgeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));
    }

    public static class CsvReportWriterBuilder {

        private static final String DOUBLES_FORMAT = "%s, %.4f";
        private static final String INTEGERS_FORMAT = "%s, %d";

        private List<String> headers;
        private Function<Integer, String> firstColumnResolver;
        private Path filePath;
        private Path reportsRoot;

        public CsvReportWriterBuilder withReportsRoot(Path rootPath) {
            this.reportsRoot = rootPath;
            return this;
        }

        public CsvReportWriterBuilder withFirstColumnResolvedAs(Function<Integer, String> resolver) {
            this.firstColumnResolver = resolver;
            return this;
        }

        public CsvReportWriterBuilder withHeaders(String... headers) {
            this.headers = Arrays.asList(headers);
            return this;
        }

        public CsvReportWriterBuilder toFile(String reportName) {
            this.filePath = reportsRoot.resolve(reportName);
            return this;
        }

        public CsvReportWriter ofDoubles() {
            return new CsvReportWriter(filePath, headers, firstColumnResolver, DOUBLES_FORMAT);
        }

        public CsvReportWriter ofIntegers() {
            return new CsvReportWriter(filePath, headers, firstColumnResolver, INTEGERS_FORMAT);
        }
    }

}
