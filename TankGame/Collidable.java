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
public interface Collidable {
    public void collisionEffect(Collidable obj);
    public Rectangle getRectangle();
}
