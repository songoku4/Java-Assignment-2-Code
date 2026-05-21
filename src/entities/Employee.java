package entities;
import enums.Designation;

public abstract class Employee implements Payable {
    
    private final String id;
    private final String name;
    private final Designation role;
    private final double baseSalary;
    
    // Every employee has their own summary to track hits and deliveries
    private ShiftSummary myShiftSummary;
    public void printPayslipBreakdown()

    public Employee(String id, String name, Designation role, double baseSalary) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.baseSalary = baseSalary;
        this.myShiftSummary = new ShiftSummary(); 
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Designation getRole() { return role; }
    public double getBaseSalary() { return baseSalary; }
    
    public ShiftSummary getShiftSummary() { return myShiftSummary; }

    // Child classes must provide the math for this
    @Override
    public abstract double calculateNetSalary();
}