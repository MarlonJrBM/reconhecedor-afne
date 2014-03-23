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
    
    private final ArrayList<String> states;
    private final ArrayList<String> alphabet;
    private final ArrayList<String> initialStates, finalStates;
    private final ArrayList<String> words;
    
    private String[][] transitionTable;
    
    Automaton(String fileName) throws IOException           
    {
        states = new ArrayList();
        alphabet = new ArrayList();
        initialStates = new ArrayList();
        finalStates = new ArrayList();
        words = new ArrayList();
        processInput(fileName);
    }
    
    public void printSets()
            /* Prints to std output the properties of the FSM. 
            For debbuging purposes */
    {
        System.out.println(states);
        System.out.println(alphabet);
    }
    
    private void readAux(Scanner s, ArrayList<String> a, boolean isTransition)
    {
        String currWord, currState, currTransition;
        int row, col;
        
        if (!isTransition)
            //we are reading the states, alphabet or the words, this is more simple
        {
            while (s.hasNext()) {
                    //reads the states
                    currWord = s.next();
                    if (currWord.contentEquals(";")) break;
                    a.add(currWord);
                }
        }
        else
            //we are reading the transition table, this is more complicated
        {
            while (s.hasNext()) 
                //reads the transition function
            {
                currWord = s.next();
                if (currWord.contentEquals(";")) break;
                currState = s.next();
                currTransition = s.next();
                row = a.indexOf(currWord);
                col = a.indexOf(currState);
                transitionTable[row][col] = currTransition;
                currWord = s.next();     
            }
        }
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
        String currState = null;
        String currTransition = null;
        Scanner input = null;
        int row, col;
        
        try {
            input = new Scanner(new BufferedReader(new FileReader(fileName)));
            
            while (input.hasNext()) {
                //reads the states
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                //System.out.println(currWord);
                states.add(currWord);
            }
            
            while (input.hasNext()) {
                //reads the alphabet
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                //System.out.println(currWord);
                alphabet.add(currWord);
            }
            
            transitionTable = new String[states.size()][states.size()];
            
            while (input.hasNext()) 
                //reads the transition function
            {
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                currState = input.next();
                currTransition = input.next();
                row = states.indexOf(currWord);
                col = states.indexOf(currState);
                transitionTable[row][col] = currTransition;
                currWord = input.next();     
            }
            
            while (input.hasNext())
                //reads the initial states
            {
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                //System.out.println(currWord);
                initialStates.add(currWord);
            }
            
            while (input.hasNext())
                //reads the final states
            {
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                //System.out.println(currWord);
                finalStates.add(currWord);
            }
            
            while (input.hasNext())
                //reads the words
            {
                currWord = input.next();
                if (currWord.contentEquals(";")) break;
                //System.out.println(currWord);
                words.add(currWord);
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
            for (String ii: initialStates)
            {
                output.print(ii + " ");
            }
            output.println(";");
            for (String ii: finalStates)
            {
                output.print(ii + " ");
            }
            output.println(";");
            for (String ii: words)
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