/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Rectangle;

/**
 *
 * @author madelineschmoll
 */
public class CollisionDetector {
    TankGameEvents event1, event2; 
    
    public CollisionDetector(TankGameEvents ev1, TankGameEvents ev2){
        this.event1 = ev1;
        this.event2 = ev2;
    }
    
    // check for these collisions
    // 1 -- p1Tank vs p2Tank
    // 2 -- tank vs bullet 
    // 3 -- tank vs power up
    // 4 -- tank vs wall
    // 5 -- wall vs bullet
    
    // or 
    // tank vs tank ... tank vs obj ... wall vs bullet
    
    // 2 , 3 , 4
    // this will check collision between a tank and bullet, power up, wall
    public void checkCollision(Tank tank, TankGameObject tgObj){
        // create a rectangle based on each objects dimensions 
        Rectangle tankR = tank.getRectangle();
        Rectangle objR = tgObj.getRectangle();
        if(tankR.intersects(objR)){//check intersection
            if(tgObj instanceof Bullet){
                ((Bullet)tgObj).collisionEffect(tank);
            }
            else{  
                tank.objCollisionEffect(tgObj);

            }
        }
    }
    
    // checks collision between two tanks
    public void checkCollision(Tank tank1, Tank tank2){
        Rectangle tank1R = new Rectangle(tank1.getX(), tank1.getY(), tank1.getWidth(), tank1.getHeight());
        Rectangle tank2R = new Rectangle(tank2.getX(), tank2.getY(), tank2.getWidth(), tank2.getHeight());
        if(tank1R.intersects(tank2R)){
            tank1.objCollisionEffect(tank2);
            tank2.objCollisionEffect(tank1);
        }
    }
    
    public void checkCollision(Bullet b, Wall w){
        Rectangle bulletR = b.getRectangle();
        Rectangle wallR = w.getRectangle();
        if(bulletR.intersects(wallR)){
            w.collisionEffect(b);
            b.collisionEffect(w);
        }
    }
}
