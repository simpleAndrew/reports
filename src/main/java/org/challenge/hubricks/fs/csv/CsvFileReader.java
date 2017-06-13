package org.challenge.hubricks.fs.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvFileReader {

    private final String filePath;

    public CsvFileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> getData() throws IOException {
        return getDataStream().collect(Collectors.toList());
    }

    public Stream<String[]> getDataStream() throws IOException {
        return Files.lines(Paths.get(filePath))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(splitColumns());
    }

    private Function<String, String[]> splitColumns() {
        return line -> line.split("\\s*,\\s*");
    }


}
