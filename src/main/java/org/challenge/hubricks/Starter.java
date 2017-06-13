package org.challenge.hubricks;

import org.challenge.hubricks.data.Employee;
import org.challenge.hubricks.fs.EmployeeDataHolder;
import org.challenge.hubricks.fs.csv.CsvReportWriter;
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

    public static final String EMPLOYEE_AGE_BY_DEPARTMENT_REPORT_FILE_NAME = "employee-age-by-department.csv";
    public static final String INCOME_BY_DEPARTMENT_REPORT_FILE_NAME = "income-by-department.csv";
    public static final String INCOME_95_BY_DEPARTMENT_REPORT_FILE_NAME = "income-95-by-department.csv";
    public static final String INCOME_AVERAGE_BY_AGE_RANGE_REPORT_FILE_NAME = "income-average-by-age-range.csv";

    public static final int UNKNOWN_AGE = 0;
    public static final String UNKNOWN_DEPARTMENT = "Unknown Department";

    public static void main(String[] args) throws IOException {
        assert args.length > 0;

        String givenRootDirectoryPath = args[0];

        Path rootPath = Paths.get(givenRootDirectoryPath);

        DataHoldersFactory dataHoldersFactory = new DataHoldersFactory(rootPath);
        EmployeeDataHolder employeeDataHolder = dataHoldersFactory.getEmployeeHolder();

        Function<Employee, Integer> ageProvider = employee -> dataHoldersFactory.getAgeHolder().lookForAge(employee.getName()).orElse(UNKNOWN_AGE);
        Function<Integer, String> getDepartmentByIndex = deptId -> dataHoldersFactory.getDepartmentHolder().lookForDepartment(deptId).orElse(UNKNOWN_DEPARTMENT);


        CsvReportWriterBuilder csvReportWriterBuilder =
                new CsvReportWriterBuilder()
                        .withReportsRoot(rootPath)
                        .withFirstColumnResolvedAs(getDepartmentByIndex);

        MedianAgeByDepartmentReportBuilder medianAgeBuilder =
                new MedianAgeByDepartmentReportBuilder(ageProvider);
        csvReportWriterBuilder
                .withHeaders("Department", "Age Median")
                .toFile(EMPLOYEE_AGE_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofIntegers()
                .writeReport(medianAgeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));

        PercentileIncomeReportBuilder medianIncomeBuilder =
                PercentileIncomeReportBuilder.ofMedian();
        csvReportWriterBuilder
                .withHeaders("Department", "Median Income")
                .toFile(INCOME_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofDoubles()
                .writeReport(medianIncomeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));

        PercentileIncomeReportBuilder percentileIncomeBuilder =
                PercentileIncomeReportBuilder.ofPercentile(0.95);
        csvReportWriterBuilder
                .withHeaders("Department", "95th Percentile Income")
                .toFile(INCOME_95_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofDoubles()
                .writeReport(percentileIncomeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));

        AverageIncomeByAgeReportBuilder incomeByAgeBuilder =
                new AverageIncomeByAgeReportBuilder(ageProvider);
        csvReportWriterBuilder
                .withHeaders("Age Range", "Average Income")
                .withFirstColumnResolvedAs(Starter::buildAgeBucketStringRepresentation)
                .toFile(INCOME_AVERAGE_BY_AGE_RANGE_REPORT_FILE_NAME)
                .ofDoubles()
                .writeReport(incomeByAgeBuilder.buildReport(employeeDataHolder.getEmployeeStream()));
    }

    private static String buildAgeBucketStringRepresentation(int ageBucket) {
        return String.format("(%d-%d]", (ageBucket - 1) * 10, ageBucket * 10);
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
