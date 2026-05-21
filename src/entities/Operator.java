package entities;
import enums.Designation;
import utils.Constants;

public class Operator extends Employee {

    public Operator(String id, String name, double baseSalary) {
        super(id, name, Designation.OPERATOR, baseSalary);
    }

    @Override
    public double calculateNetSalary() {
        ShiftSummary summary = getShiftSummary();
        
        // Step 1: Calculate the money earned
        double bonus = summary.getTotalDeliveries() * Constants.OPERATOR_DELIVERY_PAY;
        
        // Step 2: Calculate the money lost
        double wallFine = summary.getTotalWallHits() * Constants.OPERATOR_WALL_PENALTY;
        double restrictedFine = summary.getTotalRestrictedHits() * Constants.OPERATOR_RESTRICTED_PENALTY;
        
        // Step 3: Final math
        return getBaseSalary() + bonus - wallFine - restrictedFine;
    }
}