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

    static Timer dt;
    static Image doubleBuffer;
    static Graphics doubleBufferGraphics;
    static int frames; //holds current frame value
    private int boost1Hit; //holds the frame value when boost is hit
    private int boost2Hit;
    private boolean player1Win;
    private boolean player2Win;
    private boolean gameWon;
    private static Image splashScreen;
    private boolean gameStarted = false;
    public boolean settings = false;
    public static boolean singlePlayer = false;

    //CONSTANTS
    static int FRAMEDELAY = 15;
    static int BOOSTTIME = 60;
    static int WINDOWWIDTH = 1000;
    static int WINDOWHEIGHT = 800;

    private static int BOTTOMTEXTYPOS = WINDOWHEIGHT - 50;

    // SETTINGS
    static int playersizeSetting = 7;
    static int speedSetting = 3;
    static int settingsSelection = 0;
    static int boostNumberSettings = 3;
    static int boostSpeedSettings = 7;
    Color player1Color = Color.red;
    Color player2Color = Color.blue;
    boolean player1Picked = false;
    static int playTill = 5;

    // SOUNDS
    static AudioClip crash;
    static AudioClip boostSound1;
    static AudioClip boostSound2;
    static AudioClip roundStart;
    static AudioClip cheer;


    //sprites
    //Image player1Up,player1Right,player1Down,player1Left;

