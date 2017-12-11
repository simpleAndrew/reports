package org.challenge.test.data;

public class Employee {

    private final int departmentId;
    private final String name;
    private final String sex;
    private final double salary;

    public Employee(int departmentId, String name, String sex, double salary) {
        this.departmentId = departmentId;
        this.name = name;
        this.sex = sex;
        this.salary = salary;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (departmentId != employee.departmentId) return false;
        if (Double.compare(employee.salary, salary) != 0) return false;
        if (name != null ? !name.equals(employee.name) : employee.name != null) return false;
        return sex != null ? sex.equals(employee.sex) : employee.sex == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = departmentId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        temp = Double.doubleToLongBits(salary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
