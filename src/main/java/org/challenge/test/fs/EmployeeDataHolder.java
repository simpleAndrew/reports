package org.challenge.test.fs;

import org.challenge.test.data.Employee;
import org.challenge.test.fs.csv.CsvFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeDataHolder {

    private final Path filePath;

    private List<Employee> employees;

    public static EmployeeDataHolder build(Path filePath) throws IOException {
        EmployeeDataHolder employeeDao = new EmployeeDataHolder(filePath);
        employeeDao.loadDate();

        return employeeDao;
    }

    public EmployeeDataHolder(Path filePath) {
        this.filePath = filePath;
    }

    private void loadDate() throws IOException {
        CsvFileReader csvFileReader = new CsvFileReader(filePath.toString());
        employees = csvFileReader.getDataStream()
                .map(EmployeeDataHolder::buildOutOfLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public Stream<Employee> getEmployeeStream() {
        return employees.stream();
    }

    public static Employee buildOutOfLine(String[] columnValues) {
        try {
            return new Employee(
                    Integer.valueOf(columnValues[0]),
                    columnValues[1],
                    columnValues[2],
                    Double.valueOf(columnValues[3])
            );
        } catch (Exception e) {
            System.out.println("Failed to convert into employeeName:" + Arrays.asList(columnValues));
            return null;
        }
    }
}
