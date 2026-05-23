package utils;

import engine.WarehouseMap;
import entities.*;
import enums.CellType;
import enums.Designation;
import exceptions.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class FileManager {

    public static Map<String, Employee> loadEmployees(String filename) {
        Map<String, Employee> loadedEmployees = new LinkedHashMap<>();

        try (Scanner fileReader = new Scanner(new File(filename))) {
            int lineNumber = 1;
            
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine().trim();
                
                if (line.isEmpty() || lineNumber == 1) {
                    lineNumber++;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    Employee emp = validateAndCreateEmployee(data, lineNumber);
                    loadedEmployees.put(emp.getId(), emp);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                lineNumber++;
            }
        } catch (Exception e) {
            System.out.println("Unable to process file. Exiting program.");
            System.exit(0);
        }
        
        linkManagersAndTeams(loadedEmployees);
        return loadedEmployees;
    }

    private static Employee validateAndCreateEmployee(String[] data, int lineNum) throws Exception {
        if (data.length < 4) {
            throw new InvalidLineException("Incorrect Employees line at line " + lineNum + ". Skipping this line.");
        }

        String id = data[0].trim();
        String name = data[1].trim();
        String designationStr = data[2].trim().toUpperCase();
        double salary = Double.parseDouble(data[3].trim());
        String managerId = (data.length == 5) ? data[4].trim() : "";

        if (id.isEmpty() || name.isEmpty() || salary <= 0) {
            throw new InvalidTypeException("Incorrect Employee Details at line " + lineNum + ". Skipping the line.");
        }

        if (designationStr.equals("OPERATOR"))
        return new Operator(id, name, salary, managerId);
        if (designationStr.equals("SUPERVISOR"))
        return new Supervisor(id, name, salary, managerId);
        if (designationStr.equals("PAYROLL_MANAGER"))
        return new PayrollManager(id, name, salary, managerId);
        if (designationStr.equals("SENIOR_OPERATOR"))
        return new SeniorOperator(id, name, salary, managerId);

        throw new InvalidTypeException("Incorrect Employee Designation at line " + lineNum + ". Skipping this line.");
    }

    private static void linkManagersAndTeams(Map<String, Employee> database) {
        for (Employee emp : database.values()) {
            String bossId = emp.getManagerId();
            if (bossId != null && !bossId.isEmpty()) {
                Employee boss = database.get(bossId);
                if (boss != null && boss instanceof Supervisor) {
                    Supervisor supervisorBoss = (Supervisor) boss;
                    supervisorBoss.addTeamMember(emp);
                }
            }
        }
    }

    public static void loadWarehouseMap(String filename, WarehouseMap map, int maxFloors) {
        try (Scanner fileReader = new Scanner(new File(filename))) {
            int lineNumber = 1; 
            
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine().trim();
                
                if (line.isEmpty() || lineNumber == 1) {
                    lineNumber++;
                    continue; 
                }

                try {
                    String[] data = line.split(",");
                    validateAndPlaceWarehouseCell(data, lineNumber, map, maxFloors);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                lineNumber++;
            }
        } catch (Exception e) {
            System.out.println("Unable to process file. Exiting program.");
            System.exit(0);
        }
    }

    private static void validateAndPlaceWarehouseCell(String[] data, int lineNum, WarehouseMap map, int maxFloors) throws Exception {
        if (data.length != 6) {
            throw new InvalidWarehouseException("Invalid Warehouse line at line " + lineNum + ". Skipping this line.");
        }

        int floor = Integer.parseInt(data[0].trim());
        int row = Integer.parseInt(data[1].trim());
        int col = Integer.parseInt(data[2].trim());
        String cellTypeStr = data[3].trim().toUpperCase();

        if (floor <= 0 || floor > maxFloors) {
            throw new InvalidWarehouseException("Invalid floor number in warehouse file: " + lineNum + ". Skipping this line.");
        }

        if (row <= 0 || row >= map.getTotalRows() - 1 || col <= 0 || col >= map.getTotalCols() - 1) {
            throw new InvalidLocationException("Invalid location in warehouse file at line " + lineNum + ". Skipping this line.");
        }

        if (!cellTypeStr.equals("RESTRICTED") && !cellTypeStr.equals("SHELF")) {
            throw new InvalidTypeException("Invalid cell type at line " + lineNum + ". Skipping this line.");
        }
        
        CellType type = CellType.valueOf(cellTypeStr);
        map.getCell(floor, row, col).setType(type);
    }

    public static void writePayslips(String filepath, Map<String, Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new File(filepath))) {
            writer.println("employee_id,employee_name,base_pay,delivered_item_pay,hits_penalty,restricted_area_penalty,reportees_managing_pay,net_salary");

            for (Employee emp : employees.values()) {
                String record = String.format("%s,%s,%.1f,%.1f,%.1f,%.1f,%.1f,%.1f",
                        emp.getId(),
                        emp.getName(),
                        emp.getBaseSalary(),
                        0.0, 0.0, 0.0, 0.0, 
                        emp.calculateNetSalary()
                );
                writer.println(record);
            }
            writer.flush(); 
        } catch (Exception e) {
            System.out.println("Could not write payslips.");
        }
    }
}