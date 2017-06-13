package org.challenge.hubricks.report;

import org.challenge.hubricks.report.data.AgedEmployeeTestBean;
import org.challenge.hubricks.utils.TestFileUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MedianAgeByDepartmentTest {

    @Test
    public void shouldBuildReportOnStreamOfOne() throws Exception {
        Map<Integer, Integer> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 50);

        testFile("reports/input/medianAge/oneEmployee.csv", expectedAgesReport);
    }

    @Test
    public void shouldCalculateMedianOnMultipleEmployeesPerDepartment() throws Exception {
        Map<Integer, Integer> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 50);

        testFile("reports/input/medianAge/multipleEmployeesInOneDept.csv", expectedAgesReport);
    }

    @Test
    public void shouldCalculateMedianOnMultipleEmployeesPerDepartments() throws Exception {
        Map<Integer, Integer> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 50);
        expectedAgesReport.put(2, 40);

        testFile("reports/input/medianAge/multipleEmployeesIn2Depts.csv", expectedAgesReport);
    }

    @Test
    public void shouldCalculateMedianOnMultipleEmployeesPer3Departments() throws Exception {
        Map<Integer, Integer> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 50);
        expectedAgesReport.put(2, 40);
        expectedAgesReport.put(3, 20);

        testFile("reports/input/medianAge/multipleEmployeesIn3Depts.csv", expectedAgesReport);
    }

    private void testFile(String resourceLocation, Map<Integer, Integer> expectedResult) throws IOException {
        AgedEmployeeTestBean agedEmployeeTestBean = AgedEmployeeTestBean.buildFrom(TestFileUtils.readTestCsvResource(resourceLocation));

        MedianAgeByDepartment averageAgeBuilder = new MedianAgeByDepartment(agedEmployeeTestBean.getAgeProducer());
        Map<Integer, Integer> generatedReport = averageAgeBuilder.buildReport(agedEmployeeTestBean.getEmployees().stream());

        assertEquals(expectedResult, generatedReport);
    }
}