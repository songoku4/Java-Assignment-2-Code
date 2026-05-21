package engine;

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
    // Tracks if Option 2 has been pressed yet
    private boolean payslipsGeneratedForSession = false; 
    
    // Tracks if we actually loaded historical data from payslips.csv at startup
    private boolean historicalPayslipsExist = false;
    private boolean shiftComplete = false;

    public static void main(String[] args) {
        // 1. Check for exactly 5 arguments
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

            // 2. Validate bounds
            if (floors < 1 || floors > 3) {
                System.out.println("Error: Number of floors has to be between 1 and 3.");
                return;
            }
            if (rows < 4 || cols < 4) {
                System.out.println("Error: Rows and columns must be at least 4 to allow proper map layout.");
                return;
            }

            // 3. Verify files exist
            File mFile = new File(mapFile);
            File eFile = new File(empFile);
            if (!mFile.exists() || !eFile.exists()) {
                System.out.println("Unable to process file. Exiting program.");
                return;
            }

            // 4. Boot up the system
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
        // If payslips.csv exists, this is where you would load it. 
        // For now, we proceed to login.

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
                case "1": handleStartShift(); break;
                case "2": handleResumeShift(); break;
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
                case "1": handleStartShift(); break;
                case "2": handleResumeShift(); break;
                case "3": supervisor.getShiftSummary().printSummary(); break;
                case "4": handleViewPayslip(supervisor); break;
                case "5": 
                    for (Employee teamMember : supervisor.getReportees()) {
                        System.out.println("\nEmployee Id: " + teamMember.getId() + 
                                           ", Employee Name: " + teamMember.getName() + 
                                           ", Designation: " + teamMember.getRole());
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
                case "1": 
                    handleViewAllSummaries(); 
                    break;
                case "2": 
                    handleGeneratePayslips(); 
                    break;
                case "3": 
                    handleViewAllPayslips(); 
                    break;
                case "4": 
                    loggedIn = false; 
                    break;
                default: 
                    System.out.println("Invalid input."); 
                    break;
            }
        }
    }

private void handleViewAllSummaries() {
        // Loop through the LinkedHashMap to preserve the file order!
        for (Employee emp : this.employees.values()) {
            System.out.println("\nEmployee Id: " + emp.getId() + 
                               ", Employee Name: " + emp.getName() + 
                               ", Designation: " + emp.getRole());
            
            emp.getShiftSummary().printSummary();
        }
    }

    private void handleGeneratePayslips() {
        // Simply flip the flag. The actual math happens dynamically when we print or save.
        this.payslipsGeneratedForSession = true;
        System.out.println("Payslips generated successfully.");
    }

    private void handleViewAllPayslips() {
        try {
            // Scenario 3: No file exists AND they haven't pressed Option 2 yet
            if (!this.historicalPayslipsExist && !this.payslipsGeneratedForSession) {
                throw new exceptions.NotFoundException("Payslip not generated yet.");
            }

            // Scenario 1 & 2: We either have old data or new data. 
            // In a simple architecture, we just print the live calculated data.
            for (Employee emp : this.employees.values()) {
                System.out.println("\nEmployeeID: " + emp.getId());
                System.out.println("Employee Name: " + emp.getName());
                
                // Formatting to 2 decimal places exactly as shown in the spec
                System.out.printf("Base salary: %.2f\n", emp.getBaseSalary());
                
                // Because calculating the exact breakdown (Delivery Pay, Penalties) 
                // requires knowing the employee's role, we can add a printPayslipFormat() 
                // method inside the Employee classes to keep the Engine clean, 
                // OR just print the Net Salary here.
                
                System.out.printf("Net Salary: %.2f\n", emp.calculateNetSalary());
                System.out.println("=======================");
            }
            
        } catch (exceptions.NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleStartShift() {
        if (this.shiftComplete) {
            System.out.println("All shelves in the warehouse are empty and nothing to deliver. Returning to main menu.");
            return;
        }
        this.shiftStarted = true;
        runFloorSelectionLoop();
    }

    private void handleResumeShift() {
        if (!this.shiftStarted) {
            System.out.println("Shift not started, cannot resume shift.");
            return;
        }
        if (this.shiftComplete) {
            System.out.println("All shelves in the warehouse are empty and nothing to deliver. Returning to main menu.");
            return;
        }
        runFloorSelectionLoop();
    }

    private void runFloorSelectionLoop() {
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
                    System.out.println("Moving to floor " + floorNum + " (Movement logic to be implemented)");
                    // runMovementLoop(floorNum);
                } else {
                    System.out.println("Invalid Input");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
            }
        }
    }

    private void handleViewPayslip(Employee emp) {
        try {
            // For now, assume it hasn't been generated to trigger the correct exception
            boolean isGenerated = false; 
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