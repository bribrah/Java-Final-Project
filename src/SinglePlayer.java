import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * created by Brian Espinosa
 * Single player version of game
 * inherits many of the methods from the multiplayer version of the game
 * new methods are mostly concerned with drawing of the levels
 */
public class SinglePlayer extends Game {

    Player player = new Player();
    private int level = 1;
    private int points = 0;
    private int lives = 0;
    private boolean levelStarted = false;
    private int pointsOnLevel;
    private boolean gameStarted = false;

    private AudioClip point;
    private Image splashScreen;

    //LEVELS
    int[] pointGottenArray;

    private Image[] levelArray;

    private static Image level1;
    private static Image level2;
    private static Image level3;
    private static Image level4;

    //satisfies array portion of reqs
    private int[] level1PointCoords = new int[]{200, 200, WINDOWWIDTH - 100, 300, 500, 500};
    private int[] level2PointCoords = new int[]{550, 234, 47, 57, 896, 610};
    private int[] level3PointCoords = new int[]{535, 358, 412, 383, 38, 365, 470, 113, 354, 653};
    private int[] level4PointCoords = new int[]{630, 324, 828, 89, 865, 214};

    private int[][] pointCoordsArray = new int[][]{level1PointCoords, level2PointCoords, level3PointCoords, level4PointCoords};

    /**
     * loads all resources that require loading in
     *
     * @throws IOException
     */
    public void loadResources() throws IOException {
        splashScreen = ImageIO.read(getClass().getResource("splash image singleplayer.png"));
        level1 = ImageIO.read(getClass().getResource("Levels/level1.png"));
        level2 = ImageIO.read(getClass().getResource("Levels/level2.png"));
        level3 = ImageIO.read(getClass().getResource("Levels/level3.png"));
        level4 = ImageIO.read(getClass().getResource("Levels/level4.png"));
        point = Applet.newAudioClip(getClass().getResource("Sounds/point.wav"));
        levelArray = new Image[]{level1, level2, level3, level4};
    }

    /**
     * calls Game.init and also loads resources
     *
     * @throws InterruptedException
     */
    public void init() throws InterruptedException {
        super.init();
        singlePlayer = true;

        try {
            loadResources();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doubleBufferGraphics.drawImage(splashScreen, 0, 0, this);
    }

    /**
     * had to create a new settings menu for single player because it has a lot less settings
     */
    public void settings() {
        doubleBuffer = createImage(getWidth(),getHeight());
        doubleBufferGraphics = doubleBuffer.getGraphics();
        ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        settings = true;

        doubleBufferGraphics.setColor(Color.white);
        doubleBufferGraphics.setFont(new Font("Cambria", Font.BOLD, 60));
        doubleBufferGraphics.drawString("SETTINGS",getWidth()/2 -130 ,85);

        if (settingsSelection == 0) {
            doubleBufferGraphics.fillOval(30, 130, 25, 25);
        }
        else if(settingsSelection == 1){
            doubleBufferGraphics.fillOval(30, 204, 25, 25);
        }

        doubleBufferGraphics.setFont(new Font("Times", Font.PLAIN, 33));
        doubleBufferGraphics.setColor(Color.red);
        doubleBufferGraphics.drawString("player directional controls: " + player.isDirectionalTurning(), 70, 150);
        doubleBufferGraphics.drawString("Change Player Color (player 1 color) -->: ", 70, 225);
        doubleBufferGraphics.drawString("Press Enter to start game!", 220,730);

        repaint();


    }

    /**
     * Method that is ran everytime round is started
     * resets player position and points gotten logic, also resets double buffer graphics by calling
     * resetlevel. if lives = 0 game starts over.
     */
    public void roundStart() {
        if (!gameStarted){
            gameStarted = true;
        }
        resetBufferImage();
        ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        repaint();

        //sets players initial positions
        settings = false;
        player.setPosition(924, 362);
        player.setDirection(270);
        player.setColor(player1Color);
        player.setSideLength(10);
        player.setSpeed(4);
        points = 0;
        if (lives == 0) {
            level = 1;
            lives = 5;
        }
        resetLevel();

        levelStarted = true;
    }

    /**
     * draws level based on current level and resets point gotten array.
     */

    public void resetLevel() {
        pointGottenArray = new int[100];
        doubleBufferGraphics.drawImage(levelArray[level - 1], 0, 0, this);

    }

    /**
     * calls draw and paints the things that are there
     * also draws the bottom text
     *
     * @param g the graphics object that is used to draw onto the doubleBuffer image
     */
    public void paint(Graphics g) {
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
            g.drawString("Level: " + level, 50, BOTTOMTEXTYPOS);
            g.drawString("Points needed: " + (pointsOnLevel - points), 600, BOTTOMTEXTYPOS);
            g.drawString("Lives: " + lives, (300), BOTTOMTEXTYPOS);

        }

    }

