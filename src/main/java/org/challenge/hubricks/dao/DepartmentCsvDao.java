package org.challenge.hubricks.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DepartmentCsvDao {

    private List<String> sortedDepartments;

    private final Path fileLocation;

    public static DepartmentCsvDao buildDao(Path dataFolder) throws IOException {
        DepartmentCsvDao dao = new DepartmentCsvDao(dataFolder);
        dao.readDepartments();
        return dao;
    }

    private DepartmentCsvDao(Path filelocation) {
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
        if (index <0 || index >= sortedDepartments.size()) {
            throw new RuntimeException("Unknown department ID detected");
        }

        return sortedDepartments.get(index);
    }

    public Optional<String> lookForDepartment(int index) {
        if (index <0 || index >= sortedDepartments.size()) {
            return Optional.empty();
        }

        return Optional.of(sortedDepartments.get(index));
    }
}