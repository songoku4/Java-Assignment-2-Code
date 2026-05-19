package src.engine;

import java.util.Scanner;

/**
You can modify any code in this class including the existing method signatures present.
*/
public class WarehouseManagerEngine {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        WarehouseManagerEngine engine = new WarehouseManagerEngine();
        //Step 1: Command line args
        //Validate all the cmd args except file names. if success then create the warehouse map using floor, row, cols
        // if validation fails, print message, return false and terminate.

        //Step 2: File Reading, fir file not found exception print message and terminate. for all other kind skip lines

        //Step 3: Main Menu Loop
        //Step 4: Write the payslips data before terminating the program.
        engine.exitProgram();
    }

    private void exitProgram(){
        //TODO: Write the payslips data at the end.

        System.out.println("Goodbye!");

    }

    private boolean validateArgs(String[] args) {
        //TODO: validate the args
        return true;
    }

    private void loadFiles(String[] args) throws IOException {
        //TODO: load files

    }

    private void runMainMenuLoop() throws IOException {
        Messages.printWelcomeA2();
       //Run main menu loop based on employee selection
    }

    
    //TODO: Create other methods
}
