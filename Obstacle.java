
public class Obstacle {
    double start; //starting pos for obstacle
    int screenY; //top edge of the obstacle in pixels
    double obstacleWidth; ///width of an obstacle
    double obstacleHeight; //height of an obsracle
    double heightAboveGround = 0; //pos obstacles should be above ground

    public Obstacle(double start, double obstacleWidth, double obstacleHeight){
        this.start = start;
        this.obstacleWidth = obstacleWidth;
        this.obstacleHeight = obstacleHeight;
    }

    //gives look of moving obstacles
    public void move(int speed){
        this.start -= speed;
    }

    //seeting the height of obstacle above the ground
    public void setHeight(double heightAboveGround){
        this.heightAboveGround = heightAboveGround;
    }
}
