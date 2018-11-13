import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel implements KeyListener, ActionListener{
    private Player player1 = new Player();
    private Player player2 = new Player();

    private static String gameTitle = "Burnout Battle";
    private Timer dt;
    private static Image doubleBuffer;
    private Graphics doubleBufferGraphics;

    // constants for window dimensions
    static int WINDOWWIDTH = 1920;
    static int WINDOWHEIGHT = 1080;

    //sprites
    ImageIcon audi_up =  new ImageIcon("src/resources/Sprites/Audi_Up");
    ImageIcon audi_left = new ImageIcon("src/resources/Sprites/Audi_Left");



    //makes game window and listens for keystrokes
    public static void main(String[] args){
        JFrame gameWindow = new JFrame(gameTitle);
        gameWindow.setBounds(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);

        Game game = new Game();
        gameWindow.getContentPane().add(game);
        gameWindow.setBackground(Color.BLACK);
        gameWindow.setVisible(true);
        game.init();
        gameWindow.addKeyListener(game);
    }

    //makes doublebuffer so the image does not flicker when it refreshes, shoudl refresh every 15ms
    public void init(){
        doubleBuffer = createImage(getWidth(),getHeight());
        doubleBufferGraphics = doubleBuffer.getGraphics();
        dt = new Timer(15, this);
        roundStart();
    }
    //method that gets called when repaint is called
    public void paint (Graphics g){
        draw((Graphics2D) doubleBufferGraphics);
        g.drawImage(doubleBuffer, 0, 0, this);
    }
    //starts a game, puts players in right spots and such
    public void roundStart(){
        doubleBufferGraphics.clearRect(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        repaint();

        //sets players initial positions
        player1.position(WINDOWWIDTH - 200, WINDOWHEIGHT / 2);
        player1.direction(270);
        player1.setSprite(audi_left);
        player1.draw();
        player2.position(200, WINDOWHEIGHT/2);
        player2.direction(90);
    }

    //draws all objects on the game screen
    public void draw(Graphics2D g){
        g.setColor(Color.BLACK);

    }


    public void actionPerformed(ActionEvent e){


        repaint();
    }
    public void keyPressed(KeyEvent k){
        int keyCode = k.getKeyCode();
        if (keyCode == KeyEvent.VK_A){
            player2.turnLeft();
        }
        else if (keyCode == KeyEvent.VK_D){
            player2.turnRight();
        }
        else if (keyCode == KeyEvent.VK_LEFT){
            player1.turnLeft();
        }
        else if (keyCode == KeyEvent.VK_RIGHT){
            player1.turnRight();
        }

    }

    //Start game with enter key and quits with escape key
    public void keyReleased(KeyEvent k){
        int keyCode = k.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER){
            if(!dt.isRunning()){
                dt.start();
                roundStart();
            }
        }
        else if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    public void keyTyped(KeyEvent k){}
}