//    {
//        try {
//            player1Up = ImageIO.read(getClass().getResource("sprites/Audi_Up.png"));
//            player1Right = ImageIO.read(getClass().getResource("sprites/Audi_Right.png"));
//            player1Down = ImageIO.read(getClass().getResource("sprites/Audi_Down.png"));
//            player1Left = ImageIO.read(getClass().getResource("sprites/Audi_Left.png"));
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * initializes game structures that are needed
     * makes doublebuffer so the image does not flicker when it refreshes and so the trails do not go away
     * draws the splash screen on the screen
     * initializes all sounds
     */
    public void init() throws InterruptedException {
        doubleBuffer = createImage(getWidth(),getHeight());
        doubleBufferGraphics = doubleBuffer.getGraphics();
        try {
            splashScreen = ImageIO.read(getClass().getResource("splash image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        doubleBufferGraphics.drawImage(splashScreen,0,0,this);

        //SOUNDS INIT
        crash = Applet.newAudioClip(getClass().getResource("Sounds/crash.wav"));
        boostSound1 = Applet.newAudioClip(getClass().getResource("Sounds/boostSound.wav"));
        boostSound2 = Applet.newAudioClip(getClass().getResource("Sounds/boostSound.wav"));
        roundStart = Applet.newAudioClip(getClass().getResource("Sounds/startCountDown.wav"));
        cheer = Applet.newAudioClip(getClass().getResource("Sounds/cheer.wav"));


        dt = new Timer(FRAMEDELAY, this);
        //needed to prevent a null exception error cause by repaint(). Error is silent/ does not cause anything but clogs up log
        try {
            repaint();
        } catch (Exception e) {
        }
    }

    /**
     * Method to make a color chooser window upon game getting called from settings menu. Sets player1 color and
     * player 2 color depending on what users choose. These variables are then used
     * later to set colors of multiple graphics.
     */
    private void chooseColor(){
        player1Picked = false;
        final JFrame colorChooserWindow = new JFrame();
        JPanel chooserContainer = new JPanel();
        final JColorChooser colorChooser = new JColorChooser(Color.red);
        colorChooserWindow.setBounds(0,0,650,450);

        final JLabel banner = new JLabel("Player 1 Choose Color",SwingConstants.CENTER);
        banner.setFont(new Font("Cambria",Font.BOLD, 30));
        chooserContainer.add(banner);
        chooserContainer.add(colorChooser);
        JButton next = new JButton("Next");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!player1Picked){
                    player1Color = colorChooser.getColor();
                    banner.setText("Player 2 Choose Color");
                    colorChooser.setColor(player2Color);
                    player1Picked =true;
                }
                else {
                    player2Color = colorChooser.getColor();
                    colorChooserWindow.setVisible(false);
                }
            }
        });
        chooserContainer.add(next);

        player1Color = colorChooser.getColor();
        colorChooserWindow.getContentPane().add(chooserContainer);
        colorChooserWindow.setVisible(true);

    }
    /**
     * This is my display for the settings page
     * It has current settings and can be changed
     * renders a ball to indicate what setting sis currently selected
     */
    public void settings(){
        doubleBuffer = createImage(getWidth(),getHeight());
        doubleBufferGraphics = doubleBuffer.getGraphics();
        ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        settings = true;

        doubleBufferGraphics.setColor(Color.white);
        doubleBufferGraphics.setFont(new Font("Cambria", Font.BOLD, 60));
        doubleBufferGraphics.drawString("SETTINGS",getWidth()/2 -130 ,85);
        doubleBufferGraphics.setFont(new Font("Times",Font.PLAIN,30));

        if (settingsSelection == 0) {
            doubleBufferGraphics.fillOval(30, 130, 25, 25);
        }
        else if(settingsSelection == 1){
            doubleBufferGraphics.fillOval(30, 204, 25, 25);
        }
        else if(settingsSelection == 2){
            doubleBufferGraphics.fillOval(30, 277, 25, 25);
        }
        else if (settingsSelection == 3){
            doubleBufferGraphics.fillOval(30, 352, 25, 25);
        }
        else if (settingsSelection == 4){
            doubleBufferGraphics.fillOval(30,425,25,25);
        }
        else if(settingsSelection == 5){
            doubleBufferGraphics.fillOval(30,499,25,25);
        }

        doubleBufferGraphics.drawString("Use arrow keys to go up and down and change settings (left right)", 40,650);
        doubleBufferGraphics.setColor(Color.red);
        doubleBufferGraphics.drawString("player speed: " + speedSetting, 70, 150);
        doubleBufferGraphics.drawString("player size: " + playersizeSetting,70,225);
        doubleBufferGraphics.drawString("Number of boosts: " + boostNumberSettings,70,300);
        doubleBufferGraphics.drawString("Boost Speed: " + boostSpeedSettings,70,375);
        doubleBufferGraphics.drawString("Play Till: " + playTill +" wins", 70, 450);
        doubleBufferGraphics.drawString("Change Player Colors -->", 70, 525);
        doubleBufferGraphics.setColor(Color.green);
        doubleBufferGraphics.setFont(new Font("Cambria",Font.BOLD,40));
        doubleBufferGraphics.drawString("Press Enter to start game!", 220,730);
        repaint();
    }

    /**
     * resets buffer image
     */
    public void resetBufferImage(){
        doubleBuffer = createImage(getWidth(),getHeight() - 50);
        doubleBufferGraphics = doubleBuffer.getGraphics();
        doubleBufferGraphics.clearRect(0,0,WINDOWWIDTH,WINDOWHEIGHT);
    }
    /**
     *starts a round, puts players in right spots, resets boosts, resets booleans and such
     * also clears the doubleBufferGraphics so that the old trails get deleted
     */
    public void roundStart(){
        if (!gameStarted){
            gameStarted = true;
        }
        resetBufferImage();
        repaint();

        //sets players initial positions
        player2.setPosition(WINDOWWIDTH - 200, (((WINDOWHEIGHT - 88) / 2) - (player2.getSidelength())));
        player2.setDirection(270);
        player2.setColor(player2Color);
        player2.setSideLength(playersizeSetting);
        player2.setBoostsLeft(boostNumberSettings);
        player2.setSpeed(speedSetting);
        boost1Hit = 0;
        player1.setPosition(200, (WINDOWHEIGHT - 88)/2 - (player2.getSidelength()));
        player1.setDirection(90);
        player1.setColor(player1Color);
        player1.setBoostsLeft(boostNumberSettings);
        player1.setSideLength(playersizeSetting);
        player1.setSpeed(speedSetting);
//        try {
//            player1.setSprite(ImageIO.read(getClass().getResource("sprites/Audi_Left.png")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        boost2Hit = 0;
        settings = false;
        player1Win = false;
        player2Win = false;
        if (gameWon){
            gameWon = false;
            player1.setScore(0);
            player2.setScore(0);
        }


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

        //puts antialiasing onto the text that is rendered on the doubleBufferGraphics
        ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawImage(doubleBuffer, 0, 0, this);


        if (gameStarted && !settings) {

            // didn't know how to get this to work with the aliasing above because it would not work in my draw method
            // so I just casted the same aliasing onto g and put the drawing code here
            ((Graphics2D)(g)).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            //sets font and draws the left portion of the bottom info bar
            g.setFont(new Font("Cambria", Font.BOLD, 30));
            g.setColor(player1Color);
            g.drawString("Player 1 Boosts: " + player1.getBoostsLeft(), 50, BOTTOMTEXTYPOS);
            g.drawString(String.valueOf(player1.getScore()), WINDOWWIDTH / 2 - 50, BOTTOMTEXTYPOS);

            //changes color and draws right portion of bottom info bar
            g.setColor(player2Color);
            g.drawString("Player 2 Boosts: " + player2.getBoostsLeft(), WINDOWWIDTH - 350, BOTTOMTEXTYPOS);
            g.drawString(String.valueOf(player2.getScore()), WINDOWWIDTH / 2 + 50, BOTTOMTEXTYPOS);
        }




    }

    /**
     * method that repaint calls
     * draws things onto window
     * @param g doublebufferGraphics should be passed here after being casted to Graphics2D
     */
    public void draw(Graphics2D g){
        if (!settings){
        g.setColor(Color.black);
        player2.draw(g);
        player1.draw(g);

        //displays which player has won and then stops frames.

            if ((player1Win || player2Win)&&!settings) {
                g.setFont(new Font("Cambria", Font.PLAIN, 45));
                g.setColor(Color.green);
                g.drawString("Press S to Change Settings!", 250, WINDOWHEIGHT - 100);
            }
            if (gameWon){
                g.setFont(new Font("Cambria",Font.BOLD,90));
                if (player1Win){
                    g.setColor(player1Color);
                    g.drawString("PLAYER 1 WINS GAME!!!",20,WINDOWHEIGHT/2 - 100);
                }
                if (player2Win){
                    g.setColor(player2Color);
                    g.drawString("PLAYER 2 WINS GAME!!!",20,WINDOWHEIGHT/2 - 100);
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cheer.play();

            }
            else if (player1Win) {
                g.setFont(new Font("Cambria", Font.BOLD, 50));
                g.setColor(Color.magenta);
                g.drawString("PLAYER 1 WINS ROUND", WINDOWWIDTH / 2 - 330, WINDOWHEIGHT / 2 - 50);
            }
            else if (player2Win) {
                g.setFont(new Font("Cambria", Font.BOLD, 50));
                g.setColor(Color.magenta);
                g.drawString("PLAYER 2 WINS ROUND", WINDOWWIDTH / 2 - 330, WINDOWHEIGHT / 2 - 50);
            }
        }

    }

    /**
     * what happens each frame
     * detects collision, determines if boost time is up and updates players position
     * @param e Each frame causes an actionevent
     */
    public void actionPerformed(ActionEvent e) {
        frames++;

        //what happens when a player collides
        if (player2.collison()) {
            crash.play();
            player1.setScore(player1.getScore() + 1);
            dt.stop();
            player1Win = true;
        } else if (player1.collison()) {
            crash.play();
            player2.setScore(player2.getScore() + 1);
            dt.stop();
            player2Win = true;
        }
        if (player1.getScore() == playTill || player2.getScore() == playTill){
            gameWon = true;
        }


        //sets player boosts to run for approx 50 frames from when boost button is hit
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
//        if (player1.getDirection() == 0 || player1.getDirection() == 360) {
////            player1.setSprite(player1Up);
////        }
////        else if (player1.getDirection() == 90){
////            player1.setSprite(player1Right);
////        }
////        else if (player1.getDirection() == 180){
////            player1.setSprite((player1Down));
////        }
////        else if (player1.getDirection() == 270){
////            player1.setSprite((player1Left));
////        }
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
        if (!singlePlayer) {
            return pixelColor.getBlue() == 0 && pixelColor.getRed() == 0 && pixelColor.getBlue() == 0;
        }
        else{
            return (pixelColor.getBlue() == 0 && pixelColor.getRed() == 0 && pixelColor.getBlue() == 0)|| (pixelColor.getBlue() == 255);
        }

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

    public void keyReleased(KeyEvent k) {
        int keyCode = k.getKeyCode();


        //ALL OF THE SETTINGS BUTTONS
        if (keyCode == KeyEvent.VK_RIGHT) {
            if (settings) {
                if (settingsSelection == 0) {
                    if (speedSetting < 10) {
                        speedSetting++;
                    }
                    if (speedSetting > playersizeSetting){
                        playersizeSetting = speedSetting;
                    }
                }
                if (settingsSelection == 1) {
                    if (playersizeSetting < 15) {
                        playersizeSetting++;
                    }
                }
                if (settingsSelection == 2) {
                    boostNumberSettings++;
                }
                if (settingsSelection == 3) {
                    if (boostSpeedSettings < 15) {
                        boostSpeedSettings++;
                    }
                }
                if (settingsSelection == 4){
                    if (playTill < 10){
                        playTill++;
                    }
                }
                if (settingsSelection == 5){
                    chooseColor();
                }
                settings();
            }
        }

            if (keyCode == KeyEvent.VK_LEFT) {
                if (settings) {
                    if (settingsSelection == 0) {
                        if (speedSetting > 2) {
                            speedSetting--;
                        }
                    }
                    if (settingsSelection == 1) {
                        if (playersizeSetting > 4) {
                            playersizeSetting--;
                        }
                        if (playersizeSetting < speedSetting){
                            speedSetting = playersizeSetting;
                        }
                    }
                    if (settingsSelection == 2) {
                        if (boostNumberSettings > 0) {
                            boostNumberSettings--;
                        }
                    }
                    if (settingsSelection == 3) {
                        if (boostSpeedSettings > 3) {
                            boostSpeedSettings--;
                        }
                    }
                    if (settingsSelection == 4){
                        if (playTill > 3) {
                            playTill--;
                        }
                    }
                    settings();
                }
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                if (settings) {
                    settingsSelection++;
                    settingsSelection = settingsSelection % 6;
                    settings();
                }
            }

            //have to put these here so boost method runs before decrement of boostsLeft
            if (keyCode == KeyEvent.VK_UP) {
                if (settings) {
                    settingsSelection--;
                    if (settingsSelection < 0) {
                        settingsSelection = 5;
                    }
                    settings();
                }
                else{
                    if (player2.getBoostsLeft() > 0) {
                        player2.setBoostsLeft(player2.getBoostsLeft() - 1);
                    }
                }
            }
            //END OF SETTINGS BUTTONS

            if (keyCode == KeyEvent.VK_W) {
                if (player1.getBoostsLeft() > 0) {
                    player1.setBoostsLeft(player1.getBoostsLeft() - 1);
                }
            }
            if (keyCode == KeyEvent.VK_S) {
                if (!gameStarted || player1Win || player2Win) {
                    settings();
                }
            }
            if (keyCode == KeyEvent.VK_ENTER) {
                if (!dt.isRunning()) {
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
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }

    public void keyTyped(KeyEvent k){}
}