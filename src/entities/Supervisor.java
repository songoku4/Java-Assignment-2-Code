package entities;
import enums.Designation;
import utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class Supervisor extends Employee {

    private List<Employee> myTeam;

    public Supervisor(String id, String name, double baseSalary) {
        super(id, name, Designation.SUPERVISOR, baseSalary);
        this.myTeam = new ArrayList<>();
    }

    public void addTeamMember(Employee emp) {
        myTeam.add(emp);
    }

    @Override
    public double calculateNetSalary() {
        ShiftSummary summary = getShiftSummary();
        
        double bonus = summary.getTotalDeliveries() * Constants.SUPERVISOR_DELIVERY_PAY;
        double teamBonus = myTeam.size() * Constants.SUPERVISOR_TEAM_PAY;
        double wallFine = summary.getTotalWallHits() * Constants.SUPERVISOR_WALL_PENALTY;
        
        // Supervisors don't get fined for restricted areas
        return getBaseSalary() + bonus + teamBonus - wallFine;
    }
}