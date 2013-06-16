package simulator;

/**
*
* @author kimler (kc) Corey
*/

public class Simulator {
    
    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        
        //init area
        
        // Selected menu item
        int sel;
        //new menu and user input
        Menu menu = new Menu();
        
        do {
            sel = menu.doMenu();
            
            switch(sel)
            {
                case 0: // 0 – Exit
                System.out.println("bye!!");
                break;
                
                case 1: // 1 – Read reference string
                menu.readRS();
                break;
                
                case 2: // 2 – Generate reference string
                menu.generateRS();
                break;
                
                case 3: // 3 – Display current reference string
                if (menu.selectedReferenceStringBuffer==null) {menu.block(1); break;}
                menu.toggleDisplayRS();
                break;
                
                case 4: // 4 – Simulate FIFO
                if (menu.selectedReferenceStringBuffer==null) {menu.block(2); break;}
                Pager fifo = new Pager (menu.selectedReferenceStringBuffer,0);
                fifo.run();
                break;
                
                case 5: // 5 – Simulate OPT
                if (menu.selectedReferenceStringBuffer==null) {menu.block(2); break;}
                Pager opt = new Pager (menu.selectedReferenceStringBuffer,1);
                opt.run();
                break;
                
                case 6: // 6 – Simulate LFU
                if (menu.selectedReferenceStringBuffer==null) {menu.block(2); break;}
                Pager lfu = new Pager (menu.selectedReferenceStringBuffer,2);
                lfu.run();
                break;
                
                default: System.out.println("Wow! no clue how anyone could every get here.");
            }
            System.out.println("-----------------------------------------------");
        } //end do
        while (sel != 0);
        
        //String menu = "menu:\n";   
    }
}