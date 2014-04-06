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
    private final ArrayList<String> words;
    private final transitionMap transitionFunction;
    
    Automaton(String fileName) throws IOException           
    {
        states = new TreeSet();
        alphabet = new TreeSet();
        initialStates = new TreeSet();
        finalStates = new TreeSet();
        words = new ArrayList();
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
            
            output.println(";\n");
             
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
            
            
            output.println(";\n");
            for (String ii: initialStates)
            {
                output.print(ii + " ");
            }
            output.println(";");
            for (String ii: finalStates)
            {
                output.print(ii + " ");
            }
            output.println(";\n");
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
      
        for (String initialState: initialStates)
        {
            if (matches(word, initialState))
                return true;
        }
           
        return false;
    }
    
    public boolean matches(String word, String currState)
    {
         return recursiveAux(word, currState);
    }
    
    private boolean recursiveAux(String word, String currState)
    {
       if (word.length()==0)
       {
           if (isFinalState(currState)) return true;  
             return false;              
       }
       else
       {
           String currSymbol = word.substring(0,1);
           
           for (String nextState: reachableStates(currState,currSymbol))
           {
               if (matches(word.substring(1), nextState))
               {
                   return true;
               }
           }
       }
       return false;
    }
    
    
    /**
     * Returns a set of states that can be directly reached
     * from the given state and the given transition
     * @param state state whill will be analyzed
     * @param symbol symbol that must be taken in order to reach another state
     * @return set os reachable states
     */
    private TreeSet<String> reachableStates(String state, String symbol)
    {
        TreeSet<String> result;
        result = transitionFunction.get(new Tupla(state, symbol));
        if (result == null)
            result = new TreeSet();
        return result;
    }
    
    /**
     * This function converts a GNFA into a regular NFA
    */
    public void convertToRegular()
    {
        //TODO - iterate through the transitionFunction TreeMap,
        //create new states if a transition word has more than one
        //character, and somehow deal with the lambda transitionFunction
        
         expandStates();
         expandLambda();
         
        
    }
    
    
    private void expandLambda()
    {
        String currState, transition;
        TreeSet<String> nextStates = new TreeSet();
        TreeSet<Tupla> stateAndTransition;
        transitionMap newMap = new transitionMap();
        while (hasLambdaTransitions(this.transitionFunction))
        {
            
            newMap.putAll(transitionFunction);
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
                            initialStates.add(nextState);
                        }


                        stateAndTransition = originStates(transitionFunction, currState);
                        for (Tupla newTransition: stateAndTransition)
                        {
                            newMap.addTransition(newTransition.getState(), newTransition.getWord(), 
                                    nextState);
                        }

                    }
                    newMap.remove(new Tupla(currState, transition));
                    it.remove();
                }
            }

            transitionFunction.putAll(newMap);
            newMap.clear();
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
    
  
    private boolean hasLambdaTransitions(transitionMap map)
    {
        boolean result = false;
        
        for (Map.Entry<Tupla, TreeSet<String>> entry: map.entrySet())
        {
            if (entry.getKey().getWord().contentEquals("v"))
                return true;
        }
        
        return result;
    }
           
    
    
    /**
     * Auxiliary function for the expansion of states when converting a GNFA to 
     * a regular  NFA
     */
    private void expandStates()
    {
        String currState, transition, newState, oldState, currSymbol;
        Integer numSymbols, ii;
        StateArray numNewStates = new StateArray();
        numNewStates.initializeArray(states.size());
        transitionMap newMap = new transitionMap();
        newMap.putAll(transitionFunction);
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
                numSymbols = transition.length();
                
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
                newMap.remove(new Tupla(currState, transition));
                it.remove();
                
            }
            
           
            
        }
        
         this.transitionFunction.putAll(newMap);
         
        
        
    }
    

            
    public void printSets()
            /* Prints to std output the properties of the FSM. 
            For debbuging purposes */
    {
        System.out.println(states);
        System.out.println(alphabet);
        System.out.println(transitionFunction);
    }
    
    
    
    private void readAux(Scanner s, Collection<String> a, boolean isTransition)
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
 
    private boolean isFinalState(String state)
    {
        return (finalStates.contains(state));
    }

    
    
}