package org.challenge.hubricks.report;

import org.challenge.hubricks.data.Employee;

import java.util.Map;
import java.util.stream.Stream;

public interface EmployeeReportBuilder<Key, Value> {
    Map<Key, Value> buildReport(Stream<Employee> employeeStream);
}
