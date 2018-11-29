import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel{
    private JButton PLAYButton;

    public MainMenu() {
        PLAYButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                roundStart();
            }
        });
    }
}
