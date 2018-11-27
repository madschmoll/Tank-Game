/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.LinkedList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 *
 * @author madelineschmoll
 */
public class BulletControl implements Drawable, Collidable{
    
    private LinkedList<Bullet> bullets = new LinkedList<>();
    
    Bullet currBullet;
    
    public void update(){
        for(int i = 0; i < bullets.size(); i++){
            currBullet = bullets.get(i);
            currBullet.update();
            
        }
    }
    
    public LinkedList<Bullet> getBulletList(){
        return bullets;
    }
    
    @Override
    public void draw(Graphics2D g, ImageObserver obs){
        for(int i = 0; i < bullets.size(); i++){
            currBullet = bullets.get(i);
            if(currBullet.getVisible()){
                currBullet.draw(g, obs);
            }
        }
    }
    
    @Override
    public void collisionEffect(Collidable obj){
        for(int i = 0; i < bullets.size(); i++){
            currBullet = bullets.get(i);
            currBullet.collisionEffect(obj);
        } 
    }
    
    @Override
    public Rectangle getRectangle(){
        return null;
    }
    
    public Rectangle getRectangle(int i){
        return bullets.get(i).getRectangle();
        
        //for(int i = 0; i < bullets.size(); i++){
        //    currBullet = bullets.get(i);
        //    currBullet.getRectangle();
        //}
    }
    
    public void addBullet(Bullet b){
        bullets.add(b);
    }
    
    public void removeBullet(int i){
        bullets.remove(i);
    }
    
    public Bullet get(int i){
        return bullets.get(i);
    }
    
    public int size(){
        return bullets.size();
    }
}