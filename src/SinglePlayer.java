import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.io.IOException;

public class SinglePlayer extends Game {

    private Player player = new Player();
    private int playerBoostHit;
    private int level = 1;
    private int points = 0;
    private boolean levelStarted = false;
    private boolean point1Got = false;
    private boolean point2Got = false;
    private boolean point3Got = false;
    private int pointsOnLevel;
    private AudioClip point;

    //LEVELS
    int[] pointGottenArray;

    private Image[] levelArray;

    private static Image level1;
    private static Image level2;
    private static Image level3;

    private int[] level1PointCoords= new int[] {200,200,WINDOWWIDTH-100,300,500,500};
    private int[] level2PointCoords = new int[] {550,234,47,57,896, 610};
    private int[] level3PointCoords = new int[] {535,358,412,383,38,365,470,113,354,653};

    private int[][] pointCoordsArray = new int[][] {level1PointCoords,level2PointCoords,level3PointCoords};

    public void loadResources() throws IOException {
        level1 = ImageIO.read(getClass().getResource("Levels/level1.png"));
        level2 = ImageIO.read(getClass().getResource("Levels/level2.png"));
        level3 = ImageIO.read(getClass().getResource("Levels/level3.png"));
        point = Applet.newAudioClip(getClass().getResource("Sounds/point.wav"));
        levelArray = new Image[] {level1,level2,level3};
    }

    public void init() throws InterruptedException {
        super.init();
        singlePlayer = true;
        try {
            loadResources();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void roundStart(){
    resetBufferImage();
    ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    repaint();

    //sets players initial positions
    player.setPosition(924, 362);
    player.setDirection(270);
    player.setColor(player1Color);
    player.setSideLength(10);
    player.setSpeed(3);
    points = 0;
    point1Got = false;
    point2Got = false;
    point3Got =false;
    resetLevel();

    levelStarted = true;
    }


    public void resetLevel(){
        pointGottenArray =  new int[100];
        doubleBufferGraphics.drawImage(levelArray[level - 1],0,0,this);

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
            g.drawString("Level: " + level, 50,BOTTOMTEXTYPOS);
            g.drawString("Points needed: " + (pointsOnLevel - points), 600, BOTTOMTEXTYPOS);
        }
    }

    public void draw(Graphics2D g){
        if (!settings) {
            if (levelStarted) {
                levelDraw(g, pointCoordsArray[level -1]);
            }
            g.setColor(Color.black);
            player.draw(g);
            }

    }

    public void levelDraw(Graphics2D g, int[] pointArray){
        pointsOnLevel = pointArray.length /2;
        for (int i = 0; i<pointArray.length; i+= 2){
            g.setColor(Color.blue);
            g.fillRect(pointArray[i],pointArray[i+1],15,15);
            if (pointGottenArray[i] == 1){
                g.setColor(Color.magenta) ;
                g.fillRect(pointArray[i],pointArray[i+1],15,15);
            }
        }
    }


    public void actionPerformed(ActionEvent e) {
        player.update();

        //what happens when a player collides
        if (player.collison()){
            crash.play();
            dt.stop();
        }
        pointCollisions(pointCoordsArray[level - 1]);
        repaint();

    }


    public void pointCollisions(int[] pointCoords){
        for (int i = 0; i < pointCoords.length; i+= 2){
            if (player.pointCollision(pointCoords[i],pointCoords[i+1]) && pointGottenArray[i] != 1){
                pointGottenArray[i] = 1;
                point.play();
                points++;
            }
        }
        if (points == pointsOnLevel){
            winMessage();
            dt.stop();
            level++;
        }
    }

    public void winMessage(){
        doubleBufferGraphics.setColor(Color.magenta);
        doubleBufferGraphics.setFont(new Font("Cambira",Font.BOLD,50));
        doubleBufferGraphics.drawString("LEVEL " + level + " COMPLETED", 250,WINDOWHEIGHT/2-50);
        doubleBufferGraphics.setColor(Color.red);
    }
    public void keyPressed(KeyEvent k){
        int keyCode = k.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT){
            player.turnLeft();
        }
        else if (keyCode == KeyEvent.VK_RIGHT){
            player.turnRight();
        }

    }
    }

