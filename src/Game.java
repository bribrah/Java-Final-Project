import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel implements KeyListener, ActionListener{

    private static String gameTitle = "Burnout Battle";
    private Timer dt;
    private static Image doubleBuffer;
    private Graphics doubleBufferGraphics;

    public static void main(String[] args){
        JFrame gameWindow = new JFrame(gameTitle);
        gameWindow.setBounds(0,0,1920,1080);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);

        Game game = new Game();
        gameWindow.getContentPane().add(game);
        gameWindow.setBackground(Color.BLACK);
        gameWindow.setVisible(true);
        gameWindow.addKeyListener(game);

    }
}