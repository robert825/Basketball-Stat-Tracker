import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
   

public class StatTracker extends JPanel implements MouseListener
{
   JPanel madeMissButtons, modStats;
   JPanel modFTs, modRebs, modAsts, modStls, modBlks, modDefs, modCTs, modFls;
   JButton madeShot, missedShot;
   JButton FTMade, FTMissed, addReb, subReb, addAst, subAst, addStl, subStl;
   JButton addBlk, subBlk, addDef, subDef, addCT, subCT, addFl, subFl;
   
   FlowLayout flow;
   BoxLayout box;
   
   int xCoord, yCoord, clicks;
   int points, rebounds, assists, steals, blocks, fouls, deflections, chargesTaken;
   int[] xLine = new int[180];
   int[] yLine = new int[180];
   boolean made, missed = false;
   boolean madeClicked, missedClicked = false;
   
   public void fillLineArrays()
   {
      int x=250;
      int y=70;
      int width = 198;
      int height = 180;
   
      for(int t=0; t<=179; t++)
      {
         int ePX = x + (int) (width  * Math.cos(Math.toRadians(t)));
         int ePY = y + (int) (height * Math.sin(Math.toRadians(t)));
      
         xLine[t]=ePX-6;
         yLine[t]=ePY+14;
      }
   }

   public boolean threePointer(int xVal, int yVal)
   {
      if(xVal<=40 || xVal>= 448 || yVal>=264)
         return true;
   
      /*int indexXVal = -1;
      while(indexXVal==-1)
      {
         if(xVal<=244)
         {
            for(int a=0; a<=90; a++)
            {
               if(xLine[a]==xVal)
               {
                  indexXVal = a;
                  break;
               }
            }
            if(indexXVal==-1)
               xVal++;
         }
         else
         {
            for(int a=xLine.length-2; a>90; a++)
            {
               if(xLine[a]==xVal)
               {
                  indexXVal = a;
                  break;
               }
            }
            if(indexXVal==-1)
               xVal--;
         }
      }
      if(yVal>=yLine[indexXVal])
         return true;*/
      return false;
   }
   public void paintComponent(Graphics g)
   {
      //Draw court lines
      g.setColor(Color.BLUE);
      g.drawRect(190,0,120,190);
      g.drawOval(190,130,120,120);
      g.drawRect(0,0,500,400);
      g.drawArc(52,-110,396,360,0,-180);
      g.drawLine(52,0,52,70);
      g.drawLine(448,0,448,70);
   
      g.setColor(Color.BLACK);
      g.fillRect(220,35,60,5);
      g.fillOval(240,40,20,20);
      
      if(made)
      {
         g.setColor(Color.GREEN.darker());
         g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
         g.drawString("O", xCoord-6, yCoord+6);
      }
      if(missed)
      {
         g.setColor(Color.RED);
         g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
         g.drawString("X", xCoord-6, yCoord+6);
      }
      
   } 
   public StatTracker()
   { 
      fillLineArrays();
      
      addMouseListener(this);
      setLayout(new BorderLayout());
      
      madeMissButtons = new JPanel();
      madeMissButtons.setLayout(flow = new FlowLayout());
      add(madeMissButtons, BorderLayout.SOUTH);
      
      modStats = new JPanel();
      modStats.setLayout(box = new BoxLayout(modStats, BoxLayout.Y_AXIS));
      add(modStats, BorderLayout.EAST);
      
      modFTs = new JPanel();
      modStats.add(modFTs);
      
      modRebs = new JPanel();
      modStats.add(modRebs);
      
      modAsts = new JPanel();
      modStats.add(modAsts);
      
      modStls = new JPanel();
      modStats.add(modStls);
      
      modBlks = new JPanel();
      modStats.add(modBlks);
      
      modDefs = new JPanel();
      modStats.add(modDefs);
      
      modCTs = new JPanel();
      modStats.add(modCTs);
      
      modFls = new JPanel();
      modStats.add(modFls);
      
      
      madeShot = new JButton("MADE O");
      madeShot.addActionListener(new MakeListener());
      madeMissButtons.add(madeShot);
      
      missedShot = new JButton("MISS X");
      missedShot.addActionListener(new MissListener());
      madeMissButtons.add(missedShot);
      
      FTMade = new JButton("FT MADE");
      modFTs.add(FTMade);
      
      FTMissed = new JButton("FT MISSED");
      modFTs.add(FTMissed);
      
      addReb = new JButton("REB +1");
      modRebs.add(addReb);
      
      subReb = new JButton("REB -1");
      modRebs.add(subReb);
      
      addAst = new JButton("AST +1");
      modAsts.add(addAst);
      
      subAst = new JButton("AST -1");
      modAsts.add(subAst);
      
      addStl = new JButton("STL +1");
      modStls.add(addStl);
      
      subStl = new JButton("STL -1");
      modStls.add(subStl);
      
      addBlk = new JButton("BLK +1");
      modBlks.add(addBlk);
      
      subBlk = new JButton("BLK -1");
      modBlks.add(subBlk);
      
      addDef = new JButton("DEFL +1");
      modDefs.add(addDef);
      
      subDef = new JButton("DEFL -1");
      modDefs.add(subDef);
      
      addCT = new JButton("CHRG +1");
      modCTs.add(addCT);
      
      subCT = new JButton("CHRG -1");
      modCTs.add(subCT);
      
      addFl = new JButton("FOUL +1");
      modFls.add(addFl);
      
      subFl = new JButton("FOUL -1");
      modFls.add(subFl);
            
   }
   public void mouseClicked(MouseEvent e) 
   {
      if(e.getX()<=500 && e.getX()>=0 && e.getY()<=400 && e.getY()>=0)
      {
         xCoord = e.getX();
         yCoord = e.getY();
         if(madeClicked)
         {
            made=true;
            missed=false;
         }
         else
         {
            missed=true;
            made=false;
         }
      }
      System.out.println(""+(xCoord-6)+" "+(yCoord+6));
      System.out.println(threePointer(xCoord-6,yCoord+6));
      repaint();
   }
   public void mousePressed(MouseEvent e) 
   {
   }

   public void mouseReleased(MouseEvent e) 
   {
   }

   public void mouseEntered(MouseEvent e) 
   {
     
   }

   public void mouseExited(MouseEvent e) 
   {
   }
   public class MakeListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         madeClicked = true;
         missedClicked = false;
      }
   }
   public class MissListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         missedClicked = true;
         madeClicked = false;
      }
   }


}
