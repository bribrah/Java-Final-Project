import java.awt.Color;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;
public class Player {

    //position of player on the board
    private int xPos;
    private int yPos;
    private int direction;

    //how fast the car is moving (used for boost)
    private int speed;

    //car sprite
    private Image sprite;


    //////////////////CONSTRUCTORS/////////////

    public Player(){
        this.xPos = 0;
        this.yPos = 0;
        this.direction = 0;
        this.speed = 0;
    }

    public Player(int xpos,int ypos,int direction){
        this.xPos = xpos;
        this.yPos = ypos;
        this.direction = direction;
        this.speed = 4;
    }



    ///////////Methods////////

    /*
    method to turn the players left or right
    each time method is called player is turrned 90 degrees
     */
    public void turnRight(){
        int direction = this.direction + 90;
        this.direction = direction % 360;
    }
    public void turnLeft(){
        int direction = this.direction - 90;
        if (direction == 0){
            this.direction = 360;
        }
        else{
            this.direction = direction;
        }
    }

    //sets player position in arena
    public void position(int x, int y){
        this.xPos = x;
        this.yPos = y;
    }
    public void direction(int heading){
        this.direction = heading;
    }

    public void setSprite(ImageIcon sprite){
        this.sprite = sprite.getImage();
    }

//    public void draw(Graphics2D g){
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawImage(this.sprite,this.xPos,this.yPos,null);
//    }

    public void draw(Graphics2D g) {
        g.setColor(Color.blue);
        g.fillRect(xPos,yPos,40,40);
 }

}
