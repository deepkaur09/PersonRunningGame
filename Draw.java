
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
//import java.math.*;

public class Draw extends JPanel {
    boolean gameOver = false;
    Timer timer;
    int speed = 5;
    int playerEnergy = 1000; //battery of player
    int incSpeedAfterTicks = 600;
    int tickCount = 0;
    int ticksUntilNextObstacle = 60;
    int ticksUntilNextEnergy = 80;
    ArrayList<Mountain> mountains = new ArrayList<Mountain>();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    ArrayList<Energy> energyLevel = new ArrayList<Energy>();

    // creating a player object
    Player player;

    // different widths and peaks for mountains
    double[][] mountainTypes = {
            { 0.4, 0.3 }, // width and peak of 1st mountain
            { 0.4, 0.35 }, // width and peak of 2nd mountain
            { 0.4, 0.4 }, // width and peak of 3rd mountain
            { 0.4, 0.45 }, // width and peak of 4th mountain
            { 0.4, 0.5 }, // width and peak of 5th mountain
            { 0.4, 0.55 }, // width and peak of 6th mountain
            { 0.4, 0.58 }, // width and peak of 7th mountain
            { 0.4, 0.42 }, // width and peak of 8th mountain
            { 0.4, 0.37 }, // width and peak of 9th mountain
            { 0.4, 0.44 } // width and peak of 10th mountain
    };

    // different sizes for obstacles
    double[][] obstacleTypes = {
            { 0.03, 0.025 },
            { 0.02, 0.03 },
            { 0.025, 0.035 }
    };

    Random random = new Random();
    int randomIndex;
    int obstacleIndex;

    int surfaceHeight = 0; // height of player from the mountain

