import engine.WarehouseMap;
import entities.*;
import enums.Designation;
import utils.FileManager;
import exceptions.NotFoundException;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class WarehouseManagerEngine {

    private static final Scanner SCANNER = new Scanner(System.in);
    
    private Map<String, Employee> employees;
    private WarehouseMap map;
    private boolean shiftStarted = false;
    private boolean payslipsGeneratedForSession = false;
    private boolean historicalPayslipsExist = false;
    private boolean shiftComplete = false;

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Invalid number of Command Line Arguments. Usage: java WarehouseManagerEngine <floors> <rows> <cols> <master file> <employees file>");
            return;
        }

        try {
            int floors = Integer.parseInt(args[0]);
            int rows = Integer.parseInt(args[1]);
            int cols = Integer.parseInt(args[2]);
            String mapFile = args[3];
            String empFile = args[4];

            if (floors < 1 || floors > 3) {
                System.out.println("Error: Number of floors has to be between 1 and 3.");
                return;
            }
            if (rows < 4 || cols < 4) {
                System.out.println("Error: Rows and columns must be at least 4 to allow proper map layout.");
                return;
            }

            File mFile = new File(mapFile);
            File eFile = new File(empFile);
            if (!mFile.exists() || !eFile.exists()) {
                System.out.println("Unable to process file. Exiting program.");
                return;
            }

            WarehouseManagerEngine engine = new WarehouseManagerEngine();
            engine.startSystem(floors, rows, cols, mapFile, empFile);

        } catch (NumberFormatException e) {
            System.out.println("Error: Floors, rows and columns must be integers.");
        }
    }

