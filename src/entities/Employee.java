package src.entities;
import entities.PayableSalary;
import enums.Designation;

public abstract class Employee implements PayableSalary {
    
    private final String id;
    private final String name;
    private final Designation designation;
    private final double baseSalary;
    private final String managerId; 
    
    private ShiftSummary shiftSummary;

    public Employee(String id, String name, Designation designation, double baseSalary, String managerId) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.baseSalary = baseSalary;
        this.managerId = managerId;
        this.shiftSummary = new ShiftSummary(); // Automatically gives them a fresh summary
    }

    // ... (Getters for the variables omitted for brevity) ...

    public ShiftSummary getShiftSummary() { 
        return this.shiftSummary; 
    }

    @Override
    public abstract double calculateNetSalary();
}