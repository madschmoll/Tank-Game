package TankGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author madelineschmoll
 */
public class Tank extends JComponent implements Observer, Drawable, Collidable {

    private int x , y, up; 
    private int livesX, livesY;
    private final int r = 6;
    private int vx, vy;
    private short angle;
    private int direction;
    private static BufferedImage tankImg, bulletImg, livesImg, healthImg;
    private boolean UpPressed, DownPressed;
    private boolean RightPressed, LeftPressed;
    private boolean shootPressed, shooting = false;
    private boolean isAlive;
    private TankGameWorld game;
    private BulletControl bc;
    private HealthBars health;
    private Lives lives; 
    
    public Tank( int x, int y, int vx, int vy, short angle, BufferedImage tankImg, BufferedImage bulletImg, BulletControl bc, TankGameWorld game){
        
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        Tank.tankImg = tankImg;
        this.isAlive = true;
        this.game = game;
        this.bulletImg = bulletImg;
        this.bc = bc;
       
        try {
            this.livesImg = ImageIO.read(new File("Resources/life.gif"));
            this.healthImg = ImageIO.read(new File("Resources/hb.gif"));
        } catch (IOException ex) {
            Logger.getLogger(Tank.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.direction = 180;
        this.lives = new Lives(livesImg, this);
        this.health = new HealthBars(healthImg, this);
        
    }
    
    
    public void subtractLife(){
        this.lives.subtractLife();
    }
    
    public boolean isAlive() {
        return this.isAlive;
    }
    
    public void kill(){
        this.isAlive = false;
    }
    
    public void depleteHealth(){
        this.health.subtractHealth();
    }
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
   
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getAngle(){
        return angle;
    }
    
    public int getHeight(){
        return tankImg.getHeight();
    }
    
    public int getWidth(){
        return tankImg.getWidth();
    }
    
    public Rectangle getRectangle(){
        return new Rectangle(x,y, this.getWidth(), this.getHeight());
    }
    
    // setters for toggling key pressed
    
    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }
    
    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void unToggleShootPressed() {
        this.shootPressed = false;
    }
    
    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public boolean isUpPressed() {
        return UpPressed;
    }

    public boolean isDownPressed() {
        return DownPressed;
    }

    // movement methods 
   
   //  move forward or backward
   
    public void move(){
        
        vx = (int) Math.round( r * Math.cos( Math.toRadians( angle ) ) );
        vy = (int) Math.round( r * Math.sin( Math.toRadians( angle ) ) );
        
        if(this.UpPressed){
            x += vx;
            y += vy;
        }
        
        if(this.DownPressed){
            x -= vx;
            y -= vy;
        }
        
        checkBorder();
        
    } 
    
    public void rotateLeft(){
        this.angle -= 5;
        checkAngle();
        turn(angle);
        
    }
    
    public void rotateRight(){
        this.angle += 5;
        checkAngle();
        turn(angle);
    }
    
    public void turn(int angle){
    	this.direction += angle;
    	if(this.direction>=360){
    		this.direction=0;
    	}
    	else if(this.direction<0){
    		this.direction=359;
    	}
    }
    
    // check border method
    private void checkBorder(){
        
        if(x < 0){
            x = 0;
        }
        if(x >= 1600){ // if x >= frame size - tank width
            x = 1600;
        }
        if(y < 0){
            y = 0;
        }
        if(y >= 1300){ // if y >= frame size - tank height
            y = 1300;
        }
    }
    
    private void checkAngle(){
        if(angle > 360){
            angle = 0;
        } if(angle < 0){
            angle = 360;
        }
    }
    
    // to String method to be used in paintComponent
    @Override
    public String toString(){
        String s = "Tank at x: " + x + ", y: " + y + ", angle: " + angle;
        return s; 
    }
    
    //@Override
    public void paintComponent(Graphics2D g) {
       if(isAlive){
            super.paintComponent(g);

            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate( Math.toRadians(angle), ( tankImg.getWidth() / 2) , ( tankImg.getHeight() / 2) );
            Graphics2D gtd = (Graphics2D) g;
            gtd.drawImage(tankImg, rotation, null);

            System.out.println(this.toString());
       }
    }
    
    @Override
    public void draw(Graphics2D g, ImageObserver obs){
        paintComponent(g);
        drawLives(g,obs);
        drawPlayerHealth(g,obs);
    }
    
    public void update() {
        
        if( this.UpPressed || this.DownPressed ){
            this.move();
        }
        if(this.LeftPressed){
            this.rotateLeft();
        }
        if(this.RightPressed){
            this.rotateRight();
        }
        if(this.shootPressed && !shooting){
           shooting = true;
           this.fire();
        }
        this.repaint();
    }
   
    private void fire() {
        Bullet b = new Bullet(bulletImg,x,y,angle,r, this);
        this.bc.addBullet(b);    
    }
    
    public void setShooting(boolean shoot){
        this.shooting = shoot;
    }
    
    public void drawLives(Graphics2D g, ImageObserver obs){
        this.lives.draw(g,obs);       
    }
    
    public void updateLives(){
        this.lives.update();
    }

    public void updatePlayerHealth(){
        this.health.update();
    }
    
    public void drawPlayerHealth(Graphics2D g, ImageObserver ob){
        this.health.draw(g, ob);
    }
    
    @Override 
    public void collisionEffect(Collidable obj){
        if(obj instanceof Wall || obj instanceof Tank){
            if(this.UpPressed){
            x -= vx;
            y -= vy;
        }
        
        if(this.DownPressed){
            x += vx;
            y += vy;
        }
        }
        else if (obj instanceof Bullet){
            if(((Bullet)obj).getOwner() != this)
                this.health.subtractHealth();
        }
        else if (obj instanceof PowerUp){
            if(lives.getLives() < 3)
                this.lives.addLife();
            else if(health.getHealthStatus() < 5)
                health.fillHealthBars();
            
            ((PowerUp) obj).setVisible(false);
        }
    }
    
    
    public void objCollisionEffect(TankGameObject obj){
        if(obj instanceof Wall){
            if(this.UpPressed){
            x -= vx;
            y -= vy;
        }
        
        if(this.DownPressed){
            x += vx;
            y += vy;
        }
        }
        else if (obj instanceof Bullet){
            if(((Bullet)obj).getOwner() != this)
                this.health.subtractHealth();
        }
        else if (obj instanceof PowerUp){
            if(lives.getLives() < 3)
                this.lives.addLife();
            else if(health.getHealthStatus() < 5)
                health.fillHealthBars();
            
            ((PowerUp) obj).setVisible(false);
        }
    }
    
    public void objCollisionEffect(Tank t){
       if(this.UpPressed){
            x -= vx;
            y -= vy;
        }
        
        if(this.DownPressed){
            x += vx;
            y += vy;
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
