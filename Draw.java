
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class Draw extends JPanel{
    int speed = 5;
    ArrayList<Mountain> mountains = new ArrayList<Mountain>();
    double[][] table = {
        {0.4, 0.6}, //width and peak of 1st mountain
        {0.4, 0.75}, // width and peak of 2nd mountain
        {0.4, 0.4}, // width and peak of 3rd mountain
        {0.4, 0.5}, // width and peak of 4th mountain
        {0.4, 0.8}, // width and peak of 5th mountain
        {0.4, 0.7}, // width and peak of 6th mountain
        {0.4, 0.3}, // width and peak of 7th mountain
        {0.4, 0.55}, // width and peak of 8th mountain
        {0.4, 0.35}, // width and peak of 9th mountain
        {0.4, 0.65} // width and peak of 10th mountain
    };
    Random random = new Random();
    int randomIndex;

    public void draw(){
        repaint();
        Timer timer = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(getWidth() == 0){
                    return;
                }
                for(Mountain mountain : mountains){
                    mountain.move(speed);
                }
                mountains.removeIf(mountain -> mountain.start + mountain.width * getWidth() < 0);

                int edge = 0;

                if(!mountains.isEmpty()){
                    Mountain lastMountain = mountains.get(mountains.size()-1);
                    edge = (int)(lastMountain.start) + (int)(lastMountain.width * getWidth());
                }

                while(edge < getWidth()){
                    randomIndex = random.nextInt(table.length);
                    mountains.add(new Mountain(edge, table[randomIndex][1], table[randomIndex][0]));
                    edge += (int)(table[randomIndex][0] * getWidth());
                }

                repaint();
            }
        });
        timer.start();
    }

    @Override 
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int height = getHeight();
        int width = getWidth();

        //colorset and drawing for ground
        g.setColor(Color.decode("#87eba7"));
        g.fillRect(0, (int)(height-(0.2)*height), width, (int)(0.2*height));

        //colorset and drawing for mountains
        g.setColor(Color.decode("#8B4513"));

        for(int i = 0; i < mountains.size(); i++){
            Mountain mountain = mountains.get(i);
            int mountainStart = (int)(mountain.start);
            int mountainPeak = (int)(mountain.peak * height);
            int mountainWidth = (int)(mountain.width * width);
            int mid = mountainStart + mountainWidth/2;
            int end = mountainStart + mountainWidth;
            int xpoints[] = {mountainStart, mid, end};
            int ypoints[] = {(int)(height-(0.2)*height), (int)(height-mountainPeak), (int)(height-(0.2)*height)};
            g.fillPolygon(xpoints, ypoints, 3);
        }


    }




}