package entities;
import enums.Designation;

public class PayrollManager extends Employee {
    public PayrollManager(String id, String name, double baseSalary, String managerId) {
        super(id, name, Designation.PAYROLL_MANAGER, baseSalary, managerId);
    }

    @Override
    public double calculateNetSalary() {
        return getBaseSalary();
    }
}