package org.challenge.test.fs.csv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvReportWriter {

    private final List<String> headers;
    private final Function<Integer, String> departmentNameProvider;
    private final String format;
    private Path path;

    public CsvReportWriter(Path filePath, List<String> headers, Function<Integer, String> departmentNameProvider, String format) {
        this.format = format;
        this.departmentNameProvider = departmentNameProvider;
        this.headers = headers;
        this.path = filePath;
    }

    public void writeReport(Map<Integer, ? extends Number> data) throws IOException {
        List<String> report = new LinkedList<>();
        report.add(headers.stream().collect(Collectors.joining(", ")));
        report.addAll(convertToCsv(data));

        writeToFile(path, report);
    }

    private List<String> convertToCsv(Map<Integer, ? extends Number> data) {
        List<String> lines = new ArrayList<>(data.size());
        data.forEach((deptId, value) -> lines.add(String.format(format, departmentNameProvider.apply(deptId), value)));

        return lines;
    }

    private void writeToFile(Path path, List<String> report) throws IOException {
        Files.write(path, report);
    }


}
