import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SinglePlayer extends Game {

    private Player player = new Player();
    private int playerBoostHit;

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
    player.setSpeed(speedSetting);
//        try {
//            player1.setSprite(ImageIO.read(getClass().getResource("sprites/Audi_Left.png")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    settings = false;
    }

    public void paint (Graphics g){
        super.paint(g);
        draw((Graphics2D) doubleBufferGraphics);

        //puts antialiasing onto the text that is rendered on the doubleBufferGraphics
        ((Graphics2D) doubleBufferGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.drawImage(doubleBuffer, 0, 0, this);
    }

    public void draw(Graphics2D g){
        if (!settings){
            g.setColor(Color.black);
            player.draw(g);

            //displays which player has won and then stops frames.
            }
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

