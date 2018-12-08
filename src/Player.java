import java.awt.Color;
import java.awt.*;
import java.awt.Graphics2D;

class Player {

    //setPosition of player on the board
    private int xPos;
    private int yPos;
    private int lastX;
    private int lastY;
    private int direction;
    private Color color;
    private int sideLength;
    //how fast the car is moving (used for boost)
    private int speed;
    private int boostsLeft;
    private int score = 0;
    private boolean directionalTurning = true;


    //////////////////CONSTRUCTORS/////////////

    public Player() {
        this.xPos = 0;
        this.yPos = 0;
        this.direction = 0;
        this.speed = 3;
        this.sideLength = 7;
        this.boostsLeft = 3;
    }

    //////////GETTERS and SETTERS////////

    /**
     * gives the players sideLength
     * @return the pixel count of the players sideLength
     */
    public int getSidelength() {
        return this.sideLength;
    }


    public void setSideLength(int num){this.sideLength = num;}
    /**
     * sets the color of the player and player's trail
     * @param color A color that the player will be
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * sets players x and y position associated with player
     * @param x the x pos of player
     * @param y the y pos of player
     */
    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }
    public int getxPos(){
        return this.xPos;
    }

    public int getyPos(){
        return this.yPos;
    }

    /**
     * Sets direction player is pointing in
     * @param heading the degree which the plasyer is pointing in
     */
    public void setDirection(int heading) {
        this.direction = heading;
    }

    /**
     * returns players direction angle
     * @return andle that the player is currently directed at
     */
    public int getDirection(){
        return this.direction;
    }
    /**
     * gives how many boosts a player has left
     * @return how many boosts a player has left
     */
    public int getBoostsLeft(){return this.boostsLeft;}

    /**
     * sets how many boost a player can use
     * @param boost how many boosts the player can use
     */
    public void setBoostsLeft(int boost){this.boostsLeft = boost;}


    /**
     * returns player score
     * @return players score
     */
    public int getScore(){
        return this.score;
    }

    /**
     * sets player score
     * @param score sets players score to integer passed
     */
    public void setScore(int score){
        this.score = score;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public int getSpeed(){
        return this.speed;
    }

    public boolean isDirectionalTurning(){
        return this.directionalTurning;
    }

    public void setDirectionalTurning(){
        this.directionalTurning = !this.directionalTurning;
    }
    ///////////Methods////////

    /**
     * method to turn the players left or right
     * each time method is called player is turrned 90 degrees
     * also sets the players width and height depending on setDirection
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

    /**
     * changes players direction but makes sure that it doesn't run into itself
     * @param num angle of heading
     */
    public void directionalTurn(int num){
        if (Math.abs(this.direction - num) != 180){
            this.direction = num;
        }
    }

    /**
     * Draws the player on the graphics2D User Space based on its current color and xpos, ypos, and sidelength
     * The player is drawn as a square
     * @param g the user space where the player is to be drawn
     */
    public void draw(Graphics2D g) {
        g.setColor(this.color);
        g.fillRect(this.xPos, this.yPos, this.sideLength, this.sideLength);
    }

    /**
     * updates the players position by changing the x and y positions
     * the change is based on speed and direction of player
     */
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

    /**
     * speeds up the player by changing it speed
     * only does something if the player has at least one boost left
     * @param boostSpeed the speed that the player will be during the boost
     */
    public void boost(int boostSpeed) {
        if (this.boostsLeft > 0) {
            this.speed = boostSpeed;
        }
    }

    /**
     * sets speed back to normal value
     * @param normalSpeed the speed before boost should be passed here
     */
    public void boostStop(int normalSpeed){
        this.speed = normalSpeed;
    }


    /**
     * Detects collison based on what direction player is currently pointed in
     * @return returns true if collision is detected, returns false if not
     */
    public boolean collison() {
        if (this.xPos > Game.WINDOWWIDTH - this.sideLength - 20 || this.xPos < 5 ||
                this.yPos > Game.WINDOWHEIGHT - this.sideLength - 92|| this.yPos < 5){
            return true;
        }
        else if (this.direction == 0 || this.direction == 360) {
            return !Game.isEmpty(this.xPos, this.yPos - 1) ||
                    !Game.isEmpty(this.xPos + sideLength, this.yPos - 1);

        } else if (this.direction == 90) {
            return !Game.isEmpty(this.xPos + this.sideLength + 1, this.yPos)||
                    !Game.isEmpty(this.xPos + this.sideLength + 1, this.yPos + this.sideLength);
        } else if (this.direction == 180) {
            return !Game.isEmpty(this.xPos, this.yPos + this.sideLength + 1)||
                    !Game.isEmpty(this.xPos+sideLength,this.yPos + this.sideLength + 1);
        } else if (this.direction == 270) {
            return !Game.isEmpty(this.xPos - 1, this.yPos)||
                    !Game.isEmpty(this.xPos -1, this.yPos + sideLength);
        }
        else{
            return false;
        }
    }


    public boolean pointCollision(int x,int y){
        return (xPos > x && xPos < x + 15 && yPos > y && yPos < y + 15) ||
                (xPos + sideLength > x && xPos < x + 15 && yPos + sideLength > y && yPos < y + 15);
    }
}


