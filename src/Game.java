
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

public class Game extends JPanel implements KeyListener, ActionListener{
    private Player player1 = new Player();
    private Player player2 = new Player();

    private static String gameTitle = "Burnout Battle";
    private Timer dt;
    private static Image doubleBuffer;
    private Graphics doubleBufferGraphics;
    private int frames; //holds current frame value
    private int boost1Hit; //holds the frame value when boost is hit
    private int boost2Hit;

    //CONSTANTS
    private int FRAMEDELAY = 15;
    private int BOOSTTIME = 60;

    // constants for window dimensions
    public static int WINDOWWIDTH = 1280;
    public static int WINDOWHEIGHT = 720;

    //sprites
    File audiUpPath = new File("/resources/sprites","Audi_Up.png");
//    BufferedImage audi_up =  ImageIO.read(new File("Audi_Down"));
    ImageIcon audi_left = new ImageIcon("resources/Sprites/Audi_Left");



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
        dt = new Timer(FRAMEDELAY, this);

        roundStart();
    }
    //starts a game, puts players in right spots and such
    public void roundStart(){
        doubleBufferGraphics.clearRect(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        repaint();

        //sets players initial positions
        player1.setPosition(WINDOWWIDTH - 200, (WINDOWHEIGHT / 2) - (player1.getSidelength()/2));
        player1.setDirection(270);
        player1.setColor(Color.blue);
        player1.setBoostsLeft(3);
        boost1Hit = 0;
        player2.setPosition(200, WINDOWHEIGHT/2 - (player1.getSidelength()/2));
        player2.setDirection(90);
        player2.setColor(Color.red);
        player2.setBoostsLeft(3);
        boost2Hit = 0;
    }
    //method that gets called when repaint is called
    public void paint (Graphics g){
        draw((Graphics2D) doubleBufferGraphics);
        g.drawImage(doubleBuffer, 0, 0, this);
    }

    //draws all objects on the game screen
    public void draw(Graphics2D g){
        g.setColor(Color.BLACK);
        player1.draw(g);
        player2.draw(g);

    }
    public void actionPerformed(ActionEvent e){
        frames++;

        if (player1.collison() == true) {
            System.out.print("Blue player loses\n");
            dt.stop();
            return;
        }
        else if (player2.collison() == true){
            System.out.print("Red Player Loses\n");
            dt.stop();
            return;
        }

        //sets player boosts to run for approx 50 frames when boost button is hit
        if (boost1Hit > this.frames){
            player1.boost();
        }
        else{
            player1.boostStop();
        }
        if (boost2Hit > this.frames){
            player2.boost();
        }
        else{
            player2.boostStop();
        }


        player1.update();
        player2.update();
        repaint();
    }

    public static boolean isEmpty(int x, int y){
        BufferedImage arenaGrid = (BufferedImage) doubleBuffer;
        Color pixelColor = new Color(arenaGrid.getRGB(x,y));
        if (pixelColor.getBlue() == 0 && pixelColor.getRed() == 0 && pixelColor.getBlue() == 0 ){
            return true;
        }
        else{
            return false;
        }

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
        if (keyCode == KeyEvent.VK_UP) {
           boost1Hit = this.frames + BOOSTTIME;
           player1.setBoostsLeft(player1.getBoostsLeft() - 1);
        }
        if (keyCode == KeyEvent.VK_W){
            boost2Hit = this.frames + BOOSTTIME;
            player2.setBoostsLeft(player2.getBoostsLeft() - 1);
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