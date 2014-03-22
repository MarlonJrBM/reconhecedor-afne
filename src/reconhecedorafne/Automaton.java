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
 * @author Marlon
 */
public class Automaton {
    
    private final NavigableSet<String> states;
    private final NavigableSet<String> alphabet;
    
    Automaton(String fileName) throws IOException           
    {
        states = new TreeSet();
        alphabet = new TreeSet();
        processInput(fileName);
    }
    
    public void printSets()
            /* Prints to std output the properties of the FSM. 
            For debbuging purposes */
    {
        System.out.println(states);
        System.out.println(alphabet);
    }
    
    /**
     * This function reads the GNFA from the input file
     * and saves all the necessary info into the Automaton class
     * @param fileName name of the file which will be read
    */
    public void processInput(String fileName) throws IOException 
            //reads the input file and saves all the necessary info
            //about the FSM
    {
        String currWord = null;
        Scanner input = null;
        
        try {
            input = new Scanner(new BufferedReader(new FileReader(fileName)));
            
            while (input.hasNext()) {
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                //System.out.println(currWord);
                states.add(currWord);
            }
            
            while (input.hasNext()) {
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                //System.out.println(currWord);
                alphabet.add(currWord);
            }
            
         
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }
    
    /**
     * This function writes the result of the conversion from the GNFA to
     * a normal NFA to an output file specified by fileName. It also answers
     * if the words presented in the input are accepted by the NFA.
     * @param fileName name of the file which the output will be saved.
    */
    
    public void processOutput(String fileName) throws IOException
    {
        PrintWriter output = null;
        
        try
        {
            output = new PrintWriter(new FileWriter(fileName));
            for (String ii: states)
            {
                output.print(ii + " ");
            }
            output.println(";");
            for (String ii: alphabet)
            {
                output.print(ii + " ");
            }
            output.println(";");
        }
        finally
        {
            if (output!=null)
            output.close();
        }
        
    }
    
}