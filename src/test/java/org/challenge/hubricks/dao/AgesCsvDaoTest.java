package org.challenge.hubricks.dao;

import org.challenge.hubricks.utils.TestFileUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AgesCsvDaoTest {

    @Test
    public void supposeToReadAgeFromRealFile() throws Exception {
        //given
        Path pathToResource = TestFileUtils.getPathToResource("data/real/ages.csv");
        String employeeName = "Opal Ballard";
        int expectedAge = 23;

        //when
        AgesCsvDao agesCsvDao = AgesCsvDao.buildDao(pathToResource);
        int age = agesCsvDao.getAge(employeeName).get();

        //then
        assertEquals("Age suppose to be read as expected", expectedAge, age);
    }

    @Test
    public void shouldSkipBrokenLines() throws Exception {
        //given
        Path pathToResource = TestFileUtils.getPathToResource("data/short/corrupted-ages.csv");
        String employeeName = "Opal Ballard";

        //when
        AgesCsvDao agesCsvDao = AgesCsvDao.buildDao(pathToResource);
        Optional<Integer> age = agesCsvDao.getAge(employeeName);

        //then
        assertFalse("Age should be missing", age.isPresent());
    }
}