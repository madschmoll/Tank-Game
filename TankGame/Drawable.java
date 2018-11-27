/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/**
 *
 * @author madelineschmoll
 */
public interface Drawable {
    public void draw(Graphics2D g, ImageObserver obs);
}
