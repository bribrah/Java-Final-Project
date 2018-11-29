import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class Game extends JPanel implements KeyListener, ActionListener{
    private Player player2 = new Player();
    private Player player1 = new Player();


    private Timer dt;
    private static Image doubleBuffer;
    private Graphics doubleBufferGraphics;
    private int frames; //holds current frame value
    private int boost1Hit; //holds the frame value when boost is hit
    private int boost2Hit;
    private boolean player1Win;
    private boolean player2Win;
    private static Image splashScreen;
    private boolean gameStarted = false;

    //CONSTANTS
    int FRAMEDELAY = 15;
    int BOOSTTIME = 60;
    public static int WINDOWWIDTH = 1000;
    public static int WINDOWHEIGHT = 800;
    static String GAMETITLE = "Burnout Battle";
    static int BOTTOMTEXTYPOS = WINDOWHEIGHT - 50;
    static int playersizeSetting = 7;
    int speedSetting = 3;
    int settingsSelection = 0;
    int boostNumberSettings = 3;
    static int boostSpeedSettings = 7;

    // SOUNDS
    private AudioClip crash;
    private AudioClip music;
    private AudioClip boostSound1;
    private AudioClip boostSound2;
    private AudioClip roundStart;


    //sprites
    Image player1Up,player1Right,player1Down,player1Left;

    {
        try {
            player1Up = ImageIO.read(getClass().getResource("sprites/Audi_Up.png"));
            player1Right = ImageIO.read(getClass().getResource("sprites/Audi_Right.png"));
            player1Down = ImageIO.read(getClass().getResource("sprites/Audi_Down.png"));
            player1Left = ImageIO.read(getClass().getResource("sprites/Audi_Left.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *makes game window and adds keylistener for keystrokes
     * adds Game to gameWindow
     * initializes game by calling game.init();
     */
    public static void main(String[] args){
        JFrame gameWindow = new JFrame(GAMETITLE);
        gameWindow.setBounds(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        gameWindow.setBackground(Color.black);

        Game game = new Game();
        gameWindow.getContentPane().add(game);

        gameWindow.setVisible(true);
        gameWindow.addKeyListener(game);
        game.init();


    }



    /**
     * initializes game structures that are needed
     * makes doublebuffer so the image does not flicker when it refreshes and so the trails do not go away
     * draws the splash screen on the screen
     * initializes all sounds
     */
    public void init(){ ;
        doubleBuffer = createImage(getWidth(),getHeight());
        doubleBufferGraphics = doubleBuffer.getGraphics();
        try {
            splashScreen = ImageIO.read(getClass().getResource("splash image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        doubleBufferGraphics.drawImage(splashScreen,0,0,this);

        //SOUNDS INIT
        //music = Applet.newAudioClip(getClass().getResource("Sounds/test2.wav"));
        crash = Applet.newAudioClip(getClass().getResource("Sounds/crash.wav"));
        boostSound1 = Applet.newAudioClip(getClass().getResource("Sounds/boostSound.wav"));
        boostSound2 = Applet.newAudioClip(getClass().getResource("Sounds/boostSound.wav"));
        roundStart = Applet.newAudioClip(getClass().getResource("Sounds/startCountDown.wav"));


        dt = new Timer(FRAMEDELAY, this);
        repaint();

    }
    public void settings(){
        doubleBufferGraphics.clearRect(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        doubleBufferGraphics.setColor(Color.red);
        doubleBufferGraphics.setFont(new Font("Cambria", Font.BOLD, 60));
        doubleBufferGraphics.drawString("SETTINGS",getWidth()/2 -130 ,85);
        doubleBufferGraphics.setFont(new Font("Times",Font.PLAIN,30));

        if (settingsSelection == 0) {
            doubleBufferGraphics.fillOval(30, 130, 25, 25);
        }
        else if(settingsSelection == 1){
            doubleBufferGraphics.fillOval(30, 230, 25, 25);
        }
        else if(settingsSelection == 2){
            doubleBufferGraphics.fillOval(30, 330, 25, 25);
        }
        else if (settingsSelection == 3){
            doubleBufferGraphics.fillOval(30, 430, 25, 25);
        }
        doubleBufferGraphics.drawString("player speed: " + this.speedSetting, 70, 150);
        doubleBufferGraphics.drawString("player size: " + this.playersizeSetting,70,250);
        doubleBufferGraphics.drawString("Number of boosts: " + this.boostNumberSettings,70,350);
        doubleBufferGraphics.drawString("Boost Speed: " + this.boostSpeedSettings,70,450);
        repaint();
    }

    /**
     *starts a round, puts players in right spots, resets boosts, resets booleans and such
     * also clears the doubleBufferGraphics so that the old trails get deleted
     */
    public void roundStart(){
        if (gameStarted == false){
            doubleBuffer = createImage(getWidth(),getHeight() - 50);
            doubleBufferGraphics = doubleBuffer.getGraphics();
            gameStarted = true;

        }
        doubleBufferGraphics.clearRect(0,0,WINDOWWIDTH,WINDOWHEIGHT);
        repaint();

        //sets players initial positions
        player2.setPosition(WINDOWWIDTH - 200, (((WINDOWHEIGHT - 88) / 2) - (player2.getSidelength())));
        player2.setDirection(270);
        player2.setColor(Color.cyan);
        player2.setSideLength(playersizeSetting);
        player2.setBoostsLeft(boostNumberSettings);
        player2.setSpeed(speedSetting);
        boost1Hit = 0;
        player1.setPosition(200, (WINDOWHEIGHT - 88)/2 - (player2.getSidelength()));
        player1.setDirection(90);
        player1.setColor(Color.red);
        player1.setBoostsLeft(boostNumberSettings);
        player1.setSideLength(playersizeSetting);
        player1.setSpeed(speedSetting);
//        try {
//            player1.setSprite(ImageIO.read(getClass().getResource("sprites/Audi_Left.png")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        boost2Hit = 0;
        player1Win = false;
        player2Win = false;


    }

    /**
     * method that is called when repaint is called
     * calls draw onto the doubleBufferGraphics to draw players onto it
     * draws the current doubleBuffer image onto the game portion of the gameWindow
     * draws the bottom info bar
     * draws a win message upon a collision
     * @param g the graphics object that is used to draw onto the doubleBuffer image
     */
    public void paint (Graphics g){
        super.paint(g);
        draw((Graphics2D) doubleBufferGraphics);

        g.drawImage(doubleBuffer, 0, 0, this);


        if (gameStarted == true) {
            //sets font and draws the left portion of the bottom info bar
            g.setFont(new Font("Cambria", Font.BOLD, 30));
            g.setColor(Color.red);
            g.drawString("Player 1 Boosts: " + String.valueOf(player1.getBoostsLeft()), 50, BOTTOMTEXTYPOS);
            g.drawString(String.valueOf(player1.getScore()), WINDOWWIDTH / 2 - 50, BOTTOMTEXTYPOS);

            //changes color and draws right portion of bottom info bar
            g.setColor(Color.blue);
            g.drawString("Player 2 Boosts: " + String.valueOf(player2.getBoostsLeft()), WINDOWWIDTH - 350, BOTTOMTEXTYPOS);
            g.drawString(String.valueOf(player2.getScore()), WINDOWWIDTH / 2 + 50, BOTTOMTEXTYPOS);
        }

        //displays which player has won and then stops frames.
        if (player1Win == true){
            g.setFont(new Font("Cambria", Font.BOLD, 50));
            g.setColor(Color.white);
            g.drawString("PLAYER 1 WINS", WINDOWWIDTH /2 - 200, WINDOWHEIGHT/2 - 50);
            dt.stop();
            return;
        }
        else if(player2Win == true){
            g.setFont(new Font("Cambria", Font.BOLD, 50));
            g.setColor(Color.white);
            g.drawString("PLAYER 2 WINS", WINDOWWIDTH /2 - 200, WINDOWHEIGHT/2 - 50);
            dt.stop();
            return;
        }

    }

    /**
     * method that repaint calls
     * draws the players
     * @param g doublebufferGraphics should be passed here after being casted to Graphics2D
     */
    public void draw(Graphics2D g){
        g.setColor(Color.black);
        player2.draw(g);
        player1.draw(g);

    }

    /**
     * what happens each frame
     * detects collision, determines if boost time is up and updates players position
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        frames++;

        //what happens when a player collides
        if (player2.collison()) {
            crash.play();
            player1.setScore(player1.getScore() + 1);
            player1Win = true;
        } else if (player1.collison()) {
            crash.play();
            player2.setScore(player2.getScore() + 1);
            player2Win = true;
        }


        //sets player boosts to run for approx 50 frames when boost button is hit
        if (boost1Hit > this.frames) {
            player2.boost(boostSpeedSettings);
        } else {
            player2.boostStop(speedSetting);
            boostSound1.stop();
        }
        if (boost2Hit > this.frames) {
            player1.boost(boostSpeedSettings);
        } else {
            player1.boostStop(speedSetting);
        }

        //changes players sprites based on direction
        if (player1.getDirection() == 0 || player1.getDirection() == 360) {
            player1.setSprite(player1Up);
        }
        else if (player1.getDirection() == 90){
            player1.setSprite(player1Right);
        }
        else if (player1.getDirection() == 180){
            player1.setSprite((player1Down));
        }
        else if (player1.getDirection() == 270){
            player1.setSprite((player1Left));
        }
            player2.update();
            player1.update();
            repaint();
    }

    /**
     * method that is called inorder to see if a certain pixel is empty
     * checks by seeing if the pixel is black
     * @param x x position on the doubleBuffer image to check
     * @param y y position on the doubleBuffer image to check
     * @return true if empty, false if not
     */
    public static boolean isEmpty(int x, int y){
        BufferedImage arenaGrid = (BufferedImage) doubleBuffer;
        Color pixelColor = new Color(arenaGrid.getRGB(x,y));
        return pixelColor.getBlue() == 0 && pixelColor.getRed() == 0 && pixelColor.getBlue() == 0;

    }


    /**
     * detecs keypresses sent from the keylistener
     * determines which action to take based on what ley is pressed
     * @param k sent from keyListener
     */
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
            if (player1.getBoostsLeft() > 0 && !player1Win && !player2Win &&gameStarted){
                boostSound1.play();
            }
            boost1Hit = this.frames + BOOSTTIME;
        }
        if (keyCode == KeyEvent.VK_W){
            if (player2.getBoostsLeft() > 0 && !player1Win && !player2Win &&gameStarted){
                boostSound2.play();
            }
            boost2Hit = this.frames + BOOSTTIME;
        }

    }

    /**
     * listens to key release events sent from keyListener and determines correct actions
     * @param k sent from keyListener
     */

    public void keyReleased(KeyEvent k){
        int keyCode = k.getKeyCode();

        if (keyCode == KeyEvent.VK_RIGHT) {
            if (gameStarted == false) {
                if (settingsSelection == 0) {
                    this.speedSetting++;
                    this.speedSetting = this.speedSetting % 7;
                }
                if (settingsSelection == 1){
                    this.playersizeSetting++;
                    this.playersizeSetting = this.playersizeSetting % 15;

                }
                if (settingsSelection == 2){
                    this.boostNumberSettings++;
                }
                if (settingsSelection == 3){
                    this.boostSpeedSettings++;
                    this.boostSpeedSettings = this.boostSpeedSettings % 10;
                }


                settings();
            }
        }
        if (keyCode == KeyEvent.VK_DOWN){
            settingsSelection ++;
            settingsSelection = settingsSelection % 4;
            settings();
        }

        //have to put these here so boost method runs before decrement of boostsLeft
        if (keyCode == KeyEvent.VK_UP) {
            if (gameStarted == true){
                if (player2.getBoostsLeft() > 0) {
                    player2.setBoostsLeft(player2.getBoostsLeft() - 1);
                }
            }
            else{
                settingsSelection--;
                if (settingsSelection < 0){
                    settingsSelection = 3;
                }
                settings();
            }
        }

        if (keyCode == KeyEvent.VK_W){
            if (player1 .getBoostsLeft() > 0) {
                player1.setBoostsLeft(player1.getBoostsLeft() - 1);
            }
        }
        if (keyCode == KeyEvent.VK_S){
            if (gameStarted == false) {
                settings();
            }
        }
        if (keyCode == KeyEvent.VK_ENTER){
            if(!dt.isRunning()){
                //music.play();
                roundStart.play();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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