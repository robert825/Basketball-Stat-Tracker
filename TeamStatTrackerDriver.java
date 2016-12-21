import javax.swing.*;
import java.util.*;
//import java.io.*;

public class TeamStatTrackerDriver
{
   public static void main(String[] args)
   {
      //File currentGame = new File("E:/Stat Tracker Basketball/currentGame.txt");
      //PrintWriter outFile = new PrintWriter(currentGame);
   
      TeamStatTracker tracker = new TeamStatTracker();
      JFrame frame = new JFrame("Basketball Stat Tracker");
      frame.setSize(940,600);
      frame.setLayout(null);
      frame.setLocation(0,0);
      frame.setJMenuBar(TeamStatTracker.tools);
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setContentPane(tracker);
      frame.setVisible(true);
      frame.setResizable(true);
              
   }
}
