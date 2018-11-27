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
public class Lives extends TankGameObject{
    private int lives; 
    private Tank owner;
    
    public Lives(BufferedImage objImg, Tank owner) {
        super(objImg, owner.getX(), owner.getY());
        this.owner = owner;
        this.lives = 3;
    }
    
    public int getLives(){
        return this.lives;
    }
    
    public void addLife(){
        this.lives++;
    }
    
     
    public void draw(Graphics2D g, ImageObserver obs){   
        int drawX = x;
        for(int i = 0; i < lives; i++){
            g.drawImage(objImg, drawX, y, obs);
            drawX += objImg.getWidth();
        }
    }
    
    public void update(){
        this.x = owner.getX() - 5;
        this.y = owner.getY() + owner.getHeight() + 20 ;
    }
    
    public void subtractLife(){
        if(lives > 0){
            lives--;
            if(lives == 0){
            owner.kill();
            }
        } else {
            owner.kill();
        }
        
    }
    
}
