public class Player {
    final int x = 100; //x position of player
    int y; //y position of player

    int wide, tall;
    
    Player(int wide, int tall){
        this.wide = wide;
        this.tall = tall;
    }

    public void update(int y){
        this.y = y;
    }
    
}
