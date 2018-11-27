/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author madelineschmoll
 */
 abstract class TankGameObject implements Drawable{
   protected BufferedImage objImg;
   protected int x, y;
   
   public TankGameObject(BufferedImage objImg, int x,int y){
       this.x = x;
       this.y = y;
       this.objImg = objImg;
       
   }
   
   public Rectangle getRectangle(){
       return new Rectangle(this.x, this.y, this.getWidth(), this.getHeight());
   }
   
   public int getX(){
       return this.x;
   }
   
   public int getY(){
       return this.y;
   }
   
   public int getWidth(){
       return objImg.getWidth();
   }
   
   public int getHeight(){
       return objImg.getHeight();
   }
   
   public void setY(int y){
       this.y = y;
   }
  
   public void setX(int x){
       this.x = x;
   }

   
}
