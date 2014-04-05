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
    
    private final TreeSet<String> states;
    private final TreeSet<String> alphabet;
    private final TreeSet<String> initialStates, finalStates;
    private final TreeSet<String> words;
    private final transitionMap transitionFunction;
    
    Automaton(String fileName) throws IOException           
    {
        states = new TreeSet();
        alphabet = new TreeSet();
        initialStates = new TreeSet();
        finalStates = new TreeSet();
        words = new TreeSet();
        transitionFunction = new transitionMap();
        processInput(fileName);
        convertToRegular();
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
       
        Scanner input = null;
        
        try {
            input = new Scanner(new BufferedReader(new FileReader(fileName)));
            
            readAux(input,states,false);
            
            readAux(input, alphabet, false);
                      
            readAux(input, states, true);
            
            readAux(input, initialStates, false);
            
            readAux(input, finalStates, false);
            
            readAux(input, words, false);
           
                   
         
        }
        catch(FileNotFoundException e)
        {
            System.out.println("FileNotFoundException was thrown. Please verify that the input and"
                    + " output files are being passed as arguments.\n Ex: ./program input.txt output.txt");
            System.exit(1);
        }
        finally {
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
        String currState,  transition;
        TreeSet <String> nextStates;
        
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
            
            for (Map.Entry<Tupla,TreeSet<String>> entry : transitionFunction.entrySet()) 
            {
                currState = entry.getKey().getState();
                transition = entry.getKey().getWord();
                nextStates = entry.getValue();
                for (String nextState: nextStates )
                {
                    output.println(currState + " " + nextState + " " + transition + " ;");
                }
                   
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
                if (recognizeWord(ii))
                 output.println(ii + " Sim ;");
                else
                    output.println(ii + " Nao ;");
            }
            
        }
        catch(FileNotFoundException e)
        {
            System.out.println("FileNotFoundException was thrown. Please verify that the input and"
                    + " output files are being passed as arguments.\n Ex: ./program input.txt output.txt");
            System.exit(1);
        }
        finally
        {
            if (output!=null)
            output.close();
        }
        
    }
    
    
    /**
     * This function returns true is the given word is 
     * accepted by the FSM
     * @param word word which will be read
    */
    public boolean recognizeWord(String word)
    {
        //TODO - This method must read the FSM somehow and determine if the
        //word is accepted by it
        
        return true;
    }
    
    /**
     * This function converts a GNFA into a regular NFA
    */
    public void convertToRegular()
    {
        //TODO - iterate through the transitionFunction TreeMap,
        //create new states if a transition word has more than one
        //character, and somehow deal with the lambda transitionFunction
        String currState, transition, newState, oldState, currSymbol;
        Integer numSymbols, ii;
        StateArray numNewStates = new StateArray();
        numNewStates.initializeArray(states.size());
        transitionMap newMap = new transitionMap();
        TreeSet<String> nextStates;
        TreeSet<String> aux = new TreeSet();
        Tupla stateWord;
        
        for (Iterator<Map.Entry<Tupla, TreeSet<String>>> it = transitionFunction.entrySet().iterator(); it.hasNext();) 
        {
            Map.Entry<Tupla,TreeSet<String>> entry = it.next();
            currState = entry.getKey().getState();
            nextStates = entry.getValue();
            transition = entry.getKey().getWord();
            
            if (transition.length()>1)
            {
                
                expandStates(newMap,nextStates, currState, transition, numNewStates  );           
                
                it.remove();
                
            }
            
           
            
        }
        
         this.transitionFunction.putAll(newMap);
         
         
         
         
        
    }
    
    
    private void expandLambda(String currState, TreeSet<String> nextStates, String transition)
    {
        TreeSet<Tupla> stateAndTransition;
        TreeMap<Tupla,TreeSet<String>> newMap = new transitionMap();
        for (Iterator<Map.Entry<Tupla, TreeSet<String>>> it = transitionFunction.entrySet().iterator(); it.hasNext();) 
        {
            Map.Entry<Tupla,TreeSet<String>> entry = it.next();
            currState = entry.getKey().getState();
            nextStates = entry.getValue();
            transition = entry.getKey().getWord();
            
            if (transition.contentEquals("v"))
            {
                
                for (String nextState: nextStates)
                {
                    if (initialStates.contains(currState))
                    {
                        
                    }
                    
                    
                    stateAndTransition = originStates(transitionFunction, nextState);
                    
                }
            }
        }
    }
    
    /**
     * Given a state, returns a set with all the states that have a transition
     * that reach that state, and the corresponding transiction
     * @param map the transition function
     * @param s state wished to be inspected
     * @return set of all the states that reach s
     */
    private TreeSet<Tupla> originStates(TreeMap <Tupla, TreeSet<String>> map, 
            String s)
    {
        //TODO
        TreeSet<Tupla> result = new TreeSet();
        
        for (Map.Entry<Tupla, TreeSet<String>> entry: map.entrySet())
        {
           if (entry.getValue().contains(s))
           {
               result.add( new Tupla(entry.getKey().getState(), entry.getKey().getWord()));
           }
        }
        
        return result;
    }
    
  
           
    
    
    /**
     * Auxiliary function for the expansion of states when converting a GNFA to 
     * a regular  NFA
     * @param newMap
     * @param nextStates
     * @param currState
     * @param transition
     * @param numNewStates 
     */
    private void expandStates(transitionMap newMap, TreeSet<String> nextStates,
            String currState, String transition, StateArray numNewStates )
    {
        Tupla stateWord;
        Integer numSymbols = transition.length(), ii;
        String oldState, currSymbol, newState;
        TreeSet<String> aux = new TreeSet();
        for (String nextState: nextStates)
                {
                    oldState = currState;
                    for (ii=1;ii<=numSymbols;ii++)
                    {
                        currSymbol = transition.substring(ii-1, ii);
                        if (ii==numSymbols)
                            //If this is the last character of the word, the next
                            //state is the the previous ending state
                        {
                           newState = nextState;  
                        }
                        else
                            //otherwise, we need to make another state
                        {
                            newState = currState + "." + numNewStates.getLatestState(Integer.parseInt(currState))
                                    .toString();
                        }
                        newMap.addTransition(oldState, currSymbol, newState);
                        states.add(newState);          
                        oldState = newState;
                    }
                }
        
        
    }
    
    private void initializeArray(ArrayList<Integer> numNewStates)
    {
        for (int ii=0;ii<states.size()+1;ii++)
        {
            numNewStates.add(1);
        }
    }
    
    private Integer getLatestState(ArrayList<Integer> numNewStates, int index )
    {
        Integer result = numNewStates.get(index);
        numNewStates.set(index, result + 1);
        return result;
    }
            
            
    public void printSets()
            /* Prints to std output the properties of the FSM. 
            For debbuging purposes */
    {
        System.out.println(states);
        System.out.println(alphabet);
        System.out.println(transitionFunction);
    }
    
    
    
    private void readAux(Scanner s, TreeSet<String> a, boolean isTransition)
    {
        String currWord, currState, nextState, transition;
        TreeSet < String > nextStates = new TreeSet();
        Tupla stateWord;
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
                currState = s.next();
                if (currState.contentEquals(";")) break;
                nextState = s.next();
                transition = s.next();
                stateWord = new Tupla(currState,transition);
                transitionFunction.addTransition(currState, transition, nextState);
                currState = s.next();     
            }
        }
    }
    
    
    private boolean isInitialState(String state)
    {
        return (initialStates.contains(state));
    }
 

    
    
}