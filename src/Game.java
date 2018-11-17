import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;

//@  SuppressWarnings({"WeakerAccess", "CanBeFinal"})
class Game extends JPanel implements KeyListener, ActionListener{
    private Player player2 = new Player();
    private Player player1 = new Player();


    private Timer dt;
    private static Image doubleBuffer;
    private Graphics doubleBufferGraphics;
    private int frames; //holds current frame value
    private int boost1Hit; //holds the frame value when boost is hit
    private int boost2Hit;

    //CONSTANTS
    private int FRAMEDELAY = 15;
    private int BOOSTTIME = 60;
    public static int WINDOWWIDTH = 1000;
    public static int WINDOWHEIGHT = 800;
    private static String GAMETITLE = "Burnout Battle";
    private static int BOTTOMTEXTYPOS = WINDOWHEIGHT - 50;

    //sprites
    File audiUpPath = new File("/resources/sprites","Audi_Up.png");
    //    BufferedImage audi_up =  ImageIO.read(new File("Audi_Down"));
    ImageIcon audi_left = new ImageIcon("resources/Sprites/Audi_Left");



    //makes game window and listens for keystrokes
    public static void main(String[] args){
        JFrame gameWindow = new JFrame(GAMETITLE);
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
        doubleBuffer = createImage(getWidth(),getHeight() - 50);
        doubleBufferGraphics = doubleBuffer.getGraphics();
        dt = new Timer(FRAMEDELAY, this);

        roundStart();
    }
    //starts a game, puts players in right spots and such
    public void roundStart(){
        doubleBufferGraphics.clearRect(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        repaint();

        //sets players initial positions
        player2.setPosition(WINDOWWIDTH - 200, ((WINDOWHEIGHT - 50) / 2) - (player2.getSidelength()));
        player2.setDirection(270);
        player2.setColor(Color.blue);
        player2.setBoostsLeft(3);
        boost1Hit = 0;
        player1.setPosition(200, (WINDOWHEIGHT - 50)/2 - (player2.getSidelength()));
        player1.setDirection(90);
        player1.setColor(Color.red);
        player1.setBoostsLeft(3);
        boost2Hit = 0;
    }
    //method that gets called when repaint is called
    public void paint (Graphics g){
        super.paint(g);
        draw((Graphics2D) doubleBufferGraphics);
        Line2D bottomLine = new Line2D.Float(0,getHeight() - 50,WINDOWWIDTH,getHeight() - 50);
        g.drawImage(doubleBuffer, 0, 0, this);
        ((Graphics2D) doubleBufferGraphics).draw(bottomLine);
        g.setFont(new Font("Cambria", Font.BOLD,26));
        g.setColor(Color.RED);
        g.drawString("Player 1 Boosts: " + String.valueOf(player1.getBoostsLeft()),50,BOTTOMTEXTYPOS );

        g.drawString(String.valueOf(player1.getScore()), WINDOWWIDTH /2 -50, BOTTOMTEXTYPOS);
        g.setColor(Color.blue);
        g.drawString("Player 2 Boosts: " + String.valueOf(player2.getBoostsLeft()), WINDOWWIDTH - 300, BOTTOMTEXTYPOS);
        g.drawString(String.valueOf(player2.getScore()), WINDOWWIDTH / 2 + 50, BOTTOMTEXTYPOS);
    }

    //draws all objects on the game screen
    public void draw(Graphics2D g){
        g.setColor(Color.BLACK);
        player2.draw(g);
        player1.draw(g);

    }

    //what happens in between each new frame
    public void actionPerformed(ActionEvent e){
        frames++;

        if (player2.collison()) {
            player1.setScore(player1.getScore() + 1);
            dt.stop();
            return;
        }
        else if (player1.collison()){
            player2.setScore(player2.getScore() + 1);
            dt.stop();
            return;
        }

        //sets player boosts to run for approx 50 frames when boost button is hit
        if (boost1Hit > this.frames){
            player2.boost();
        }
        else{
            player2.boostStop();
        }
        if (boost2Hit > this.frames){
            player1.boost();
        }
        else{
            player1.boostStop();
        }


        player2.update();
        player1.update();
        repaint();
    }

    //method to check if a oixel is empty or not
    public static boolean isEmpty(int x, int y){
        BufferedImage arenaGrid = (BufferedImage) doubleBuffer;
        Color pixelColor = new Color(arenaGrid.getRGB(x,y));
        return pixelColor.getBlue() == 0 && pixelColor.getRed() == 0 && pixelColor.getBlue() == 0;

    }





//game controls
    public void keyPressed(KeyEvent k){
        int keyCode = k.getKeyCode();
        if (keyCode == KeyEvent.VK_A){
            player1.turnLeft();
        }
        else if (keyCode == KeyEvent.VK_D){
            player1.turnRight();
        }
        else if (keyCode == KeyEvent.VK_LEFT){
            player2.turnLeft();
        }
        else if (keyCode == KeyEvent.VK_RIGHT){
            player2.turnRight();
        }
        if (keyCode == KeyEvent.VK_UP) {
            boost1Hit = this.frames + BOOSTTIME;
            player2.setBoostsLeft(player2.getBoostsLeft() - 1);
        }
        if (keyCode == KeyEvent.VK_W){
            boost2Hit = this.frames + BOOSTTIME;
            player1.setBoostsLeft(player1.getBoostsLeft() - 1);
        }

    }

    //Start game with enter key and quits with escape key
    //stops thread for 1.2 seconds in order to prevent lag

    public void keyReleased(KeyEvent k){
        int keyCode = k.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER){
            if(!dt.isRunning()){

                try
                {
                    Thread.sleep(1200);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
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