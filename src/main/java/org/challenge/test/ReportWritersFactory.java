package org.challenge.test;

import org.challenge.test.fs.csv.CsvReportWriter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ReportWritersFactory {

    private static final String EMPLOYEE_AGE_BY_DEPARTMENT_REPORT_FILE_NAME = "employee-age-by-department.csv";
    private static final String INCOME_BY_DEPARTMENT_REPORT_FILE_NAME = "income-by-department.csv";
    private static final String INCOME_95_BY_DEPARTMENT_REPORT_FILE_NAME = "income-95-by-department.csv";
    private static final String INCOME_AVERAGE_BY_AGE_RANGE_REPORT_FILE_NAME = "income-average-by-age-range.csv";


    private final Function<Integer, String> departmentNameProvider;
    private final Path rootPath;

    public ReportWritersFactory(Path rootPath, Function<Integer, String> departmentNameProvider) {
        this.departmentNameProvider = departmentNameProvider;
        this.rootPath = rootPath;
    }

    public CsvReportWriter getMedianAgeByDepartmentWriter() {
        return new CsvReportWriterBuilder()
                        .withReportsRoot(rootPath)
                        .withFirstColumnResolvedAs(departmentNameProvider)
                        .withHeaders("Department", "Age Median")
                        .toFile(EMPLOYEE_AGE_BY_DEPARTMENT_REPORT_FILE_NAME)
                        .ofIntegers();
    }

    public CsvReportWriter getMedianIncomeByDepartmentWriter() {
        return new CsvReportWriterBuilder()
                .withReportsRoot(rootPath)
                .withFirstColumnResolvedAs(departmentNameProvider)
                .withHeaders("Department", "Income Median")
                .toFile(INCOME_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofDoubles();
    }

    public CsvReportWriter get95thPercentileIncomeByDepartmentWriter() {
        return new CsvReportWriterBuilder()
                .withReportsRoot(rootPath)
                .withFirstColumnResolvedAs(departmentNameProvider)
                .withHeaders("Department", "Income 95th Percentile")
                .toFile(INCOME_95_BY_DEPARTMENT_REPORT_FILE_NAME)
                .ofDoubles();
    }

    public CsvReportWriter getIncomeAverageByAge() {
        return new CsvReportWriterBuilder()
                .withReportsRoot(rootPath)
                .withFirstColumnResolvedAs(ReportWritersFactory::buildAgeBucketStringRepresentation)
                .withHeaders("Age Range", "Income Average")
                .toFile(INCOME_AVERAGE_BY_AGE_RANGE_REPORT_FILE_NAME)
                .ofDoubles();
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
