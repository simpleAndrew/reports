package org.challenge.test.report;

import org.challenge.test.report.data.SalaryEmployeeTestBean;
import org.challenge.test.utils.TestFileUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PercentileIncomeReportBuilderTest {

    @Test
    public void shouldBuildReportOnStreamOfOne() throws Exception {
        Map<Integer, Double> expected = new HashMap<>();
        expected.put(1, 1000.5);

        testMedian("reports/input/median/oneEmployee.csv", expected);
    }

    @Test
    public void shouldBuildReportOnStreamOfMultipleEmployees() throws Exception {
        Map<Integer, Double> expected = new HashMap<>();
        expected.put(1, 1000.5);
        expected.put(2, 40000.0);

        testMedian("reports/input/median/2departments.csv", expected);
    }

    @Test
    public void shouldBuildReportOnStreamOfMultipleEmployeesWithMedians() throws Exception {
        Map<Integer, Double> expected = new HashMap<>();
        expected.put(1, 99.0);
        expected.put(2, 444.0);

        testMedian("reports/input/median/2departmentsMedianToGo.csv", expected);
    }

    @Test
    public void shouldBuildReportOnStreamOfMultipleEmployeesWithPercentile95() throws Exception {
        Map<Integer, Double> expected = new HashMap<>();
        expected.put(1, 1005.0);
        expected.put(2, 40000.0);

        testFile(0.95, "reports/input/percentile/2departments.csv", expected);
    }

    @Test
    public void shouldBuildReportOnStreamOfMultipleEmployeesWithPercentile80() throws Exception {
        Map<Integer, Double> expected = new HashMap<>();
        expected.put(1, 1000.0);
        expected.put(2, 401.0);

        testFile(0.80, "reports/input/percentile/2departments.csv", expected);
    }

    @Test
    public void shouldBuildReportOnStreamOfMultipleEmployeesWithPercentile05() throws Exception {
        Map<Integer, Double> expected = new HashMap<>();
        expected.put(1, 100.0);
        expected.put(2, 4.0);

        testFile(0.05, "reports/input/percentile/2departments.csv", expected);
    }

    private void testMedian(String fileShortcut, Map<Integer, Double> expectedReport) throws IOException {
        testFile(0.5, fileShortcut, expectedReport);
    }

    private void testFile(double percentile, String fileShortcut, Map<Integer, Double> expectedReport) throws IOException {

        PercentileIncomeReportBuilder percentileSalaryReportBuilder =
                PercentileIncomeReportBuilder.ofPercentile(percentile);

        SalaryEmployeeTestBean salaryEmployeeTestBean =
                SalaryEmployeeTestBean.buildFrom(TestFileUtils.readTestCsvResource(fileShortcut));

        Map<Integer, Double> integerDoubleMap =
                percentileSalaryReportBuilder.buildReport(salaryEmployeeTestBean.getEmployees().stream());

        assertEquals(expectedReport, integerDoubleMap);
    }

}