public class Mountain {
    double start;
    double peak;
    double width;

    Mountain(double start, double peak, double width){
        this.start = start;
        this.peak = peak;
        this.width = width;
    }

    public void move(int speed){
        this.start -= speed;
    }
}
