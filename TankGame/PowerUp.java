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
public class PowerUp extends TankGameObject implements Drawable, Collidable{
    private boolean visible;
    
    public PowerUp(BufferedImage objImg, int x, int y) {
        super(objImg, x, y);
        this.visible = true;
    }
    
    @Override
    public void draw(Graphics2D g, ImageObserver obs){
        if(visible)
            g.drawImage(objImg, x, y, obs);
    }
    
    @Override
    public void collisionEffect(Collidable obj){
      //  this.visible = false;
    }
    
    
    public void setVisible(boolean sh){
        this.visible = sh;
    }
    
    public boolean getVisible(){
        return this.visible;
    }
}
