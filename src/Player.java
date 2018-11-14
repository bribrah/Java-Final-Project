import java.awt.Color;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Player {

    //position of player on the board
    private int xPos;
    private int yPos;
    private int direction;
    private Color color;
    private int width;
    private int height;

    //how fast the car is moving (used for boost)
    private int speed;

    //car sprite
    private Image sprite;


    //////////////////CONSTRUCTORS/////////////

    public Player(){
        this.xPos = 0;
        this.yPos = 0;
        this.direction = 0;
        this.speed = 4;
        this.width = 25;
        this.height = 15;
    }

    public Player(int xpos,int ypos,int direction){
        this.xPos = xpos;
        this.yPos = ypos;
        this.width = 25;
        this.height = 15;
        this.direction = direction;
        this.speed = 4;
    }


    //////////getters////////

    public int getHeight(){
        return this.height;
    }

    ///////////Methods////////

    /*
    method to turn the players left or right
    each time method is called player is turrned 90 degrees
     */
    public void turnRight(){
        int direction = this.direction + 90;
        if (direction > 360) {
            this.direction = direction % 360;
        }
        else{
            this.direction = direction;
        }
    }
    public void turnLeft(){
        int direction = this.direction - 90;
        if (direction <= 0){
            this.direction = 360;
        }
        else{
            this.direction = direction;
        }
    }
    public void setColor(Color color){
        this.color = color;
    }

    //sets player position in arena
    public void position(int x, int y){
        this.xPos = x;
        this.yPos = y;
    }
    public void direction(int heading){
        this.direction = heading;
    }


    public void draw(Graphics2D g){
        g.setColor(this.color);
        g.fillRect(this.xPos,this.yPos,this.width,this.height);
    }

    public void update(){
        if (this.direction == 0 || this.direction == 360){
            this.yPos -= speed;
            this.height = 25;
            this.width = 15;
        }
        else if (this.direction == 90){
            this.xPos += speed;
            this.height = 15;
            this.width = 25;
        }
        else if (this.direction == 180){
            this.yPos += speed;
            this.height = 25;
            this.width = 15;
        }
        else if (this.direction == 270){
            this.xPos -= speed;
            this.height = 15;
            this.width = 25;
        }
        else{
            this.xPos += speed;
        }
    }
}
