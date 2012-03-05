/*
 * Card.java
 *
 * Created on October 16, 2004, 3:55 PM
 */

package Uno;

import java.applet.*;
import java.awt.*;

/**
 *
 * @author  hamu
 */
class Card extends java.awt.Button {
    
    private int number = 0;
    
    private boolean selected = false;
    
    /* Special values for Number */
    
    public static final int SKIP = -1;
    
    public static final int PLUS2 = -2;
    
    public static final int REVERSE = -3;
    
    public static final int PLUS4 = -4;
    
    public static final int JOKER = -5;

    public static final int UNO = -6;   // only for the first card
    
    private java.awt.Color colour;
    
    public final static java.awt.Color colours[] = {
        java.awt.Color.BLUE, java.awt.Color.GREEN, 
        java.awt.Color.RED, java.awt.Color.ORANGE};

        
/////** Creates a new instance of Card *////////////////////////////////////////
    public Card(int num, java.awt.Color col) {
        super();
        init(num, col);
    }
////////////////////////////////////////////////////////////////////////////////    
    private void init(int num, java.awt.Color col) {
        number = num;
        colour = col;
        setForeground(colour);
        setBackground(java.awt.Color.GRAY);
        switch(number)
        {
            case SKIP:
                setLabel("SKIP!"); break;
            case PLUS2:
                setLabel("+2"); break;
            case REVERSE:
                setLabel("REVERSE!"); break;
            case PLUS4:
                setForeground(colour = java.awt.Color.CYAN);
                setLabel("+4"); break;
            case JOKER:
                setForeground(colour = java.awt.Color.CYAN);
                setLabel("JOKER!"); break;
            default:
                setLabel(Integer.toString(number));
        }
    }
////////////////////////////////////////////////////////////////////////////////    
    public void setSelected(boolean yes) {
        if(yes && !selected)
            setBackground(java.awt.Color.WHITE);
        else if(!yes && selected)
            setBackground(java.awt.Color.GRAY);
        selected = yes;
    }
////////////////////////////////////////////////////////////////////////////////    
    public void setEqual(Card what) {
        init(what.number, what.colour);
    }
////////////////////////////////////////////////////////////////////////////////
    public void setColour(int indx) {
        if(colour == java.awt.Color.CYAN && indx >= 0 && indx <= 3)
            colour = colours[indx];
        setForeground(colour);
    }
////////////////////////////////////////////////////////////////////////////////    
    public boolean canBePutOn(Card what) {
//        java.lang.System.out.println(this+" and "+what);
//        java.lang.System.out.println(Integer.toString(number)+" "+colour+" vs "+Integer.toString(what.number)+" "+what.colour);
        return (what.colour == colour || what.number == number || colour == java.awt.Color.CYAN || what.number == UNO);
    }
////////////////////////////////////////////////////////////////////////////////    
    public boolean isSelected() {
        return selected;
    }
////////////////////////////////////////////////////////////////////////////////
    public int getNumber() {
        return number;
    }
}
