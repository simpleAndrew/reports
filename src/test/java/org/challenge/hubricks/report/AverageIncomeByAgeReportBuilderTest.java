package org.challenge.hubricks.report;

import org.challenge.hubricks.report.data.AgedEmployeeTestBean;
import org.challenge.hubricks.utils.TestFileUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AverageIncomeByAgeReportBuilderTest {

    @Test
    public void shouldCalculateMedianOnOneEmployee() throws Exception {
        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(3, 1000.5);

        testFile("reports/input/incomeByAge/oneEmployee.csv", expectedAgesReport);
    }

    @Test
    public void shouldAssignBucketsCorrectly() throws Exception {
        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(3, 2000.0);
        expectedAgesReport.put(2, 1000.0);

        testFile("reports/input/incomeByAge/employeesWithAgeOnBounds.csv", expectedAgesReport);
    }

    @Test
    public void shouldCalculateMedianOnEmployeesInSameDozen() throws Exception {
        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(4, 20500.25);

        testFile("reports/input/incomeByAge/2employeeInSameDozen.csv", expectedAgesReport);
    }

    @Test
    public void shouldCalculateMedianOnMultipleEmployeesPer3Departments() throws Exception {
        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(3, 2000.0);
        expectedAgesReport.put(5, 4500.0);
        expectedAgesReport.put(6, 7000.0);

        testFile("reports/input/incomeByAge/multipleEmployeesIn3Depts.csv", expectedAgesReport);
    }

    private void testFile(String resourceLocation, Map<Integer, Double> expectedResult) throws IOException {
        AgedEmployeeTestBean agedEmployeeTestBean = AgedEmployeeTestBean.buildFrom(TestFileUtils.readTestCsvResource(resourceLocation));

        AverageIncomeByAgeReportBuilder averageAgeBuilder =
                new AverageIncomeByAgeReportBuilder(agedEmployeeTestBean.getAgeProducer());

        Map<Integer, Double> generatedReport = averageAgeBuilder.buildReport(agedEmployeeTestBean.getEmployees().stream());

        assertEquals(expectedResult, generatedReport);
    }
}