package org.challenge.hubricks.fs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AgeDataHolder {

    private final Path filePath;
    private Map<String, Integer> nameToAge;

    public static AgeDataHolder buildDao(Path filePath) throws IOException {
        AgeDataHolder ageDataHolder = new AgeDataHolder(filePath);
        ageDataHolder.loadData();
        return ageDataHolder;
    }

    private AgeDataHolder(Path filePath) {
        this.filePath = filePath;
    }

    private void loadData() throws IOException {
        nameToAge = new CsvFileReader(filePath.toString()).getDataStream()
                .map(AgeTuple::buildOfLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(AgeTuple::getEmployeeName, AgeTuple::getAge));
    }


    public Optional<Integer> lookForAge(String employeeName) {
        return Optional.ofNullable(nameToAge.getOrDefault(employeeName, null));
    }

    private static class AgeTuple {

        private static AgeTuple buildOfLine(String[] line) {
            try {
                return new AgeTuple(
                        Integer.parseInt(line[1]),
                        line[0]);
            } catch (Exception e) {

                System.out.println("Failed to convert age-name pair:" + Arrays.asList(line));
                return null;
            }
        }

        final int age;
        final String employeeName;

        public AgeTuple(int age, String employee) {
            this.age = age;
            this.employeeName = employee;
        }

        public int getAge() {
            return age;
        }

        public String getEmployeeName() {
            return employeeName;
        }
    }
}
