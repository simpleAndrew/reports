package org.challenge.hubricks.fs;

import org.challenge.hubricks.fs.csv.CsvFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DepartmentDataHolder {

    private List<String> sortedDepartments;

    private final Path fileLocation;

    public static DepartmentDataHolder buildDao(Path dataFolder) throws IOException {
        DepartmentDataHolder dao = new DepartmentDataHolder(dataFolder);
        dao.readDepartments();
        return dao;
    }

    private DepartmentDataHolder(Path filelocation) {
        this.fileLocation = filelocation;
    }

    private void readDepartments() throws IOException {

        sortedDepartments = new CsvFileReader(fileLocation.toString())
                .getDataStream()
                .map(line -> line[0])
                .sorted()
                .collect(Collectors.toList());
    }

    public int getNumberOfDepartments() {
        return sortedDepartments.size();
    }

    public Set<String> getDepartments() {
        return new HashSet<>(sortedDepartments);
    }

    public String getDepartmentByIndex(int index) {
        if (index < 1 || index > sortedDepartments.size()) {
            throw new RuntimeException("Unknown department ID detected");
        }

        return sortedDepartments.get(index - 1);
    }

    public Optional<String> lookForDepartment(int index) {
        if (index < 1 || index > sortedDepartments.size()) {
            return Optional.empty();
        }

        return Optional.of(sortedDepartments.get(index - 1));
    }
}