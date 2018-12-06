import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;

public class SinglePlayer extends Game {

    private Player player = new Player();
    private int playerBoostHit;
    private int level = 1;
    private int points = 0;
    private boolean levelStarted = false;
    private boolean point1Got = false;
    private boolean point2Got = false;
    private boolean point3Got = false;
    private AudioClip point;

    //LEVELS
    private static Image level1;
    private static Image level2;
    private static Image level3;

    public void init() throws InterruptedException {
        super.init();
        singlePlayer = true;
        try {
            level3 = ImageIO.read(getClass().getResource("Levels/level3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        point = Applet.newAudioClip(getClass().getResource("Sounds/point.wav"));
    }

    public void roundStart(){
    resetBufferImage();
    ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    repaint();

    //sets players initial positions

    player.setPosition(200, (WINDOWHEIGHT - 88)/2 - (player.getSidelength()));
    player.setDirection(90);
    player.setColor(player1Color);
    player.setBoostsLeft(boostNumberSettings);
    player.setSideLength(playersizeSetting);

    player.setPosition(WINDOWWIDTH - 200, (WINDOWHEIGHT - 88)/2 - (player.getSidelength()));
    player.setDirection(270);
    player.setColor(player1Color);
    player.setBoostsLeft(boostNumberSettings);
    player.setSideLength(10);

    player.setSpeed(speedSetting);

    settings = false;

    points = 0;
    point1Got = false;
    point2Got = false;
    point3Got =false;

    if (level == 3){
        doubleBufferGraphics.drawImage(level3,0,0,this);
    }

    levelStarted = true;
    }



    public void paint (Graphics g) {
        super.paint(g);
        draw((Graphics2D) doubleBufferGraphics);

        //puts antialiasing onto the text that is rendered on the doubleBufferGraphics
        ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawImage(doubleBuffer, 0, 0, this);
        if (levelStarted && !settings) {

            // didn't know how to get this to work with the aliasing above because it would not work in my draw method
            // so I just casted the same aliasing onto g and put the drawing code here
            ((Graphics2D) (g)).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            //sets font and draws the left portion of the bottom info bar
            g.setFont(new Font("Cambria", Font.BOLD, 30));
            g.setColor(Color.red);
            g.drawString("Points needed: " + points, 50, BOTTOMTEXTYPOS);
        }
    }

    public void draw(Graphics2D g){


        if (!settings) {
            if (level == 1 && levelStarted){
                level1(g);
            }

            g.setColor(Color.black);
            player.draw(g);
            }

    }

    public void level1(Graphics2D g){
        g.setColor(Color.blue);


        g.fillRect(200,200,15,15);
        g.fillRect(WINDOWWIDTH - 100, 300, 15 , 15);
        g.fillRect(WINDOWWIDTH-200, WINDOWHEIGHT-150, 15,15);
    }


    public void actionPerformed(ActionEvent e) {
        frames++;
        player.update();

        //what happens when a player collides
        repaint();
        if (player.collison()){
            crash.play();
            dt.stop();
        }
        if (playerBoostHit > frames){
            player.boost(boostSpeedSettings);
        }
        else{
            player.boostStop(speedSetting);
        }
        level1Points();
    }

    public void level1Points(){
        if (level == 1){
            if (!point1Got){
                if (player.pointCollision(200,200)){
                    point.play();
                    points++;
                    point1Got = true;
                }
            }
            if (!point2Got){
                if (player.pointCollision(WINDOWWIDTH - 100, 300)){
                    point.play();
                    points++;
                    point2Got = true;
                }
            }
            if (!point3Got){
                if (player.pointCollision(WINDOWWIDTH-200, WINDOWHEIGHT-150)){
                    point.play();
                    points++;
                    point2Got = true;
                }
            }
            if (points == 3){
                dt.stop();
                level = 3;
            }
        }
    }
    public void keyPressed(KeyEvent k){
        int keyCode = k.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT){
            player.turnLeft();
        }
        else if (keyCode == KeyEvent.VK_RIGHT){
            player.turnRight();
        }
        if (keyCode == KeyEvent.VK_UP) {
            if (player.getBoostsLeft() > 0){
                boostSound1.play();
            }
            playerBoostHit = frames + BOOSTTIME;
        }

    }
    }

