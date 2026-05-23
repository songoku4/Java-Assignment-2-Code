package entities;
import enums.Designation;
import utils.Constants;

public class SeniorOperator extends Employee {
    public SeniorOperator(String id, String name, double baseSalary, String managerId) {
        super(id, name, Designation.SENIOR_OPERATOR, baseSalary, managerId);
    }

    @Override
    public double calculateNetSalary() {
        ShiftSummary summary = getShiftSummary();
        double bonus = summary.getTotalDeliveries() * Constants.OPERATOR_DELIVERY_PAY;
        double wallFine = summary.getTotalWallHits() * Constants.OPERATOR_WALL_PENALTY;
        double restrictedFine = summary.getTotalRestrictedHits() * Constants.OPERATOR_RESTRICTED_PENALTY;
        return getBaseSalary() + bonus - wallFine - restrictedFine;
    }
}