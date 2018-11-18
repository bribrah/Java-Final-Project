import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JButton Play;
    private JButton settingsButton;
    private JButton controlsButton;

    public void init(){
//        this.Play;
//        this.settingsButton;
//        this.controlsButton;
    }

    public MainMenu() {
    }





    //////////methods//////
    public void play (JFrame gameWindow, Game game){
        gameWindow.getContentPane().add(game);
        game.init();
    }
}

