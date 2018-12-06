import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Brian Espinosa
 * Class that holds main method.
 */
public class RunGame
{

    /**
     *makes game window and adds keylistener for keystrokes
     * adds Game to gameWindow
     * initializes game by calling game.init();
     */
public static void main(String[] args) throws InterruptedException {

    final JFrame singleOrMultiplayer = new JFrame("Single or Multipler Choice");
    singleOrMultiplayer.setBounds(0,0,500,200);
    JPanel buttonContainer = new JPanel();
    JButton multiPlayerButton = new JButton("MultPlayer");
    JButton singlePlayerButton = new JButton("SinglePlayer");
    multiPlayerButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            singleOrMultiplayer.setVisible(false);
            JFrame gameWindow = new JFrame("Burnout Battle");
            gameWindow.setBounds(0,0,Game.WINDOWWIDTH,Game.WINDOWHEIGHT);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.setResizable(false);
            gameWindow.setBackground(Color.black);

            Game game = new Game();
            gameWindow.getContentPane().add(game);

            gameWindow.setVisible(true);
            gameWindow.addKeyListener(game);
            try {
                game.init();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    });
    singlePlayerButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            singleOrMultiplayer.setVisible(false);
            JFrame gameWindow = new JFrame("Burnout Battle");
            gameWindow.setBounds(0,0,Game.WINDOWWIDTH,Game.WINDOWHEIGHT);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.setResizable(false);
            gameWindow.setBackground(Color.black);

            SinglePlayer game = new SinglePlayer();
            gameWindow.getContentPane().add(game);

            gameWindow.setVisible(true);
            gameWindow.addKeyListener(game);
            try {
                game.init();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    });
    buttonContainer.add(multiPlayerButton);
    buttonContainer.add(singlePlayerButton);
    singleOrMultiplayer.getContentPane().add(buttonContainer);
    singleOrMultiplayer.setVisible(true);

}
}


