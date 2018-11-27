/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 * @author madelineschmoll
 */
public class TankGameEvents extends Observable {
    int eventType;
    final int inputKey = 1;
    final int collision = 2; // can I change this
    int collisionDamage; 
    
    Object event; 
    
    public void setValue(String message){
        event = message;
        eventType = collision;
        setChanged();
        notifyObservers(this);    
    }
    
    public void setValue(KeyEvent e){
        eventType = inputKey;
        event = e;
        setChanged();
        notifyObservers(this);    
    }
    
    public Object getEvent(){
        return event;
    }
        
    public int getType(){
        return eventType;
    }
    
    
}
