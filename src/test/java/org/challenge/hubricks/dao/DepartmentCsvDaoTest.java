package org.challenge.hubricks.dao;

import org.challenge.hubricks.utils.TestFileUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DepartmentCsvDaoTest {

    @Test
    public void shouldReadDepartmentsFromRealFile() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");

        //when
        DepartmentCsvDao departmentCsvDao = DepartmentCsvDao.buildDao(pathToDepartments);

        //then
        assertNotNull("Dao should be initialized", departmentCsvDao);
    }

    @Test
    public void shouldReturnDepartmentAfterCreation() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");
        int expectedNumberOfDepartments = 7;

        //when
        DepartmentCsvDao departmentCsvDao = DepartmentCsvDao.buildDao(pathToDepartments);

        //then
        assertEquals("Should read number of departments correctly", expectedNumberOfDepartments, departmentCsvDao.getNumberOfDepartments());
    }

    @Test
    public void shouldInitDepartmentsInCorrcetOrder() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");
        String expectedFirstDepartment = "Accounting";
        String expectedLastDepartment = "Sales";

        //when
        DepartmentCsvDao departmentCsvDao = DepartmentCsvDao.buildDao(pathToDepartments);
        String firstDepartmentName = departmentCsvDao.lookForDepartment(0).get();
        String lastDepartmentName = departmentCsvDao.lookForDepartment(6).get();

        //then
        assertEquals("Should correctly init first department", expectedFirstDepartment, firstDepartmentName);
        assertEquals("Should corrcetly init last department", expectedLastDepartment, lastDepartmentName);
    }

    @Test
    public void shouldThrowExceptionOnWrongDepartmentRequest() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");


        //when
        DepartmentCsvDao departmentCsvDao = DepartmentCsvDao.buildDao(pathToDepartments);
        Optional<String> missingDepartment = departmentCsvDao.lookForDepartment(-1);

        //then
        assertFalse("Should not get normal value for missing department", missingDepartment.isPresent());
    }
}