/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 *
 * @author madelineschmoll
 */
public class Wall extends TankGameObject implements Drawable, Collidable{
    
    private final boolean breakable;
    private boolean show;
    private int wallHealth;
    
    public Wall(BufferedImage objImg, int x, int y, boolean breakable) {
        super(objImg, x, y);
        this.wallHealth = 4;
        this.breakable = breakable;
        this.show = true;
    }
    
    public boolean isBreakable(){
        return breakable;
    }
    
    public void depleteWallHealth(){
        if(wallHealth > 0)
            wallHealth--;
        else
            hide();
    }
    
    private void hide(){
        this.show = false;
    }
    
    public boolean getVisible(){
        return this.show;
    }
    
    public void update(){
        
    }
    
    public void collisionEffect(Bullet b){
        if(wallHealth > 0 && breakable){
            this.wallHealth--;
        } else if(wallHealth <= 0){
            hide();
        }
        b.setVisible(false);
    }
    
    @Override 
    public void collisionEffect(Collidable obj){
        if(breakable && wallHealth > 0){
            this.wallHealth--;
        } else if(wallHealth <= 0){
            hide();
        }
    }
    
    @Override
    public Rectangle getRectangle(){
        return (new Rectangle(x,y,getWidth(),(getHeight()*6)));
    }
    
    public void drawHorizontal(Graphics g, ImageObserver obs) {
          // horizontal wall
       if(show){
          int xNext = x;
          for(int i = 0; i < 6; i++){
            g.drawImage(objImg, xNext, y, obs);
            xNext += objImg.getWidth();
          }
       }
     }
    
    
    // only vertical walls will be used
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
          // vertical wall
        if(show){  
          int yNext = y;
          for(int i = 0; i < 6; i++){
            g.drawImage(objImg, x, yNext, obs);
            yNext += objImg.getHeight();
          }
        }
     }

   
}