private void startSystem(int floors, int rows, int cols, String mapFile, String empFile) {
        String payslipFile = "data/payslips.csv";

        System.out.println("Processing Warehouse file: " + mapFile);
        this.map = new WarehouseMap(floors, rows, cols);
        FileManager.loadWarehouseMap(mapFile, this.map, floors);

        System.out.println("Processing Employees file: " + empFile);
        this.employees = FileManager.loadEmployees(empFile);

        System.out.println("Processing Payslips file: " + payslipFile);
        
        File pFile = new File(payslipFile);
        if (!pFile.exists()) {
            System.out.println("Payslip file doesnt exist: " + payslipFile);
        } else {
            this.historicalPayslipsExist = true;
        }

        System.out.println("Welcome to Warehouse Manager Assignment 2.");
        runLoginLoop(payslipFile);
    }

    private void runLoginLoop(String payslipFile) {
        boolean running = true;
        
        while (running) {
            System.out.println("\n=== Employee Login ===");
            System.out.print("Enter your Employee ID or X to terminate: ");
            String input = SCANNER.nextLine().trim();

            if (input.equalsIgnoreCase("X")) {
                System.out.println("Saving Payslips file: " + payslipFile);
                FileManager.writePayslips(payslipFile, this.employees);
                System.out.println("Goodbye!");
                running = false;
                continue;
            }

            Employee loggedInUser = this.employees.get(input.toUpperCase());

            if (loggedInUser == null) {
                System.out.println("Employee ID not found. Please try again.");
                continue;
            }

            System.out.println("\nWelcome, " + loggedInUser.getName() + " [" + loggedInUser.getRole() + "]");

            if (loggedInUser.getRole() == Designation.OPERATOR || loggedInUser.getRole() == Designation.SENIOR_OPERATOR) {
                runOperatorMenu(loggedInUser);
            }
            else if (loggedInUser.getRole() == Designation.SUPERVISOR) {
                runSupervisorMenu((Supervisor) loggedInUser);
            }
            else if (loggedInUser.getRole() == Designation.PAYROLL_MANAGER) {
                runPayrollMenu(loggedInUser);
            }
        }
    }

    private void runOperatorMenu(Employee operator) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=== Operator Menu — " + operator.getName() + " [" + operator.getRole() + "] ===");
            System.out.println("1. Start warehouse shift");
            System.out.println("2. Resume last shift");
            System.out.println("3. View my shift summary");
            System.out.println("4. View my payslip");
            System.out.println("5. Logout");
            System.out.print("> ");

            String choice = SCANNER.nextLine().trim();
            switch (choice) {
                case "1": handleStartShift(operator); break;
                case "2": handleResumeShift(operator); break;
                case "3": operator.getShiftSummary().printSummary(); break;
                case "4": handleViewPayslip(operator); break;
                case "5": loggedIn = false; break;
                default: System.out.println("Invalid input."); break;
            }
        }
    }

    private void runSupervisorMenu(Supervisor supervisor) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=== Supervisor Menu — " + supervisor.getName() + " [" + supervisor.getRole() + "] ===");
            System.out.println("1. Start warehouse shift");
            System.out.println("2. Resume last shift");
            System.out.println("3. View my shift summary");
            System.out.println("4. View my payslip");
            System.out.println("5. View all reportees' shift summary");
            System.out.println("6. Logout");
            System.out.print("> ");

            String choice = SCANNER.nextLine().trim();
            switch (choice) {
                case "1": handleStartShift(supervisor); break;
                case "2": handleResumeShift(supervisor); break;
                case "3": supervisor.getShiftSummary().printSummary(); break;
                case "4": handleViewPayslip(supervisor); break;
                case "5":
                    for (Employee teamMember : supervisor.getReportees()) {
                        System.out.println("\nEmployee Id: " + teamMember.getId() + ", Employee Name: " + teamMember.getName() + ", Designation: " + teamMember.getRole());
                        teamMember.getShiftSummary().printSummary();
                    }
                    break;
                case "6": loggedIn = false; break;
                default: System.out.println("Invalid input."); break;
            }
        }
    }

    private void runPayrollMenu(Employee manager) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=== Payroll Manager Menu — " + manager.getName() + " [" + manager.getRole() + "] ===");
            System.out.println("1. View all employees' shift summary");
            System.out.println("2. Generate payslips");
            System.out.println("3. View all generated payslips");
            System.out.println("4. Logout");
            System.out.print("> ");

            String choice = SCANNER.nextLine().trim();
            switch (choice) {
                case "1": handleViewAllSummaries(); break;
                case "2": handleGeneratePayslips(); break;
                case "3": handleViewAllPayslips(); break;
                case "4": loggedIn = false; break;
                default: System.out.println("Invalid input."); break;
            }
        }
    }

    private void handleViewAllSummaries() {
        for (Employee emp : this.employees.values()) {
            System.out.println("\nEmployee Id: " + emp.getId() + ", Employee Name: " + emp.getName() + ", Designation: " + emp.getRole());
            emp.getShiftSummary().printSummary();
        }
    }

    private void handleGeneratePayslips() {
        this.payslipsGeneratedForSession = true;
        System.out.println("Payslips generated successfully.");
    }

    private void handleViewAllPayslips() {
        try {
            if (!this.historicalPayslipsExist && !this.payslipsGeneratedForSession) {
                throw new exceptions.NotFoundException("Payslip not generated yet.");
            }

            for (Employee emp : this.employees.values()) {
                System.out.println("\nEmployeeID: " + emp.getId());
                System.out.println("Employee Name: " + emp.getName());
                System.out.printf("Base salary: %.2f\n", emp.getBaseSalary());
                System.out.printf("Net Salary: %.2f\n", emp.calculateNetSalary());
                System.out.println("=======================");
            }
            
        } catch (exceptions.NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleStartShift(Employee emp) {
        if (this.shiftComplete) {
            System.out.println("All shelves in the warehouse are empty and nothing to deliver. Returning to main menu.");
            return;
        }
        this.shiftStarted = true;
        runFloorSelectionLoop(emp);
    }

    private void handleResumeShift(Employee emp) {
        if (!this.shiftStarted) {
            System.out.println("Shift not started, cannot resume shift.");
            return;
        }
        if (this.shiftComplete) {
            System.out.println("All shelves in the warehouse are empty and nothing to deliver. Returning to main menu.");
            return;
        }
        runFloorSelectionLoop(emp);
    }

    private void runFloorSelectionLoop(Employee emp) {
        boolean inShift = true;
        while (inShift) {
            this.map.printCurrentState(); 
            System.out.print("Enter a floor number to navigate the warehouse or X to return to the main menu : ");
            String input = SCANNER.nextLine().trim();

            if (input.equalsIgnoreCase("X")) {
                inShift = false;
                continue;
            }

            try {
                int floorNum = Integer.parseInt(input);
                if (this.map.getForkliftForFloor(floorNum) != null) {
                    runMovementLoop(floorNum, emp); 
                } else {
                    System.out.println("Invalid Input");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
            }
        }
    }

    private void runMovementLoop(int floorNum, Employee emp) {
        boolean onFloor = true;
        
        while (onFloor) {
            this.map.printSingleFloor(floorNum);

            System.out.println("Enter direction:");
            System.out.println("U - Up.");
            System.out.println("D - Down.");
            System.out.println("L - Left.");
            System.out.println("R - Right.");
            System.out.println("T - Deliver carried item at START (O).");
            System.out.println("Q - Quit to main menu.");
            System.out.print("> ");

            String direction = SCANNER.nextLine().trim().toUpperCase();

            if (direction.equals("Q")) {
                System.out.println("Shift paused.");
                onFloor = false;
            } else {
                System.out.println("Movement direction logged: " + direction);
            }
        }
    }

    private void handleViewPayslip(Employee emp) {
        try {
            boolean isGenerated = this.payslipsGeneratedForSession; 
            if (!isGenerated) {
                throw new NotFoundException("Employee " + emp.getId() + "'s payslip not found.");
            }
            System.out.println("EmployeeID: " + emp.getId());
            System.out.println("Net Salary: " + emp.calculateNetSalary());
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}