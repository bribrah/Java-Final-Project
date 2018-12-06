import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SinglePlayer extends Game {

    private Player player = new Player();
    private int playerBoostHit;
    private int level = 1;
    private boolean levelStarted = false;
    private AudioClip point;

    //LEVELS
    private static Image level1;
    private static Image level2;
    private static Image level3;

    public void roundStart(){
    doubleBuffer = createImage(getWidth(),getHeight());
    doubleBufferGraphics = doubleBuffer.getGraphics();
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
//        try {
//            player1.setSprite(ImageIO.read(getClass().getResource("sprites/Audi_Left.png")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    settings = false;




    levelStarted = true;
    }

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



    public void paint (Graphics g){
        super.paint(g);
        draw((Graphics2D) doubleBufferGraphics);

        //puts antialiasing onto the text that is rendered on the doubleBufferGraphics
        ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawImage(doubleBuffer, 0, 0, this);
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
        g.fillRect(WINDOWWIDTH-200, WINDOWHEIGHT-100, 15,15);
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
        if (player.blueCollison()){
            point.play();
        }
        if (playerBoostHit > frames){
            player.boost(boostSpeedSettings);
        }
        else{
            player.boostStop(speedSetting);
        }
    }

    public static boolean isBlue(int x,int y){
        BufferedImage arenaGrid = (BufferedImage) doubleBuffer;
        Color pixelColor = new Color(arenaGrid.getRGB(x,y));
        if (pixelColor.getBlue() == 255){
            return true;
        }
        else{
            return false;
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

