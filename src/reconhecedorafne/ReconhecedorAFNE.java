/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reconhecedorafne;

import java.io.*;
import java.util.*;

/**
 *
 * @author Marlon.
 */
public class ReconhecedorAFNE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException   {
        // TODO code application logic here
        
        try {
        Automaton fsm = new Automaton(args[0]);
        fsm.printSets();
        fsm.processOutput(args[1]);
        
        
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println("ArrayIndexOutOfBoundsException was thrown. Please make sure that the input and"
                    + " output files are being passed as arguments.\n Ex: ./program input.txt output.txt");
        }
        
        
        //PrintWriter output = new PrintWriter(new FileWriter(args[1]));
        
   
        System.out.println("Hello World!");
        
    
    }
    
    
   
    
    

    
    
 
    
}
