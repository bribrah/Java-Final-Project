import java.awt.Color;
import java.awt.*;
import java.awt.Graphics2D;

public class Player {

    //setPosition of player on the board
    private int xPos;
    private int yPos;
    private int direction;
    private Color color;
    private int sideLength;
    //how fast the car is moving (used for boost)
    private int speed;
    private int boostsLeft;

    //car sprite
    private Image sprite;


    //////////////////CONSTRUCTORS/////////////

    public Player() {
        this.xPos = 0;
        this.yPos = 0;
        this.direction = 0;
        this.speed = 3;
        this.sideLength = 7;
        this.boostsLeft = 3;
    }
    public Player(int direction){
        this.xPos = 0;
        this.yPos = 0;
        this.direction = direction;
        this.speed = 3;
        this.sideLength = 7;
    }

    //////////getters////////

    public int getSidelength() {
        return this.sideLength;
    }


    public void setSideLength(int num){this.sideLength = num;}
    ///////////Methods////////

    /*
    method to turn the players left or right
    each time method is called player is turrned 90 degrees
    also sets the players width and height depending on setDirection
     */
    public void turnRight() {
        int direction = this.direction + 90;
        if (direction > 360) {
            this.direction = direction % 360;
        } else {
            this.direction = direction;
        }
    }

    public void turnLeft() {
        int direction = this.direction - 90;
        if (direction <= 0) {
            this.direction = 360;
        } else {
            this.direction = direction;
        }
    }

    //sets color of player and players trail
    public void setColor(Color color) {
        this.color = color;
    }

    //sets player setPosition in arena
    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    //sets players setDirection
    public void setDirection(int heading) {
        this.direction = heading;
    }

    //gets boosts left
    public int getBoostsLeft(){return this.boostsLeft;}
    //sets boost bar
    public void setBoostsLeft(int boost){this.boostsLeft = boost;}


    //draws the player
    public void draw(Graphics2D g) {
        g.setColor(this.color);
        g.fillRect(this.xPos, this.yPos, 7, 7);
    }

    //updates the players setPosition based on its current setDirection
    public void update() {
        if (this.direction == 0 || this.direction == 360) {
            this.yPos -= speed;
        } else if (this.direction == 90) {
            this.xPos += speed;
        } else if (this.direction == 180) {
            this.yPos += speed;
        } else if (this.direction == 270) {
            this.xPos -= speed;
        }
    }

    //speeds up player for a short duration
    public void boost() {
        if (this.boostsLeft + 1 > 0) {
            this.speed = 5;
        }
    }
    public void boostStop(){
        this.speed = 3;
    }

    public boolean collison() {
        if (this.xPos > Game.WINDOWWIDTH - this.sideLength - 20 || this.xPos < 5 || this.yPos > Game.WINDOWHEIGHT - this.sideLength - 42|| this.yPos < 5){
            return true;
        }
        else if (this.direction == 0 || this.direction == 360) {
            if (!Game.isEmpty(this.xPos, this.yPos - 1)) {
                return true;
            } else {
                return false;
            }

        } else if (this.direction == 90) {
            if (!Game.isEmpty(this.xPos + this.sideLength + 1, this.yPos)) {
                return true;
            } else {
                return false;
            }
        } else if (this.direction == 180) {
            if (!Game.isEmpty(this.xPos, this.yPos + this.sideLength + 1)) {
                return true;
            } else {
                return false;
            }
        } else if (this.direction == 270) {
            if (!Game.isEmpty(this.xPos - 1, this.yPos)) {
                return true;
            } else {
                return false;
            }
        }
        else{
                return false;
            }

        }
    }