    /**
     * called when repain is called
     * draws player and points
     *
     * @param g doublebufferGraphics should be passed here after being casted to Graphics2D
     */
    public void draw(Graphics2D g) {
        if (!settings) {
            if (levelStarted && level <= levelArray.length) {
                levelDraw(g, pointCoordsArray[level - 1]);
            }
            player.draw(g);

        }

    }

    /**
     * logic to draw the points associated with whatever current level is
     *
     * @param g
     * @param pointArray the coordinate array of the level.
     */
    public void levelDraw(Graphics2D g, int[] pointArray) {
        pointsOnLevel = pointArray.length / 2;

        //satisfies loop portion of reqs
        for (int i = 0; i < pointArray.length; i += 2) {
            g.setColor(Color.blue);
            g.fillRect(pointArray[i], pointArray[i + 1], 15, 15);
            if (pointGottenArray[i] == 1) {
                g.setColor(Color.magenta);
                g.fillRect(pointArray[i], pointArray[i + 1], 15, 15);
            }
        }

    }


    /**
     * called every frame
     * detects if collision, updates player position and repaints.
     *
     * @param e Each frame causes an actionevent
     */
    public void actionPerformed(ActionEvent e) {
        player.update();

        //what happens when a player collides
        if (player.collison()) {
            lives--;
            crash.play();
            if (lives == 0) {
                doubleBufferGraphics.setFont(new Font("Cambria", Font.BOLD, 60));
                doubleBufferGraphics.setColor(Color.blue);
                doubleBufferGraphics.drawString("You Lose! :(", 300, WINDOWHEIGHT / 2 - 60);
                doubleBufferGraphics.setFont(new Font("Cambria", Font.PLAIN, 45));
                doubleBufferGraphics.setColor(Color.green);
                doubleBufferGraphics.drawString("Press S to Change Settings!", 250, WINDOWHEIGHT - 100);
                gameStarted = false;
            }
            dt.stop();
        }
        repaint();
        pointCollisions(pointCoordsArray[level - 1]);

    }


    /**
     * logic to draw the hitboxes for the current levels points
     *
     * @param pointCoords the point coordinate array of the current level
     */
    public void pointCollisions(int[] pointCoords) {
        for (int i = 0; i < pointCoords.length; i += 2) {
            if (player.pointCollision(pointCoords[i], pointCoords[i + 1]) && pointGottenArray[i] != 1) {
                pointGottenArray[i] = 1;
                point.play();
                points++;
            }
        }
        if (points == pointsOnLevel) {
            winMessage();
            cheer.play();
            dt.stop();
            level++;

        }
    }

    /**
     * displays win message when player beats a level
     */
    public void winMessage() {
        if (level > levelArray.length) {
            resetBufferImage();
            doubleBufferGraphics.setFont(new Font("Cambria", Font.BOLD, 60));
            doubleBufferGraphics.setColor(Color.orange);
            doubleBufferGraphics.drawString("CONGRATULATIONS YOU WIN!", 100, WINDOWHEIGHT / 2 - 60);
        } else {
            doubleBufferGraphics.setColor(Color.magenta);
            doubleBufferGraphics.setFont(new Font("Cambira", Font.BOLD, 50));
            doubleBufferGraphics.drawString("LEVEL " + level + " COMPLETED", 250, WINDOWHEIGHT / 2 - 50);
        }
        doubleBufferGraphics.setColor(Color.red);
    }

    /**
     * players controls
     *
     * @param k sent from keyListener
     */
    public void keyPressed(KeyEvent k) {

        int keyCode = k.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            if (player.isDirectionalTurning()) {
                player.directionalTurn(270);
            } else {
                player.turnLeft();
            }

        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            if (player.isDirectionalTurning()) {
                player.directionalTurn(90);
            } else {
                player.turnRight();
            }
        }
        if (player.isDirectionalTurning() && !settings) {
            if (keyCode == KeyEvent.VK_UP) {
                player.directionalTurn(0);
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                player.directionalTurn(180);
            }
        }

    }

    /**
     * used for settings controls
     * @param k sent from keyListener
     */
    public void keyReleased(KeyEvent k) {
        super.keyReleased(k);
        int keycode = k.getKeyCode();
        if (keycode == KeyEvent.VK_S && !gameStarted){
            settings();
        }
        if (settings){
            if (keycode == KeyEvent.VK_RIGHT){
                if (settingsSelection == 0){
                    player.setDirectionalTurning();
                }
                else if (settingsSelection == 1){
                    chooseColor();
                }
                settings();
            }
        }
    }
}

