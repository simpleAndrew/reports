package org.challenge.hubricks;

import org.challenge.hubricks.fs.AgeDataHolder;
import org.challenge.hubricks.fs.DepartmentDataHolder;
import org.challenge.hubricks.fs.EmployeeDataHolder;

import java.io.IOException;
import java.nio.file.Path;

public class DataHoldersFactory {

    private static final String DEPARTMENTS_FILE_NAME = "departments.csv";
    private static final String EMPLOYEES_FILE_NAME = "employees.csv";
    private static final String AGES_FILE_NAME = "ages.csv";

    private final Path reportsRootPath;

    public DataHoldersFactory(Path reportsRootPath) {
        this.reportsRootPath = reportsRootPath;
    }

    public AgeDataHolder getAgeHolder() {
        try {
            return AgeDataHolder.build(reportsRootPath.resolve(AGES_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException("Can't find ages.csv file in " + reportsRootPath.toString(), e);
        }
    }

    public EmployeeDataHolder getEmployeeHolder() {
        try {
            return EmployeeDataHolder.build(reportsRootPath.resolve(EMPLOYEES_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException("Can't find employees.csv file in " + reportsRootPath.toString(), e);
        }
    }

    public DepartmentDataHolder getDepartmentHolder() {
        try {
            return DepartmentDataHolder.build(reportsRootPath.resolve(DEPARTMENTS_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException("Can't find departments.csv file in " + reportsRootPath.toString(), e);
        }
    }
}
