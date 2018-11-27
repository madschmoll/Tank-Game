/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 *
 * @author madelineschmoll
 */
public class Bullet extends TankGameObject implements Drawable, Collidable {
    boolean visible;
    private final int speed, damage, r;
    private int angle, vx, vy;
    private Tank owner;
    
    public Bullet(BufferedImage objImg, int x, int y, int angle, int r,Tank owner){
       super(objImg, x, y);
       this.visible = true;
       this.angle = angle;
       this.r = r;
       this.owner = owner;
       this.speed = 5;
       this.damage = 1;
       
       vx = (int) Math.round( r * Math.cos( Math.toRadians( angle ) ) );
       vy = (int) Math.round( r * Math.sin( Math.toRadians( angle ) ) );
    }
    
    public void setVisible(boolean i){
        this.visible = i;
    }
    
    public boolean getVisible(){
        return (visible && owner.isAlive());
    }
    
    public Tank getOwner(){
        return this.owner;
    }
    
    public int getDamage(){
        return damage;
    }
    
    
    public void update(){
        
        // if bullet is on screen
        if(onScreen()){
            x += vx * speed;
            y += vy * speed; 
            
        } else { // if bullet is not on screen
            this.visible = false; 
        }
        
    
    }
    
    /// GET RID OF THESE FUNCTIONS AFTER IMPLEMENTING COLLIDABLE 
    public void collisionEffect(Wall w){
        this.visible = false;
        if(w.isBreakable()){
            w.depleteWallHealth();
        }
    }
    
    public void collisionEffect(Tank t){
        if(this.owner != t){
            this.visible = false;
            t.depleteHealth();
        }
    }
    ////
    
    @Override
    public void collisionEffect(Collidable obj){
        
        if(obj instanceof Tank){
            if(((Tank)obj) !=  this.owner){
                this.visible = false;
                ((Tank)obj).depleteHealth();
            }
        }
        
        if(obj instanceof Wall){
            if(((Wall)obj).isBreakable()){
                ((Wall)obj).depleteWallHealth();
            }
            this.visible = false;
        }
    }
    
    
    @Override
    public void draw(Graphics2D g, ImageObserver obs) {
            
        g.drawImage(objImg, x, y, obs);
        
    }
    
    public boolean onScreen(){
        return ( x < 1600 && y < 1600 && x > -5 && y > -5 ); 
    } 

   
}

