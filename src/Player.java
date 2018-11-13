public class Player {

    //position of player on the board
    private int xPos;
    private int yPos;
    private int direction;

    //area of car
    private int area;

    //how fast the car is moving (used for boost)
    private int speed;


    //////////////////CONSTRUCTORS/////////////

    public Player(){
        this.xPos = 0;
        this.yPos = 0;
        this.direction = 0;
        this.area = 0;
        this.speed = 0;
    }

    public Player(int xpos,int ypos,int direction){
        this.xPos = xpos;
        this.yPos = ypos;
        this.direction = direction;
        this.speed = 4;
        this.area = 20;
    }

}
