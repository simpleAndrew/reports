package org.challenge.hubricks.fs;

import org.challenge.hubricks.utils.TestFileUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AgeDataHolderTest {

    @Test
    public void supposeToReadAgeFromRealFile() throws Exception {
        //given
        Path pathToResource = TestFileUtils.getPathToResource("data/real/ages.csv");
        String employeeName = "Opal Ballard";
        int expectedAge = 23;

        //when
        AgeDataHolder ageDataHolder = AgeDataHolder.buildDao(pathToResource);
        int age = ageDataHolder.lookForAge(employeeName).get();

        //then
        assertEquals("Age suppose to be read as expected", expectedAge, age);
    }

    @Test
    public void shouldSkipBrokenLines() throws Exception {
        //given
        Path pathToResource = TestFileUtils.getPathToResource("data/short/corrupted-ages.csv");
        String employeeName = "Opal Ballard";

        //when
        AgeDataHolder ageDataHolder = AgeDataHolder.buildDao(pathToResource);
        Optional<Integer> age = ageDataHolder.lookForAge(employeeName);

        //then
        assertFalse("Age should be missing", age.isPresent());
    }
}