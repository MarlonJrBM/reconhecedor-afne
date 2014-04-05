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
public class Tupla implements Comparable<Tupla>
{
    private String state;
    private String word;
    
    Tupla(String state, String word)
    {
        this.state = state;
        this.word = word;
    }
    
    Tupla()
    {
        this.state = "";
        this.word = "";
    }
    
    public void setState(String state)
    {
        this.state = state;
    }
    
    public void setWord(String word)
    {
        this.word = word;
    }
    
    public String getState()
    {
        return this.state;
    }
    
    public String getWord()
    {
        return this.word;
    }
    
    @Override
    public int compareTo(Tupla o)
    {
        int result = this.state.compareTo(o.state);
        if (result == 0)
        {
            result = this.word.compareTo(o.word);
        }
        
        return result;
    }
    
    @Override
    public String toString()
    {
        return (this.state + " + " + this.word + " ");
    }
}


class StateArray extends ArrayList<Integer>
{
    
    public void initializeArray(int numStates)
    {
        for (int ii=0;ii<=numStates;ii++)
        {
            this.add(1);
        }
    }
    
    
    /** Returns the next state that will be created
     * based on a current one.
     * @param index state's number 
    */
    public Integer getLatestState(int index)
    {
        Integer result = this.get(index);
        this.set(index, result + 1);
        return result;
    }
}

class transitionMap extends TreeMap<Tupla, TreeSet<String>>
{
    void addTransition(String currState, String transition, String nextState)
    {
        TreeSet < String > nextStates = new TreeSet();
        nextStates.add(nextState);
        Tupla stateWord = new Tupla(currState, transition);
        if (this.containsKey(stateWord))
                    //we already have a key, thus we must replace the current value
                {
                    nextStates.addAll(this.get(stateWord));
                    this.replace(stateWord, nextStates);
                }
                else 
                    //we don't have a key yet, so we must add a new mapping
                {
                    this.put(stateWord, nextStates);
                }
                
        
    }
}
