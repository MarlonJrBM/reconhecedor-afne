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
        
        Automaton fsm = new Automaton(args[0]);
        fsm.printSets();
        fsm.processOutput(args[1]);
        
        
        //PrintWriter output = new PrintWriter(new FileWriter(args[1]));
        
   
        System.out.println("Hello World!");
        
    
    }
    
    
   
    
    

    
    
 
    
}
