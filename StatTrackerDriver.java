import javax.swing.*;
import java.util.*;

public class StatTrackerDriver
{
   public static void main(String[] args)
   {
      StatTracker tracker = new StatTracker();
      JFrame frame = new JFrame("Basketball Stat Tracker");
      frame.setSize(720,600);
      frame.setLayout(null);
      frame.setLocation(0,0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //frame.addKeyListener(tracker.new Listener(tracker));
      frame.setContentPane(tracker);
      frame.setVisible(true);
      frame.setResizable(false);
              
   }
}
