package org.challenge.test.fs;

import org.challenge.test.utils.TestFileUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class DepartmentDataHolderTest {

    @Test
    public void shouldReadDepartmentsFromRealFile() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");

        //when
        DepartmentDataHolder departmentDataHolder = DepartmentDataHolder.build(pathToDepartments);

        //then
        assertNotNull("Dao should be initialized", departmentDataHolder);
    }

    @Test
    public void shouldIndexDepartmentsStartingWithOne() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");
        String expectedFirstDepartment = "Accounting";

        //when
        DepartmentDataHolder departmentDataHolder = DepartmentDataHolder.build(pathToDepartments);

        //then
        assertEquals("Should return first department correctly", expectedFirstDepartment, departmentDataHolder.getDepartmentByIndex(1));
    }

    @Test
    public void shouldReturnDepartmentAfterCreation() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");
        int expectedNumberOfDepartments = 7;

        //when
        DepartmentDataHolder departmentDataHolder = DepartmentDataHolder.build(pathToDepartments);

        //then
        assertEquals("Should read number of departments correctly", expectedNumberOfDepartments, departmentDataHolder.getNumberOfDepartments());
    }

    @Test
    public void shouldInitDepartmentsInCorrectOrder() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");
        String expectedFirstDepartment = "Accounting";
        String expectedLastDepartment = "Sales";

        //when
        DepartmentDataHolder departmentDataHolder = DepartmentDataHolder.build(pathToDepartments);
        String firstDepartmentName = departmentDataHolder.lookForDepartment(1).get();
        String lastDepartmentName = departmentDataHolder.lookForDepartment(7).get();

        //then
        assertEquals("Should correctly init first department", expectedFirstDepartment, firstDepartmentName);
        assertEquals("Should corrcetly init last department", expectedLastDepartment, lastDepartmentName);
    }

    @Test
    public void shouldThrowExceptionOnWrongDepartmentRequest() throws Exception {
        //given
        Path pathToDepartments = TestFileUtils.getPathToResource("data/real/departments.csv");


        //when
        DepartmentDataHolder departmentDataHolder = DepartmentDataHolder.build(pathToDepartments);
        Optional<String> missingDepartment = departmentDataHolder.lookForDepartment(-1);

        //then
        assertFalse("Should not get normal value for missing department", missingDepartment.isPresent());
    }
}