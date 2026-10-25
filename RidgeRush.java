/*
For a window that fills your screen, look up setExtendedState with JFrame.MAXIMIZED_BOTH.
 For a true full-screen game with no title bar, look up setUndecorated(true) and GraphicsDevice 
full-screen mode. Start with the first one, since it’s much simpler. 
Also use getWidth() and getHeight() instead of fixed numbers when 
positioning things, because screen sizes differ.

Person running

The person’s position is simple: at any x, they stand on the ground height at that point.
Jumping needs only two ideas: an upward speed when Space is pressed, and gravity pulling it back down each tick.
Hills can be a list of heights that scrolls past, or a smooth wave from a math function.

Obstacles that fit a runner

Cactus: a rectangle on the ground that you jump over. It’s the simplest and a good first one.
Spinning blade machine: same as the cactus, but wider or taller, so it needs a better-timed jump.
Rock: a tall one that needs a high jump.
Gap or pit: a hole in the terrain that you fall into if you don’t jump.
Flying obstacle: something you duck under with the down key.

Maximize the window (look up setExtendedState with MAXIMIZED_BOTH),
 then inside paintComponent ask the panel for its own getWidth()
  and getHeight() and base all your coordinates on those, 
  like “ground starts at 80% of the height.” That way the game fits any screen.
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
      Draw d = new Draw();
      new RidgeRush();
      d.draw();
      d.setBackground(Color.decode("#87d7eb"));
      frame.add(d);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      frame.setVisible(true);
   }
}