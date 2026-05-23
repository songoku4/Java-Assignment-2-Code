package entities;

import enums.Designation;

public abstract class Employee implements Payable {
    
    private final String id;
    private final String name;
    private final Designation role;
    private final double baseSalary;
    private final String managerId;
    
    private ShiftSummary myShiftSummary;

    public Employee(String id, String name, Designation role, double baseSalary, String managerId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.baseSalary = baseSalary;
        this.managerId = managerId;
        this.myShiftSummary = new ShiftSummary();
    }

    public String getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public Designation getRole() { 
        return role; 
    }
    public double getBaseSalary() { 
        return baseSalary; 
    }
    public String getManagerId() { 
        return managerId; 
    }
    
    public ShiftSummary getShiftSummary() {
         return myShiftSummary; 
        }

    @Override
    public abstract double calculateNetSalary();
}