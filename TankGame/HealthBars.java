/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 *
 * @author madelineschmoll
 */
public class HealthBars extends TankGameObject {
    private final Tank owner;
    private int healthStatus;
    
    public HealthBars(BufferedImage objImg, Tank owner) {
        super(objImg, owner.getX(), owner.getY());
        this.owner = owner;
        healthStatus = 5;
    }
    
    public int getHealthStatus(){
        return this.healthStatus;
    }
    
    public void fillHealthBars(){
        this.healthStatus = 5;
    }
    
    public void addHeath(){
        if(healthStatus < 5){
            healthStatus++;
        }
    }
    
    public void subtractHealth(){
        if(healthStatus > 0){
            healthStatus--;
        } else {
            owner.subtractLife();
            if(owner.isAlive())
                healthStatus = 5;
            
        }     
    }
    
    public void update(){
        this.x = owner.getX() - 5;
        this.y = owner.getY() + owner.getHeight() + 10 ;
    }
    
    public void draw(Graphics2D g, ImageObserver obs) {
        int drawX = x;
        for(int i = 0; i < healthStatus; i++){
            g.drawImage(objImg, drawX, y, obs);
            drawX += objImg.getWidth();
        }
    }

}
