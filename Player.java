public class Player {
    final int x = 100; //x position of player
    int y; //y position of player
    double heightAboveSurface = 0; //height of player from the mountain
    double velocityY = 0; //velocity of player in y direction
    boolean ducking = false; //the player is not ducked yet

    int wide, tall;
    
    Player(int wide, int tall){
        this.wide = wide;
        this.tall = tall;
    }

    public void update(int y){
        this.y = y;
    }
    
}
