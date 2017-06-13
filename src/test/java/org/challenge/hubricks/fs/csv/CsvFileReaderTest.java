package org.challenge.hubricks.fs.csv;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.challenge.hubricks.utils.TestFileUtils.getStringPathToResource;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class CsvFileReaderTest {

    @Test
    public void shouldReadFileThatExists() throws IOException {
        //given
        String fileLocation = getStringPathToResource("csv/sample.csv");
        int expectedLinesInFile = 1;

        //when
        CsvFileReader csvFileReader = new CsvFileReader(fileLocation);
        List<String[]> strings = csvFileReader.getData();

        //then
        assertEquals("Only one line should be read", expectedLinesInFile, strings.size());
    }

    @Test
    public void shouldReadDataAndSplitItOnColumns() throws IOException {
        //given
        validateReadContent("csv/sample.csv");
    }

    @Test
    public void shouldRemoveTrailingSpaces() throws IOException {
        validateReadContent("csv/spaced-data.csv");
    }

    @Test
    public void shouldRemoveTrailingTabs() throws IOException {
        //given
        validateReadContent("csv/tabbed-data.csv");
    }

    @Test
    public void shouldSkipEmptyLines() throws IOException {
        //given
        String fileLocation = getStringPathToResource("csv/with-empty-line.csv");

        //when
        CsvFileReader csvFileReader = new CsvFileReader(fileLocation);
        List<String[]> strings = csvFileReader.getData();

        //then
        assertEquals("Empty line should be skipped", 2, strings.size());
    }

    @Test
    public void shouldReadRealFile() throws IOException {
        //given
        String fileLocation = getStringPathToResource("csv/real-kind-of-data.csv");

        //when
        CsvFileReader csvFileReader = new CsvFileReader(fileLocation);
        List<String[]> strings = csvFileReader.getData();

        //then
        int readLinesNumber = strings.size();
        assertEquals("All lines should be read", 100, readLinesNumber);
    }

    private void validateReadContent(String resourceShortcut) throws IOException {
        //given
        String fileLocation = getStringPathToResource(resourceShortcut);
        String[] expectedData = {"1", "Me", "Hello"};

        //when
        CsvFileReader csvFileReader = new CsvFileReader(fileLocation);
        List<String[]> strings = csvFileReader.getData();

        //then
        String[] readLine = strings.get(0);
        assertArrayEquals("All columns should be recognized", expectedData, readLine);
    }

    @Test(expected = IOException.class)
    public void shouldFailIfRequestedFileMissing() throws IOException {
        //given
        String brokenFileLocation = "no-csv/not_existing_file.csv_not";

        //when
        CsvFileReader csvFileReader = new CsvFileReader(brokenFileLocation);
        csvFileReader.getData();

        //then
        fail("Class suppose to fail on missing file read attempt");
    }

    @Test
    public void shouldNotRecogniseNonCsvFormat() throws IOException {
        //given
        String brokenFileLocation = getStringPathToResource("csv/non-csv.txt");

        //when
        CsvFileReader csvFileReader = new CsvFileReader(brokenFileLocation);
        List<String[]> readResult = csvFileReader.getData();

        //then
        assertFalse("Reader should read usual txt data", readResult.isEmpty());
    }

}