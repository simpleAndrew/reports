package org.challenge.test.fs;

import org.challenge.test.data.Employee;
import org.challenge.test.utils.TestFileUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class EmployeeDaoTest {

    @Test
    public void shouldReadEmployeesFromRealFile() throws Exception {
        //give
        Path root = TestFileUtils.getPathToResource("data/real/employees.csv");

        //when
        EmployeeDataHolder dao = EmployeeDataHolder.build(root);
        Stream<Employee> employeeStream = dao.getEmployeeStream();

        //then
        assertEquals("Number of employees should be correct", 98, employeeStream.count());
    }

    @Test
    public void shouldConvertEmployeesCorrectly() throws Exception {
        //give
        Path root = TestFileUtils.getPathToResource("data/short/employees.csv");
        Employee expected = new Employee(6, "Opal Ballard", "f", 4350.00);

        //when
        EmployeeDataHolder dao = EmployeeDataHolder.build(root);
        Employee employee = dao.getEmployeeStream().findFirst().get();

        //then
        assertEquals("Should read the only employeeName correctly", expected, employee);
    }

    @Test
    public void shouldSkipCorruptedLines() throws Exception {
        //give
        Path root = TestFileUtils.getPathToResource("data/short/corrupted-employee.csv");
        Employee expected = new Employee(2, "Jonathon Kim", "m", 1030.00);

        //when
        EmployeeDataHolder dao = EmployeeDataHolder.build(root);
        Employee employee = dao.getEmployeeStream().findFirst().get();

        //then
        assertEquals("Should read the only valid employeeName correctly", expected, employee);
    }
}