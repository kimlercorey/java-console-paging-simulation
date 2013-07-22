/*
 * Author: Kimler Corey
 * 
 */
package simulator;

import java.util.ArrayList;

/**
 *
 * @author kimler
 */
public class Pager {

    private int pframes = 4; //[0-3]
    private int vframes = 8; //[0-7]
    private String referenceString; //Sequence of pages to be read
    private int pageMethod; //0=fifo, 1=opt, 2=lru;
    private int numOfAvaiableFrames = 4;
    private int numberOfPageFaults = 0;

    //constructor
    // set the reference string and algorthm type
    public Pager(String referenceString, int pageMethod) {
        this.referenceString = referenceString;
        this.pageMethod = pageMethod;
        this.describe();
    }

    //describe - mostly for debugging
    private void describe() {
        System.out.println("START: type " + pageMethod + "/Paging on " + this.referenceString);
    }

    //executes the solution
    public void run() {

        switch (this.pageMethod) {
            case 0: // fifo
                fifo(this.referenceString);
                break;

            case 1: // opt    
                opt(this.referenceString);
                break;

            case 2: // lru
                lfu(this.referenceString);
                break;

            default:
        }
        System.out.println("**************** RESULTS ********************************************");
        System.out.println("\t" + "Simulation complete: " + this.referenceString.length() + " pages processed.");
        System.out.println("\t" + "Total faults: " + this.numberOfPageFaults);
        System.out.println("*********************************************************************");

    }

//  FIFO algorithm using the stored reference 
    @SuppressWarnings("empty-statement")
    void fifo(String rs) {
        // Set up array for physical frames
        ArrayList physicalFrames = new ArrayList();
        physicalFrames.add(-1);
        physicalFrames.add(-1);
        physicalFrames.add(-1);
        physicalFrames.add(-1);

        int loopFault;
        int victimFrame;
        //for each vframe (character in string) referenced to process 
        for (int i = 0; i < this.referenceString.length(); i++) {

            loopFault = 0;
            victimFrame = -1;

            //covnvert current ref to int 
            int currentPage = Integer.parseInt(Character.toString(this.referenceString.charAt(i)));
            Integer integerCurrentPage = new Integer(currentPage);

            System.out.println("Processing frame " + currentPage + " (frame " + i + " of " + this.referenceString.length() + ")"); //info

            //If there are null pframes add to that (also make sure it isn't already in there)
            if (this.numOfAvaiableFrames > 0 && (!physicalFrames.contains(integerCurrentPage))) {
                //System.out.println( "ADD ITEM TO OPEN PHY FRAME ");

                //push simulate
                physicalFrames.set(3, physicalFrames.get(2));
                physicalFrames.set(2, physicalFrames.get(1));
                physicalFrames.set(1, physicalFrames.get(0));
                physicalFrames.set(0, currentPage);

                loopFault = 1; //for display processing
                this.numberOfPageFaults++; //track these
                this.numOfAvaiableFrames--;

            } else {
                // either do nothing if there is a match

                if (physicalFrames.indexOf(currentPage) < 0) {
                    // or No Match, time to make a victim and pagefault

                    victimFrame = Integer.parseInt(physicalFrames.get(3).toString());
                    physicalFrames.set(3, physicalFrames.get(2));
                    physicalFrames.set(2, physicalFrames.get(1));
                    physicalFrames.set(1, physicalFrames.get(0));
                    physicalFrames.set(0, currentPage);

                    loopFault = 1; //for display processing
                    this.numberOfPageFaults++;
                }
            }

            //do a printout    
            System.out.print("Ref String pg: " + currentPage + " |" + physicalFrames.toString().replaceAll("-1", "-"));
            if (loopFault == 1) {
                System.out.print(" PAGE FAULT ");
            }
            if (victimFrame != -1) {
                System.out.print(" VICTIM " + victimFrame);
            }
            System.out.print("\n---------------------------------- (press enter to continue)\n");
            //System.out.print("PHYSICAL FRAMES:"+ physicalFrames.get(0).toString()+physicalFrames.get(1).toString());
            try {
                System.in.read();
            } catch (Exception e) {
            };


        } //end for each ref


    }//--------------------------------------------------------

// OPT algorithm using the stored reference string
    @SuppressWarnings("empty-statement")
    void opt(String rs) {
        // Set up array for physical frames
        ArrayList physicalFrames = new ArrayList();

        int[] futureUseBit = new int[4];
        futureUseBit[0] = 0;
        futureUseBit[1] = 0;
        futureUseBit[2] = 0;
        futureUseBit[3] = 0;

        int loopFault;
        int victimFrame;
        //for each vframe (character in string) referenced to process 
        for (int i = 0; i < this.referenceString.length(); i++) {

            loopFault = 0;
            victimFrame = -1;

            //covnvert current ref to int 
            int currentPage = Integer.parseInt(Character.toString(this.referenceString.charAt(i)));
            Integer integerCurrentPage = new Integer(currentPage);

            System.out.println("Processing frame " + currentPage + " (frame " + i + " of " + this.referenceString.length() + ")"); //info

            //If there are null pframes add to that (also make sure it isn't already in there)
            if (this.numOfAvaiableFrames > 0 && (!physicalFrames.contains(integerCurrentPage))) {

                for (int c = 0; c < physicalFrames.size(); c++) {
                    if (Integer.parseInt(physicalFrames.get(c).toString()) >= 0) {
                        //sinceUseBit[c]+=1;
                    }
                }

                physicalFrames.add(currentPage);
                int addedat = physicalFrames.indexOf(currentPage);

                loopFault = 1; //for display processing
                this.numberOfPageFaults++; //track these
                this.numOfAvaiableFrames--;


            } else {
                // either do nothing if there is a match
                // determine number of future uses for each item in "physical frame" - pf   
                for (int pf = 0; pf < physicalFrames.size(); pf++) {

                    int numOfFutureUses = 0;

                    for (int rin = i; rin < this.referenceString.length(); rin++) {

                        if (Integer.parseInt(physicalFrames.get(pf).toString())
                                == Integer.parseInt(Character.toString(this.referenceString.charAt(rin)))) {
                            numOfFutureUses += 1;
                        }
                    }
                    futureUseBit[pf] = numOfFutureUses;
                }


                if (physicalFrames.indexOf(currentPage) < 0) {
                    // or No Match, time to make a victim and pagefault

                    int leastUsefulElement = 0;
                    int smallest = 7;
                    for (int c = 0; c < physicalFrames.size(); c++) {

                        if (futureUseBit[c] < smallest) {
                            leastUsefulElement = c;
                            smallest = futureUseBit[c];

                        }
                    }
                    victimFrame = Integer.parseInt(physicalFrames.get(leastUsefulElement).toString());
                    physicalFrames.set(leastUsefulElement, currentPage);

                    loopFault = 1; //for display processing
                    this.numberOfPageFaults++;
                }
            }

            //System.out.println("futureUseBit " + futureUseBit[0] + futureUseBit[1] + futureUseBit[2] + futureUseBit[3]);
            //do a printout    
            System.out.print("Ref String pg: " + currentPage + " |" + physicalFrames.toString().replaceAll("-1", "-"));
            if (loopFault == 1) {
                System.out.print(" PAGE FAULT ");
            }
            if (victimFrame != -1) {
                System.out.print(" VICTIM " + victimFrame);
            }
            System.out.print("\n---------------------------------- (press ENTER to continue)\n");
            //System.out.print("PHYSICAL FRAMES:"+ physicalFrames.get(0).toString()+physicalFrames.get(1).toString());
            try {
                System.in.read();
            } catch (Exception e) {
            };


        } //end for each ref


    }//--------------------------------------------------------

//LFU algorithm using the stored reference string
    @SuppressWarnings("empty-statement")
    void lfu(String rs) {
        // Set up array for physical frames
        ArrayList physicalFrames = new ArrayList();

        int[] sinceUseBit = new int[4];
        sinceUseBit[0] = 0;
        sinceUseBit[1] = 0;
        sinceUseBit[2] = 0;
        sinceUseBit[3] = 0;

        int loopFault;
        int victimFrame;
        //for each vframe (character in string) referenced to process 
        for (int i = 0; i < this.referenceString.length(); i++) {

            loopFault = 0;
            victimFrame = -1;

            //covnvert current ref to int 
            int currentPage = Integer.parseInt(Character.toString(this.referenceString.charAt(i)));
            Integer integerCurrentPage = new Integer(currentPage);

            System.out.println("Processing frame " + currentPage + " (frame " + i + " of " + this.referenceString.length() + ")"); //info

            //If there are null pframes add to that (also make sure it isn't already in there)
            if (this.numOfAvaiableFrames > 0 && (!physicalFrames.contains(integerCurrentPage))) {

                physicalFrames.add(currentPage);
                int addedat = physicalFrames.indexOf(currentPage);
                sinceUseBit[addedat] += 1;

                loopFault = 1; //for display processing
                this.numberOfPageFaults++; //track these
                this.numOfAvaiableFrames--;


            } else {
                // either do nothing if there is a match
                int addedat = physicalFrames.indexOf(currentPage);

                try {
                    sinceUseBit[addedat] += 1;
                } catch (Exception e) {
                }

                if (physicalFrames.indexOf(currentPage) < 0) {
                    // or No Match, time to make a victim and pagefault

                    int leastFrequentElement = 0;
                    int smallest = 7;
                    for (int c = 0; c < physicalFrames.size(); c++) {

                        if (sinceUseBit[c] < smallest) {
                            leastFrequentElement = c - 1;
                        }
                    }
                    victimFrame = Integer.parseInt(physicalFrames.get(leastFrequentElement).toString());
                    physicalFrames.set(leastFrequentElement, currentPage);
                    sinceUseBit[leastFrequentElement] = 0;

                    loopFault = 1; //for display processing
                    this.numberOfPageFaults++;
                }
            }

            //do a printout    
            System.out.print("Ref String pg: " + currentPage + " |" + physicalFrames.toString().replaceAll("-1", "-"));
            if (loopFault == 1) {
                System.out.print(" PAGE FAULT ");
            }
            if (victimFrame != -1) {
                System.out.print(" VICTIM " + victimFrame);
            }

            //Press Enter
            System.out.print("\n---------------------------------- (press ENTER to continue)\n");
            try {
                System.in.read();
            } catch (Exception e) {
            };


        } //end for each ref


    }//--------------------------------------------------------
}
