/*
 * Author: Kimler Corey
 */
package simulator;

import java.util.Random;
import java.util.Scanner;

public class Menu {

    private int selected; //stores the menu selection
    private boolean showReferenceString;
    public String selectedReferenceStringBuffer;
    static final String VFRAMES = "01234567";
    static Random rnd = new Random();

    // does the whole menu logic (presentation, validation and returns reult)
    public int doMenu() {

        this.showMenu();
        int token;


        do {
            Scanner scanner = new Scanner(System.in);

            // non integers shouldn't be accespted
            while (!scanner.hasNextInt()) {
                System.out.print("\nValid selections are 0-6 (try again)> ");
                scanner.nextLine();
            }

            token = scanner.nextInt();

            //other integers shouldn't work
            if (token > 6 || token < 0) {
                System.out.print("\nSeriously? you can choose either 0,1,2,3,4,5 or 6. Everything else just ain't gonna work. (try again)\n> ");
            }

        } while (token > 6 || token < 0);

        this.selected = token;

        // return the valid selection to caller
        return this.selected;
    }

    //menu view
    public void showMenu() {

        String menu = "Memory Paging Simulation Menu:\n\n";

        // display the reference string if display is true
        if (this.showReferenceString) {
            menu += "[Current RS:" + this.selectedReferenceStringBuffer + "]\n\n";
        }

        menu += "0 – Exit\n";
        menu += "1 – Read reference string\n";
        menu += "2 – Generate reference string\n";
        menu += "3 – Toggle Display of current reference string\n";
        menu += "4 – Simulate FIFO\n";
        menu += "5 – Simulate OPT\n";
        menu += "6 – Simulate LFU\n> ";

        System.out.print(menu);
    }

    // reads in a new reference string from the user
    public void readRS() {

        System.out.println("Input the characters: ");
        Scanner input = new Scanner(System.in);
        //validates the input uses only digits 0-7
        String intext = input.next();
        if (intext.matches("^[0-7]+$")) {
            this.selectedReferenceStringBuffer = intext;
        } else {
            System.out.println("Please input only numbers between 0 and 7 becasue thats how many virtual frames there are.");
        }
        //this.selectedReferenceStringBuffer
    }

    // generates a new reference string for the user
    public void generateRS() {
        int numberofframes = 0;

        System.out.println("Enter how many frames to process (0 for default string):");

        Scanner scan = new Scanner(System.in);
        try {
            numberofframes = scan.nextInt();
        } catch (Exception e) {
            System.out.println("So wrong.");
        }

        if (numberofframes == 0) {
            this.selectedReferenceStringBuffer = "30620373207423723";
        } else if (numberofframes > 0) {
            this.selectedReferenceStringBuffer = getRandomString(numberofframes);
        } else {
            System.out.println("Just seems wrong.");
        }

    }

    // attempts to toggle the display for reference string
    public void toggleDisplayRS() {
        System.out.println("Display current reference string");
        if (this.showReferenceString == true) {
            this.showReferenceString = false;
        } else {
            this.showReferenceString = true;
        }


    }

    //blocking messages for user menu actions
    public void block(int errMessage) {

        if (errMessage == 1) {
            System.out.println("You can't view the reference string until you have one!");
        } else {
            System.out.println("Please create a reference string before doing that.");
        }
    }

    //generates a random string or returns the default string 
    public String getRandomString(int len) {

        StringBuilder gen = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            gen.append(VFRAMES.charAt(rnd.nextInt(VFRAMES.length())));
        }
        return gen.toString();
    }
}
