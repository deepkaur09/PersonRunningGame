/*
Obstacles that fit a runner

Cactus: a rectangle on the ground that you jump over. It’s the simplest and a good first one.
Spinning blade machine: same as the cactus, but wider or taller, so it needs a better-timed jump.
Rock: a tall one that needs a high jump.
Gap or pit: a hole in the terrain that you fall into if you don’t jump.
Flying obstacle: something you duck under with the down key.


a tilted rectangle is harder to hit-test exactly. The tilted shape is a rotated rectangle, 
and checking whether it overlaps the robot needs extra math. Java can do this for you: 
Polygon or Area with AffineTransform can build the rotated shape, and intersects checks overlap. 
It’s doable, but it’s more work than a plain rectangle.

The shortcut: the tilt only changes the picture. For the hit check, use a plain upright rectangle at the obstacle’s position.
 The tilt is usually small, so the upright box is almost the same area. Nobody will notice a few pixels of difference,
  and you skip all the rotation math.
*/

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.*;

public class RidgeRush{
   CardLayout layout = new CardLayout();
   JPanel mainPanel = new JPanel(layout);
   JPanel menuPanel = new JPanel();
   JPanel gamePanel = new JPanel();

   
   RidgeRush(){
      mainPanel.add(menuPanel, "menu");
      mainPanel.add(gamePanel, "game");
      //Draw object = new Draw();
   

   }

   public static void main(String[] args) {
      JFrame frame = new JFrame();
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      frame.setResizable(false);


      Draw d = new Draw();
      new RidgeRush();
      d.setBackground(Color.decode("#87d7eb"));
      frame.add(d);

      frame.setVisible(true);
      d.draw();


      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      
   }
}