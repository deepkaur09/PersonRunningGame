public class Energy {
    double posX; //starting pos for energy
    double width = 0.04; //width of energy
    double height = 0.04; //height of energy
    int posOnMountain; //pos energy should be above ground

    public Energy(double posX){
        this.posX = posX;
    }

    public void move(int speed){
        this.posX -= speed;
    }
    
}
