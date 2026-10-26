
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class Draw extends JPanel {
    int speed = 5;
    ArrayList<Mountain> mountains = new ArrayList<Mountain>();

    // creating a player object
    Player player;

    double[][] table = {
            { 0.4, 0.6 }, // width and peak of 1st mountain
            { 0.4, 0.75 }, // width and peak of 2nd mountain
            { 0.4, 0.4 }, // width and peak of 3rd mountain
            { 0.4, 0.5 }, // width and peak of 4th mountain
            { 0.4, 0.8 }, // width and peak of 5th mountain
            { 0.4, 0.7 }, // width and peak of 6th mountain
            { 0.4, 0.3 }, // width and peak of 7th mountain
            { 0.4, 0.55 }, // width and peak of 8th mountain
            { 0.4, 0.35 }, // width and peak of 9th mountain
            { 0.4, 0.65 } // width and peak of 10th mountain
    };
    Random random = new Random();
    int randomIndex;

    public void draw() {
        repaint();
        Timer timer = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (getWidth() == 0) {
                    return;
                }

                if (player == null) {
                    //used getHeight() to set the size of the player (make circle) based on the height of the window
                    player = new Player((int) (0.035 * getHeight()), (int) (0.035 * getHeight()));
                }

                // move mountains and remove mountains that are off the screen
                for (Mountain mountain : mountains) {
                    mountain.move(speed);
                }
                mountains.removeIf(mountain -> mountain.start + mountain.width * getWidth() < 0);

                int edge = 0;

                // keep adding mountains
                if (!mountains.isEmpty()) {
                    Mountain lastMountain = mountains.get(mountains.size() - 1);
                    edge = (int) (lastMountain.start) + (int) (lastMountain.width * getWidth());
                }

                // adding first 3 mountains before edge goes part getWidth()
                while (edge < getWidth()) {
                    randomIndex = random.nextInt(table.length);
                    mountains.add(new Mountain(edge, table[randomIndex][1], table[randomIndex][0]));
                    edge += (int) (table[randomIndex][0] * getWidth());
                }

                // working on player movement
                if (player != null) {
                    int playerY = 0; // y position of player
                    int surfaceHeight = 0; // height of player from the mountain
                    double groundY = getHeight() * 0.8; // ground level

                    for (Mountain mountain : mountains) {
                        double startPx = mountain.start; // start of moutain in pixels
                        double widthPx = mountain.width * getWidth(); // width of mountain in pixels
                        double endPx = startPx + widthPx; // end of mountain in pixels
                        int centerOfPlayer = player.x + player.wide / 2;

                        double distanceIn = centerOfPlayer - startPx; // distance of player from start of mountain in
                                                                      // pixels

                        double mountainHeight; // height of mountain from ground/grass level
                        double peakY; // peak of mountain

                        if (startPx <= centerOfPlayer && centerOfPlayer <= endPx) {
                            peakY = getHeight() - (mountain.peak * getHeight());
                            mountainHeight = groundY - peakY;

                            if (distanceIn < widthPx / 2) {
                                surfaceHeight = (int) (mountainHeight * (distanceIn / (widthPx / 2)));
                            } else {
                                surfaceHeight = (int) (mountainHeight * ((widthPx - distanceIn) / (widthPx / 2)));
                            }
                            break;
                        }

                    }
                    playerY = (int) (groundY - player.tall - surfaceHeight);
                    player.update(playerY);

                }

                // repaint the screen
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int height = getHeight();
        int width = getWidth();

        // colorset and drawing for ground
        g.setColor(Color.decode("#87eba7"));
        g.fillRect(0, (int) (height - (0.2) * height), width, (int) (0.2 * height));

        // colorset and drawing for mountains
        g.setColor(Color.decode("#8B4513"));

        for (int i = 0; i < mountains.size(); i++) {
            Mountain mountain = mountains.get(i);
            int mountainStart = (int) (mountain.start);
            int mountainPeak = (int) (mountain.peak * height);
            int mountainWidth = (int) (mountain.width * width);
            int mid = mountainStart + mountainWidth / 2;
            int end = mountainStart + mountainWidth;
            int xpoints[] = { mountainStart, mid, end };
            int ypoints[] = { (int) (height - (0.2) * height), (int) (height - mountainPeak),
                    (int) (height - (0.2) * height) };
            g.fillPolygon(xpoints, ypoints, 3);
        }

        // colorset and drawing for player
        g.setColor(Color.decode("#ff7b00"));

        if (player != null) {
            g.fillOval(player.x, player.y, player.wide, player.tall);
            g.fillOval(player.x, player.y - player.tall, player.wide, player.tall);
            g.setColor(Color.BLACK);
            g.drawLine(player.x, player.y - (player.tall/2), player.x + (player.wide/2), player.y - (player.tall/3));
            g.drawLine(player.x + player.wide, player.y - (player.tall/2), player.x + player.wide + (player.wide/2), player.y - (player.tall/3));
            //g.fillRect(player.x - player.wide/2, player.y - player.tall*(2), player.wide * 2, player.tall*(2)); 

        }

    }

}