    public Draw() {
        // Setting up keys
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "jump");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "ducked");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released DOWN"), "unducked");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "restart");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exit");

        // action taker for jumping
        getActionMap().put("jump", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (player != null && player.heightAboveSurface == 0) {
                    player.velocityY = 20;
                }
            }
        });
        // action taker for down/ducking
        getActionMap().put("ducked", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (player != null && player.heightAboveSurface == 0) {
                    player.ducking = true;
                }
            }
        });
        // action taker for unducking
        getActionMap().put("unducked", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (player != null) {
                    player.ducking = false;
                }
            }
        });

        getActionMap().put("restart", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    gameOver = false;
                    mountains.clear();
                    obstacles.clear();
                    player = null;
                    ticksUntilNextObstacle = 60;
                    speed = 5;
                    tickCount = 0;
                    energyLevel.clear();
                    ticksUntilNextEnergy = 80;
                    timer.start();
                }
            }
        });

         getActionMap().put("exit", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public void draw() {
        repaint();
        timer = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    repaint();
                    ((Timer) e.getSource()).stop();
                    return;
                }
                if (getWidth() == 0) {
                    return;
                }

                if (player == null) {
                    // used getHeight() to set the size of the player (make circle) based on the
                    // height of the window
                    player = new Player((int) (0.035 * getHeight()), (int) (0.035 * getHeight()));
                }

                tickCount++;
                playerEnergy--; // decrease player energy over time
                ticksUntilNextObstacle--; // decrease ticks until next obstacle
                ticksUntilNextEnergy--; // decrease ticks until next energy
                double groundY = getHeight() * 0.8; // ground level

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
                    randomIndex = random.nextInt(mountainTypes.length);
                    mountains.add(new Mountain(edge, mountainTypes[randomIndex][1], mountainTypes[randomIndex][0]));
                    edge += (int) (mountainTypes[randomIndex][0] * getWidth());
                }

                // working on player movement
                if (player != null) {
                    int playerY = 0; // y position of player

                    surfaceHeight = calculateSurfaceHeight(player.x + player.wide / 2);

                    player.heightAboveSurface += player.velocityY;
                    player.velocityY -= 1.5; // gravity gets substracted from velocityY

                    if (player.heightAboveSurface <= 0) {
                        player.heightAboveSurface = 0;
                        player.velocityY = 0;
                    }

                    playerY = (int) (groundY - player.tall - surfaceHeight - player.heightAboveSurface);
                    player.update(playerY);

                }

                // working on obstacles

                // keeping the objects move to left
                for (Obstacle obstacle : obstacles) {
                    obstacle.move(speed);
                }

                // removing from list
                obstacles.removeIf(obstacle -> obstacle.start + obstacle.obstacleWidth * getWidth() < 0);


                // add obstacle only after ticksUntilNextObstacle becomes less than 0
                if (ticksUntilNextObstacle <= 0) {
                    obstacleIndex = random.nextInt(obstacleTypes.length);
                    boolean isSafe = isSafeSpawn(getWidth(), obstacleTypes[obstacleIndex][0] * getWidth());
                    if (isSafe) {
                        Obstacle obs = new Obstacle(getWidth(), obstacleTypes[obstacleIndex][0],
                                obstacleTypes[obstacleIndex][1]);

                        // 1 in 6 obstacles will be in sky
                        if(random.nextInt(6) == 0){
                            obs.heightAboveGround = 0.09; 
                            //player.y - player.tall
                        }

                        obstacles.add(obs);
                        ticksUntilNextObstacle = 40 + random.nextInt(80);

                        int centerSurface = calculateSurfaceHeight(
                                (int) (obs.start + (obs.obstacleWidth * getWidth()) / 2));
                        int leftSurface = calculateSurfaceHeight((int) (obs.start));
                        int rightSurface = calculateSurfaceHeight((int) (obs.start + obs.obstacleWidth * getWidth()));
                        int rise = rightSurface - leftSurface;

                        // if the obstacle is above ground, then it should not be tilted
                        if(obs.heightAboveGround > 0){
                            rise = 0;
                        }
                        obs.angle = Math.atan2(rise, obs.obstacleWidth * getWidth());

                        int screenY = (int) (groundY - obs.obstacleHeight * getHeight() - centerSurface
                                - obs.heightAboveGround * getHeight());
                        obs.screenY = screenY;

                    } else {
                        ticksUntilNextObstacle = 2;
                    }

                }

                //increading speed after every incSpeedAfterTicks ticks
                if (speed < 15 && tickCount % incSpeedAfterTicks == 0) {
                    speed++;
                }

                for (Obstacle obstacle : obstacles) {
                    if (getPlayerBox().intersects(getObstacleBox(obstacle))) {
                        gameOver = true;
                        //((Timer) e.getSource()).stop();
                    }
                }

                //adding energy to the game
                if(ticksUntilNextEnergy <= 0){
                    ticksUntilNextEnergy = 80 + random.nextInt(10);
                    Energy en = new Energy(getWidth());
                    energyLevel.add(en);
                    int surH = calculateSurfaceHeight((int) (en.posX + en.width * getHeight() / 2)); // keeping in terms of height
                    en.posOnMountain = (int) (groundY - en.height * getHeight() - surH - 0.06 * getHeight());
                }

                //moving the energy to left
                for (Energy en : energyLevel) {
                    en.move(speed);
                }

                energyLevel.removeIf(en -> en.posX + en.width * getWidth() < 0);

                if(playerEnergy <= 0){
                    gameOver = true;
                }

                for (Energy en : energyLevel) {
                    if (getPlayerBox().intersects(getEnergyBox(en))) {
                        playerEnergy += 50; // increase player energy by 50
                        playerEnergy = Math.min(playerEnergy, 1000); // cap player energy at 1000
                        energyLevel.remove(en); // remove the energy from the game
                        break; // exit the loop to avoid ConcurrentModificationException
                    }
                }

                // repaint the screen
                repaint();
            }
        });
        timer.start();
    }

    // @par player center
    public int calculateSurfaceHeight(int x) {
        int surfaceH = 0;
        for (Mountain mountain : mountains) {
            double startPx = mountain.start; // start of moutain in pixels
            double widthPx = mountain.width * getWidth(); // width of mountain in pixels
            double endPx = startPx + widthPx; // end of mountain in pixels
            int centerOfObject = x;

            double distanceIn = centerOfObject - startPx; // distance of object from start of mountain in pixels

            double mountainHeight; // height of mountain from ground/grass level
            double peakY; // peak of mountain

            if (startPx <= centerOfObject && centerOfObject <= endPx) {
                peakY = getHeight() - (mountain.peak * getHeight());
                mountainHeight = (getHeight() * 0.8) - peakY;

                if (distanceIn < widthPx / 2) {
                    surfaceH = (int) (mountainHeight * (distanceIn / (widthPx / 2)));
                } else {
                    surfaceH = (int) (mountainHeight * ((widthPx - distanceIn) / (widthPx / 2)));
                }
                break;
            }
        }
        return surfaceH;

    }

    public boolean isSafeSpawn(double obsStart, double obsWidth) {
        for (Mountain mountain : mountains) {
            double startPx = mountain.start; // start of moutain in pixels
            double widthPx = mountain.width * getWidth(); // width of mountain in pixels
            double endPx = startPx + widthPx; // end of mountain in pixels
            double margin = 0.03 * getWidth();

            double distanceIn = obsStart - startPx; // distance of object from start of mountain in pixels

            if (startPx <= obsStart && obsStart <= endPx) {

                if (obsStart - margin <= startPx || obsStart + obsWidth + margin >= endPx) {
                    return false;
                }

                if (distanceIn + obsWidth + margin >= widthPx / 2 && distanceIn - margin <= widthPx / 2) {
                    return false;
                }

                return true;
            }
        }
        return false;
    }

    public Rectangle getPlayerBox(){
        int shrink = (int)(0.004 * getHeight()); // shrink the player box a little bit to make it more fair for the player
        if(player.ducking){
            return new Rectangle(player.x + shrink, player.y + shrink, player.wide * 2 - 2 * shrink, player.tall - 2 * shrink);
        }
        else{
            return new Rectangle(player.x + shrink, player.y - player.tall + shrink, player.wide - 2 * shrink, player.tall * 2 - 2 * shrink);
        }
    }

    public Rectangle getObstacleBox(Obstacle obstacle){
        int shrink = (int)(0.004 * getHeight());
        int obsWidth = (int) (obstacle.obstacleWidth * getWidth());
        int obsHeight = (int) (obstacle.obstacleHeight * getHeight());
        return new Rectangle((int) obstacle.start + shrink, obstacle.screenY + shrink, obsWidth - 2 * shrink, obsHeight - 2 * shrink);
    }

    public Rectangle getEnergyBox(Energy energy){
        int shrink = (int)(0.004 * getHeight());
        int enWidth = (int) (energy.width * getWidth());
        int enHeight = (int) (energy.height * getHeight());
        return new Rectangle((int) energy.posX + shrink, energy.posOnMountain + shrink, enWidth - 2 * shrink, enHeight - 2 * shrink);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g2D);
        int height = getHeight();
        int width = getWidth();

        g2D.setColor(Color.GREEN);
        //energy bar
        int barWidth = (int) (0.2 * width * playerEnergy / 1000);
        int barHeight = (int) (0.03 * height);
        g2D.fillRect((int) (0.8 * width), (int) (0.03 * height), barWidth, barHeight);

        //outlining the energy bar
        g2D.setColor(Color.BLACK);
        g2D.drawRect((int) (0.8 * width), (int) (0.03 * height), (int) (0.2 * width), barHeight);

        // colorset and drawing for ground
        g2D.setColor(Color.decode("#87eba7"));
        g2D.fillRect(0, (int) (height - (0.2) * height), width, (int) (0.2 * height));

        // colorset and drawing for mountains
        g2D.setColor(Color.decode("#8B4513"));

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
            g2D.fillPolygon(xpoints, ypoints, 3);
        }

        // colorset and drawing for player
        g2D.setColor(Color.decode("#4800ff"));

        if (player != null) {
            // bottom circle
            g2D.fillOval(player.x, player.y, player.wide, player.tall);
            // upper circle when ducked
            if (player.ducking) {
                g2D.fillOval(player.x + player.wide, player.y, player.wide, player.tall);
            }
            // upper circle when in usual
            else {
                g2D.fillOval(player.x, player.y - player.tall, player.wide, player.tall);
            }
            g2D.setColor(Color.BLACK);
            // g.drawLine(player.x, player.y - (player.tall/2), player.x + (player.wide/2),
            // player.y - (player.tall/3));
            // g.drawLine(player.x + player.wide, player.y - (player.tall/2), player.x +
            // player.wide + (player.wide/2), player.y - (player.tall/3));
            // g.fillRect(player.x - player.wide/2, player.y - player.tall*(2), player.wide
            // * 2, player.tall*(2));

        }

        // drawing the obstacle
        for (Obstacle obstacle : obstacles) {
            Graphics2D og = (Graphics2D) g2D.create();
            og.setColor(Color.DARK_GRAY);
            int obsWidth = (int) (obstacle.obstacleWidth * width);
            int obsHeight = (int) (obstacle.obstacleHeight * height);
            int pivotX = (int) (obstacle.start + (obstacle.obstacleWidth * getWidth()) / 2);
            int pivotY = obstacle.screenY + obsHeight;

            // drawing tilted obstacles
            og.rotate(-obstacle.angle, pivotX, pivotY);
            og.fillRect((int) obstacle.start, obstacle.screenY, obsWidth, obsHeight);
            og.dispose();
        }

        // drawing the energy
        for (Energy en : energyLevel) {
            g2D.setColor(Color.YELLOW);
            int enWidth = (int) (en.width * height);
            int enHeight = (int) (en.height * height);
            g2D.fillOval((int) en.posX, en.posOnMountain, enWidth, enHeight);
        }

        //redrawing the energy
        if(gameOver){
            g2D.setColor(Color.RED);
            g2D.setFont(new Font("Arial", Font.BOLD, 50));
            String gameOverText = "Game Over!";
            int textWidth = g2D.getFontMetrics().stringWidth(gameOverText);
            int textHeight = g2D.getFontMetrics().getHeight();
            int x = (width - textWidth) / 2;
            int y = (height - textHeight) / 2 + g2D.getFontMetrics().getAscent();
            g2D.drawString(gameOverText, x, y);
            g2D.setFont(new Font("Arial", Font.BOLD, 20));
            String restartText = "Press ENTER to restart";
            int restartWidth = g2D.getFontMetrics().stringWidth(restartText);
            int restartHeight = g2D.getFontMetrics().getHeight();
            int restartX = (width - restartWidth) / 2;
            int restartY = y + textHeight + restartHeight;
            g2D.drawString(restartText, restartX, restartY);
            String exitText = "Press ESC to exit";
            int exitWidth = g2D.getFontMetrics().stringWidth(exitText);
            int exitHeight = g2D.getFontMetrics().getHeight();
            int exitX = (width - exitWidth) / 2;
            int exitY = restartY + restartHeight + exitHeight;
            g2D.drawString(exitText, exitX, exitY);
        }
    }

}