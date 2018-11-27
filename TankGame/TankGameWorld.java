/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author madelineschmoll
 */
public class TankGameWorld extends Canvas implements Runnable{
   
    public static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
    public static final int WORLD_WIDTH = 1600 ,WORLD_HEIGHT = 1200;
    private final String gameTitle = "Tank Wars Game";
    
    private Thread thread;
    private boolean running;
    
    private final TankGameEvents geo1,geo2;
    private Graphics2D g2d;
    
    private BufferedImage tank1Image, bulletImage, wallImage, breakableWallImage, puImage;
    private BufferedImage bimg, background;
    private BufferedImage leftScreen, rightScreen, mm;
    private BufferedImage bigLeg;
    
    private Tank player1,player2;
    private BulletControl bcP1, bcP2;
    private final LinkedList<Wall> walls;
    private LinkedList<PowerUp> powerUps;
    private LinkedList<Drawable> drawables;
    private LinkedList<Collidable> collidables;
    private CollisionDetector cd;

    private static JFrame frame;
    
    
    public TankGameWorld() {
        try {
            this.bimg = ImageIO.read(new File("Resources/Background.bmp"));
        } catch (IOException ex) {
            Logger.getLogger(TankGameWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        geo1 = new TankGameEvents();
        geo2 = new TankGameEvents();
        walls = new LinkedList<>();
        powerUps = new LinkedList<>();
        drawables = new LinkedList<>();
        collidables = new LinkedList<>();
    }
    
    public static void main(String args[]) throws IOException{
        TankGameWorld game = new TankGameWorld();
         
        game.setPreferredSize(new Dimension(SCREEN_WIDTH , SCREEN_HEIGHT));
        game.setMaximumSize(new Dimension(WORLD_WIDTH, WORLD_HEIGHT));
        game.setMinimumSize(new Dimension(SCREEN_WIDTH , SCREEN_HEIGHT));
       
        game.setSize(new Dimension(WORLD_WIDTH, WORLD_HEIGHT)); 
       
        frame = new JFrame(game.getTitle());
        frame.addWindowListener(new WindowAdapter() {
        @Override
	public void windowGainedFocus(WindowEvent e) {
                        game.requestFocusInWindow();
		    }
	    });
        frame.add(game);
        frame.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        frame.pack();
	frame.setVisible(true);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        game.init();

        game.start();
        
        
    }
    
    private void update(){ // tick 
        player1.update();
        player2.update();
        player1.updatePlayerHealth();
        player2.updatePlayerHealth();
        player1.updateLives();
        player2.updateLives();
        bcP1.update();
        bcP2.update();
       
        checkCollisions();
       
    }
    
    
    private void checkCollisions(){
        
        
        // collision checks needed:
        // 1 -- tank vs tank
        // 2 -- tank vs walls
        // 3 -- tank vs bullets
        // 4 -- bullets vs walls
        // 5 -- tanks vs powerups
       
        
        // 1
        cd.checkCollision(player1,player2);

        // 2
        for(int i = 0; i < walls.size(); i++){
             cd.checkCollision(player1, walls.get(i));
             cd.checkCollision(player2, walls.get(i));
        }

        // 3
        for(int i = 0; i < bcP1.size(); i++){
             cd.checkCollision(player2,bcP1.get(i));
             if(!bcP1.get(i).getVisible()){
                     bcP1.removeBullet(i);
                 }
        }

        for(int i = 0; i < bcP2.size(); i++){
             cd.checkCollision(player1, bcP2.get(i));
             if(!bcP2.get(i).getVisible()){
                     bcP2.removeBullet(i);
                 }
        }
        
        //4
        for(int i = 0; i < walls.size(); i++){
            for(int j = 0; j < bcP1.size(); j++){
                 cd.checkCollision(bcP1.get(j), walls.get(i));
                 if(!walls.get(i).getVisible()){
                     walls.remove(i);
                 }
            }
        }

        for(int i = 0; i < walls.size(); i++){
            for(int j = 0; j < bcP2.size(); j++){
                 cd.checkCollision(bcP2.get(j), walls.get(i));
                 if(!walls.get(i).getVisible()){
                     walls.remove(i);
                     
                 }
            }
        }

        // 5
        for(int i = 0; i < powerUps.size(); i++){
             cd.checkCollision(player1,powerUps.get(i));
             cd.checkCollision(player2, powerUps.get(i));
             if(!powerUps.get(i).getVisible()){
                     powerUps.remove(i);
                 }
        }

    }
    
    private void draw(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(2);
            return;
        }
        
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        
        /// Draw World
        drawWorld(g);
        
        //LOOK UP 
        leftScreen = background.getSubimage(getScreenX(player1),getScreenY(player1),SCREEN_WIDTH/2,SCREEN_HEIGHT);
        rightScreen = background.getSubimage(getScreenX(player2),getScreenY(player2),SCREEN_WIDTH/2,SCREEN_HEIGHT);
        mm = background.getSubimage(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
       
        g.drawImage(leftScreen, 0, 0, this);
        g.drawImage(rightScreen, 400, 0, this);
        
        // draw mini map
        g.scale(0.2,0.2);
        g.drawImage(mm, SCREEN_WIDTH + 300, SCREEN_HEIGHT + 1200, this);
        g.dispose();
        bs.show();
        
    }
    
    private int getScreenX(Tank t){
        int offset = (t.getX() + (t.getWidth() / 2 )) - (SCREEN_WIDTH/2);
        
        if(t.getX() + offset <= 0){
            return 0;
        }
        else if(t.getX() + offset > 0 && t.getX() + offset < WORLD_WIDTH + SCREEN_WIDTH){
            return ((t.getX() + offset) / 4);
        }
        else if(t.getX() + offset >= WORLD_WIDTH + SCREEN_WIDTH){
            return (WORLD_WIDTH - (SCREEN_WIDTH / 2));
        }
        return 0;
    }
    
    
    private int getScreenY(Tank t){
        int offset = (t.getY() + (t.getHeight() / 2 )) - (SCREEN_HEIGHT);
        
        if(t.getY() + offset <= 0){
            return 0;
        }
        else if(t.getY() + offset > 0 && t.getY() + offset < WORLD_HEIGHT - SCREEN_HEIGHT){
            return (t.getY() + offset) / 4;
        }
        else if(t.getY() + offset >= WORLD_HEIGHT - SCREEN_HEIGHT){
            return (WORLD_HEIGHT - SCREEN_HEIGHT);
        }
        
        return 0; 
    }
   
    
    private void drawWorld(Graphics2D g){
        // background 
        background = new BufferedImage(1600, 1200, BufferedImage.TYPE_INT_ARGB);
        g = background.createGraphics();
        
        for(int i = 0; i < 1600; i+= 320){
            for(int j = 0; j < 1200; j += 240)
                g.drawImage(bimg, i, j, 320, 240, this);
        }
        
        g.drawImage(background, 0, 0, this);
        /*
        for(int i = 0; i < drawables.size(); i++){
            drawables.get(i).draw(g,this);
        }
        
        */
        
        
        for(int i = 0; i < walls.size(); i++){
            walls.get(i).draw(g, this);
        }
        
        for(int i = 0; i < powerUps.size(); i++)
           powerUps.get(i).draw(g,this);
        
        player1.paintComponent(g);
        player2.paintComponent(g);
        player1.drawLives(g, this);
        player2.drawLives(g, this);
        player1.drawPlayerHealth(g, this);
        player2.drawPlayerHealth(g, this);
        bcP1.draw(g, this);
        bcP2.draw(g, this); 
    }
    
       
    public String getTitle(){
        return this.gameTitle;
    }
    
    @Override
    public void run(){ // run will execute when thread starts
       long lastTime = System.nanoTime(); // operates in nano seconds
       final int numberOfTicks = 60; // frames per second
       double ns = 1000000000 / numberOfTicks;
       double delta = 0; 
       
        while(running){
            long currTime = System.nanoTime();
            delta += (currTime - lastTime) / ns;
            lastTime = currTime;
            if(delta > 0){
                update();
            }
            draw();
        }
        
        stop();
    }
    
    public synchronized void start() {
        
        if(running){
            return;
        } 
        
        running = true;
        System.out.println();
        thread = new Thread(this); //this changed
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    
    public synchronized void stop(){
        
        if(!running){
            return;
        }
        
        running = false;
        
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(TankGameWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(1);
        
    }
    
    public void init(){
        
        try {
            
            // initialize images
            tank1Image = ImageIO.read(new File("Resources/Tank2.gif"));
            bulletImage = ImageIO.read(new File("Resources/Shell.gif"));
            wallImage = ImageIO.read(new File("Resources/Wall1.gif"));
            breakableWallImage = ImageIO.read(new File("Resources/Wall2.gif"));
            puImage = ImageIO.read(new File("Resources/PowerUp.gif"));
       
        } catch (IOException ex) {
            Logger.getLogger(TankGameWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // create instance of tank, bulletcontrol
        
        bcP1 = new BulletControl();
        bcP2 = new BulletControl();
        
        player1 = new Tank(0,0,0,0, (short) 0, tank1Image, bulletImage, bcP1, this);
        player2 = new Tank(WORLD_WIDTH - tank1Image.getWidth(),0,0,0, (short) 180, tank1Image, bulletImage, bcP2, this);
       
        
        //// GET RID OF THIS AFTER IMPLEMENTING COLLIDABLE
        walls.add(new Wall(wallImage, 267, 0, false)); // 1
        walls.add(new Wall(wallImage, 267, 1008, false)); // 2 
        walls.add(new Wall(breakableWallImage, 534, 504, true)); // 3 
        walls.add(new Wall(breakableWallImage, 801, 252, true)); // 4
        walls.add(new Wall(breakableWallImage, 801, 756, true)); // 5
        walls.add(new Wall(breakableWallImage, 1068, 504, true)); // 6 
        walls.add(new Wall(wallImage, 1335, 0, false)); // 7 
        walls.add(new Wall(wallImage, 1335, 1008, false)); // 8
        
        powerUps.add(new PowerUp(puImage, 604, 575));
        powerUps.add(new PowerUp(puImage, 975, 575));
        /////
        
       /* drawables.add(new Wall(wallImage, 267, 0, false));
        drawables.add(new Wall(wallImage, 267, 1008, false));
        drawables.add(new Wall(breakableWallImage, 534, 504, true));
        drawables.add(new Wall(breakableWallImage, 801, 252, true));
        drawables.add(new Wall(breakableWallImage, 801, 756, true));
        drawables.add(new Wall(breakableWallImage, 1068, 504, true));
        drawables.add(new Wall(wallImage, 1335, 0, false));
        drawables.add(new Wall(wallImage, 1335, 1008, false));
        drawables.add(new PowerUp(puImage, 300, 300));
        drawables.add(player1);
        drawables.add(player2);
        drawables.add(bcP1);
        drawables.add(bcP2);
        
        collidables.add(new Wall(wallImage, 267, 0, false));
        collidables.add(new Wall(wallImage, 267, 1008, false));
        collidables.add(new Wall(breakableWallImage, 534, 504, true));
        collidables.add(new Wall(breakableWallImage, 801, 252, true));
        collidables.add(new Wall(breakableWallImage, 801, 756, true));
        collidables.add(new Wall(breakableWallImage, 1068, 504, true));
        collidables.add(new Wall(wallImage, 1335, 0, false));
        collidables.add(new Wall(wallImage, 1335, 1008, false));
        collidables.add(new PowerUp(puImage, 300, 300));
        collidables.add(player1);
        collidables.add(player2);
        collidables.add(bcP1);
        collidables.add(bcP2); */
        
        // create tank controls
        TankControls playerControl1 = new TankControls(player1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
        TankControls playerControl2 = new TankControls(player2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_F);
        
        frame.add(player1);
        frame.add(player2);
        // add a key listener
        addKeyListener(playerControl1);
        addKeyListener(playerControl2);
        this.geo1.addObserver(player1);
        this.geo2.addObserver(player2);
        
        // add collision detector
        cd = new CollisionDetector(geo1,geo2);
        
         
    }
  
}
