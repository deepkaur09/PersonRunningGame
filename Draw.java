
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

        /* 

        int x1points[] = {(int)(width*0.1) - offset, (int)(width*0.3) - offset, (int)(width*0.5) - offset};
        int y1points[] = {(int)(height-(0.2)*height), (int)(height-(0.6)*height), (int)(height-(0.2)*height)};
        g.fillPolygon(x1points, y1points, 3);

        int x2points[] = {(int)(width*0.7) - offset, (int)(width*0.9) - offset, (int)(width*1.1) - offset};
        int y2points[] = {(int)(height-(0.2)*height), (int)(height-(0.6)*height), (int)(height-(0.2)*height)};
        g.fillPolygon(x2points, y2points, 3);

        int x3points[] = {(int)(width*1.3) - offset, (int)(width*1.5) - offset, (int)(width*1.7) - offset};
        int y3points[] = {(int)(height-(0.2)*height), (int)(height-(0.8)*height), (int)(height-(0.2)*height)};
        g.fillPolygon(x3points, y3points, 3);

        int x4points[] = {(int)(width*1.9) - offset, (int)(width*2.1) - offset, (int)(width*2.3) - offset};
        int y4points[] = {(int)(height-(0.2)*height), (int)(height-(0.6)*height), (int)(height-(0.2)*height)};
        g.fillPolygon(x4points, y4points, 3);

        int x5points[] = {(int)(width*2.5) - offset, (int)(width*2.7) - offset, (int)(width*2.9) - offset};
        int y5points[] = {(int)(height-(0.2)*height), (int)(height-(0.7)*height), (int)(height-(0.2)*height)};
        g.fillPolygon(x5points, y5points, 3);

        */


    }




}