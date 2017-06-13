package org.challenge.hubricks.report;

import org.challenge.hubricks.report.data.AgedEmployeeTestBean;
import org.challenge.hubricks.utils.TestFileUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AverageAgeBuilderTest {

    @Test
    public void shouldBuildReportOnStreamOfOne() throws Exception {
        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 50.0);

        testFile("reports/input/averageAge/oneEmployee.csv", expectedAgesReport);
    }

    @Test
    public void shouldSplitFor2Departments() throws Exception {

        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 20.0);
        expectedAgesReport.put(2, 30.0);

        testFile("reports/input/averageAge/2departments.csv", expectedAgesReport);
    }

    @Test
    public void shouldAverageAges() throws Exception {

        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 25.0);

        testFile("reports/input/averageAge/oneDepartmentWithAverage.csv", expectedAgesReport);
    }

    @Test
    public void shouldSplitFor2DepartmentsWithAverages() throws Exception {

        Map<Integer, Double> expectedAgesReport = new HashMap<>();
        expectedAgesReport.put(1, 32.5);
        expectedAgesReport.put(2, 45.0);
        expectedAgesReport.put(3, 100.0);

        testFile("reports/input/averageAge/3departmentsWithDifferentNature.csv", expectedAgesReport);
    }

    private void testFile(String resourceLocation, Map<Integer, Double> expectedResult) throws IOException {
        AgedEmployeeTestBean agedEmployeeTestBean = AgedEmployeeTestBean.buildFrom(TestFileUtils.readTestCsvResource(resourceLocation));

        AverageAgeBuilder averageAgeBuilder = new AverageAgeBuilder(agedEmployeeTestBean.getAgeProducer());
        Map<Integer, Double> generatedReport = averageAgeBuilder.buildReport(agedEmployeeTestBean.getEmployees().stream());

        assertEquals(expectedResult, generatedReport);
    }

}