import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.event.*;
import java.io.*;
   
public class TeamStatTracker extends JPanel implements MouseListener
{
   JPanel madeMissRecordStats, modStats, squad, addReb;
   JPanel madeMiss, recordStats;
   JButton madeShot, missedShot;
   JButton FTMade, FTMissed, addOReb, addDReb, addAst, addStl;
   JButton addBlk, addDef, addCT, addFl, addTurn, undo;
   JLabel recordPoints, recordFGMadeMissed, recordFGP, record3PMadeMissed, record3PP;
   JLabel recordFTMadeMissed, recordFTP, recordRebs, recordORebs, recordAsts;
   JLabel recordStls, recordBlks, recordDefs, recordCTs, recordFls, recordTurns;
   
   JTextField addPlayBox;
   TextArea log;
   JFrame statLogFrame;
   
   GridLayout grid1, grid2, grid3, grid4, grid5;
   BoxLayout box1;
   
   public static JMenuBar tools;
   public static JMenu view, update, showShotChart, gameFinished;
   public static JMenuItem viewStatLog, viewGameLog, addPlayer, updateClock, showTeamChart, finish;
   
   ArrayList<Integer> shotsX = new ArrayList<Integer>();
   ArrayList<Integer> shotsY = new ArrayList<Integer>();
   ArrayList<Boolean> wasMade = new ArrayList<Boolean>();
   
   ArrayList<String> gameInformation = new ArrayList<String>();
   ArrayList<String> gamePlayLog = new ArrayList<String>();
   ArrayList<Player> team = new ArrayList<Player>();
   Player current, selected;
   Font f = new Font("Font1", Font.PLAIN, 36);
   int xCoord, yCoord, clicks;
   String lastAction = " ";
   
   String finalScore = " ";
   String opponent = " ";
   String dateTime = " ";
   String loc = " ";
   String trackedBy = " ";
   
   int points, rebounds, oRebounds, assists, steals, blocks, fouls, deflections, chargesTaken, turnovers = 0;
   int FGMade, FGMissed, threesMade, threesMissed, FTMakes, FTMisses = 0;
   double FGPer, threePer, FTPer = 0;
   boolean made, missed = false;
   boolean madeClicked, missedClicked = false;
   boolean displayStats;
   boolean undoMake, undoMiss;
   boolean gameLogOpen = false;
   boolean showTeam = true;
   boolean showPlayer = false;
   boolean closeable = false;
 
   //File currentGame = new File("E:/Stat Tracker Basketball/currentGame.txt");
   //PrintWriter outFile = new PrintWriter(currentGame);
  
   public void write() throws IOException 
   {
      try
      {
         File currentGame = new File("E:/Stat Tracker Basketball/currentGame.txt");
         PrintWriter outFile = new PrintWriter(currentGame);
         
         outFile.println("Game Information");
         outFile.println("----------------");
         for(int a=0; a<gameInformation.size(); a++)
         {
            outFile.println(gameInformation.get(a));
         }
         int sp = finalScore.indexOf(' ')+1;
         outFile.println("FINAL: Loudoun Valley- "+finalScore.substring(sp)+" "+opponent+"- "+finalScore.substring(0,sp));
         outFile.println("");
         outFile.println("\nStats");
         outFile.println("-----");
         for(Player p: team)
         {
            ArrayList<Object> aaa = p.retrieveStats();
            
            for(Object o: aaa)
            {
               outFile.print(""+o+" ");
            }
            outFile.println("\n");
         }
         outFile.println("");
         outFile.println("\nShots");
         outFile.println("-----");
         for(Player p: team)
         {
            ArrayList<Integer> xxx = p.getShotsX();
            ArrayList<Integer> yyy = p.getShotsY();
            ArrayList<Boolean> mmm = p.getMadeMissed();
            outFile.println(""+p.getName());
            outFile.println(""+xxx.size());
            for(int a=0; a<xxx.size(); a++)
            {
               outFile.println(""+xxx.get(a)+" "+yyy.get(a)+" "+mmm.get(a));
            }
         }
         
         outFile.close();
      }
      catch (IOException error) 
      {
         System.err.println( "Error making file:" );
         System.err.println( "\t" + error );
      }
   }
   
   public void updateStats()
   {
      FGPer = round((double)FGMade/(FGMade+FGMissed)*100);
      threePer = round((double)threesMade/(threesMade+threesMissed)*100);
      FTPer = round((double)FTMakes/(FTMakes+FTMisses)*100);
      
      recordFGMadeMissed.setText("Field Goals: "+FGMade+" / "+(FGMade+FGMissed));   
      recordFGP.setText("Field Goal %: "+FGPer+"%");
      record3PMadeMissed.setText("3 PT Field Goals: "+threesMade+" / "+(threesMade+threesMissed));
      record3PP.setText("3 PT Field Goal %: "+threePer+"%");
      recordFTMadeMissed.setText("Free Throws: "+FTMakes+" / "+(FTMakes+FTMisses));
      recordFTP.setText("Free Throw %: "+FTPer+"%");
      recordPoints.setText("Points: "+points);
      recordRebs.setText("Rebounds: "+rebounds);
      recordORebs.setText("Off. Rebounds: "+oRebounds);
      recordAsts.setText("Assists: "+assists);
      recordStls.setText("Steals: "+steals);
      recordBlks.setText("Blocks: "+blocks);
      recordDefs.setText("Deflections: "+deflections);
      recordCTs.setText("Charges Taken: "+chargesTaken);
      recordFls.setText("Personal Fouls: "+fouls);
      recordTurns.setText("Turnovers: "+turnovers);         
   }
   public double round(double d)
   {
      d = Math.round(d*10);
      d = d/10;
      return d;
   }
   public void prompt()
   {
      Scanner input = new Scanner(System.in);
               
      System.out.println("Enter players (type \"END\" when finished adding players)");
      System.out.println("Or type \"LV\" to use roster from LVHS 2013-14 team");
      System.out.println("-------------------------------------------------------");
      
      String name = input.nextLine();
      if(name.equals("LV"))
      {
         try 
         {  
            String filename = "E:/Stat Tracker Basketball/LoudounValley2013-14.txt";
            File file = new File(filename);
            Scanner inFile = new Scanner(file);
         
            while(inFile.hasNextLine())
            {
               String sss = inFile.nextLine();
               team.add(new Player(sss));
            }
         }
         catch (FileNotFoundException e)
         {
            System.out.println("File not found");
         
         } 
      }  
      else
         while(!name.equals("END"))
         {
            team.add(new Player(name)); 
            name = input.nextLine();
         }
      current = team.get(0); 
      
      System.out.println("---Game Information---");
      System.out.print("Enter opponent: ");
      opponent = input.nextLine();
      gameInformation.add("Opponent: "+ opponent);
      
      System.out.print("Enter date & time (mm.dd.yy h:mm): ");
      dateTime = input.nextLine();
      gameInformation.add("Date & Time: "+dateTime);
      
      System.out.print("Location: ");
      loc = input.nextLine();
      gameInformation.add("Location: "+loc);
      
      System.out.print("Game tracked by: ");
      trackedBy = input.nextLine();
      gameInformation.add("Game tracked by "+trackedBy);
   }
   public boolean threePointer(int xVal, int yVal)
   {
      if(xVal<=272 || xVal>= 668 || yVal>=250)
         return true;
      Arc2D.Float arc = new Arc2D.Float(Arc2D.OPEN);
      arc.setArc(272,-110,396,360,0,-180,Arc2D.CHORD);
      Area area = new Area(arc);
      if(area.contains(xVal+6,yVal-6))
         return false;
      else if(yVal<70 && xVal>272 && xVal<668)
         return false;
      return true;
   }
   public void paintComponent(Graphics g)
   {
      if(showPlayer)
      {
         for(int a=0; a<shotsX.size(); a++)
         {
            g.setColor(new Color(234, 234, 234));
            if(wasMade.get(a)== true)
            {
               g.drawString("O", shotsX.get(a)-6, shotsY.get(a)+6);
            }
            else
            {
               g.drawString("X", shotsX.get(a)-6, shotsY.get(a)+6);
            }
         }
      }
      //Draw court lines
      g.setColor(Color.BLUE);
      g.drawRect(410,0,120,190);
      g.drawOval(410,130,120,120);
      g.drawRect(220,0,500,400);
      g.drawArc(272,-110,396,360,0,-180);
      g.drawLine(272,0,272,70);
      g.drawLine(668,0,668,70);
   
      g.setColor(Color.BLACK);
      g.fillRect(440,35,60,5);
      g.fillOval(460,40,20,20);
      g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
      
      /*if(made)
      {
         g.setColor(Color.GREEN.darker());
         g.drawString("O", xCoord-6, yCoord+6);
      }
      if(missed)
      {
         g.setColor(Color.RED);
         g.drawString("X", xCoord-6, yCoord+6);
      }
      if(undoMake)
      {
         g.setColor(new Color(234,234,234));
         g.drawString("O", xCoord-6, yCoord+6);
      }
      if(undoMiss)
      {
         g.setColor(new Color(234,234,234));
         g.drawString("X", xCoord-6, yCoord+6);
      }
      undoMake=false;
      undoMiss=false;*/
      
      if(showTeam)
      {
         for(int a=0; a<shotsX.size(); a++)
         {
            if(wasMade.get(a)== true)
            {
               g.setColor(Color.GREEN.darker());
               g.drawString("O", shotsX.get(a)-6, shotsY.get(a)+6);
            }
            else
            {
               g.setColor(Color.RED);
               g.drawString("X", shotsX.get(a)-6, shotsY.get(a)+6);
            }
         }
      }
      if(showPlayer)
      {
         ArrayList<Integer> selectedShotsX = selected.getShotsX();
         ArrayList<Integer> selectedShotsY = selected.getShotsY();
         ArrayList<Boolean> selectedMadeMissed = selected.getMadeMissed();
      
         for(int a=0; a<selectedShotsX.size(); a++)
         {
            if(selectedMadeMissed.get(a)== true)
            {
               g.setColor(Color.GREEN.darker());
               g.drawString("O", selectedShotsX.get(a)-6, selectedShotsY.get(a)+6);
            }
            else
            {
               g.setColor(Color.RED);
               g.drawString("X", selectedShotsX.get(a)-6, selectedShotsY.get(a)+6);
            }
         }
      
      }
      
      if(gamePlayLog.size()>1)
      {
         g.setColor(new Color(234,234,234));
         g.drawString(gamePlayLog.get(gamePlayLog.size()-2), 300,420);
      }
      g.setColor(Color.BLACK);
      if(gamePlayLog.size()>0)
         g.drawString(gamePlayLog.get(gamePlayLog.size()-1), 300,420);
         
     
   } 
   public void addPlayer(JPanel j, JMenu m, Player p)
   {
      JButton button = new JButton(""+p.getName());
      button.addActionListener(new PlayerListener());
      j.add(button);
      JMenuItem item = new JMenuItem(""+p.getName());
      item.addActionListener(new ShowShotChartListener());
      m.add(item);
   }
   public TeamStatTracker()
   {  
      addMouseListener(this);
      setLayout(new BorderLayout());
      
      tools = new JMenuBar();
      view = new JMenu("View");
      update = new JMenu("Update");
      showShotChart = new JMenu("Show Shot Chart");
      gameFinished = new JMenu("Finish Game");
      viewStatLog = new JMenuItem("Stat Log");
      viewGameLog = new JMenuItem("Game Log");
      addPlayer = new JMenuItem("Add Player");
      updateClock = new JMenuItem("Game Clock");
      showTeamChart = new JMenuItem("Team");
      finish = new JMenuItem("Finish and Save");
      tools.add(view);
      tools.add(update);
      tools.add(showShotChart);
      tools.add(gameFinished);
      view.add(viewStatLog);
      view.add(viewGameLog);
      update.add(addPlayer);
      update.add(updateClock);
      showShotChart.add(showTeamChart);
      gameFinished.add(finish);
      
      viewStatLog.addActionListener(new ViewStatLogListener());
      viewGameLog.addActionListener(new ViewGameLogListener());
      addPlayer.addActionListener(new AddPlayerListener());
      updateClock.addActionListener(new UpdateClockListener());
      showTeamChart.addActionListener(new ShowTeamChartListener());
      finish.addActionListener(new FinishGameListener());
      
      madeMissRecordStats = new JPanel();
      madeMissRecordStats.setLayout(box1 = new BoxLayout(madeMissRecordStats, 1));
      add(madeMissRecordStats, BorderLayout.SOUTH);
      
      recordStats = new JPanel();
      recordStats.setLayout(grid1 = new GridLayout(4,4,5,0));
      madeMissRecordStats.add(recordStats);
      
      madeMiss = new JPanel();
      madeMiss.setLayout(grid2 = new GridLayout(1,2));
      madeMissRecordStats.add(madeMiss);
      
      modStats = new JPanel();
      modStats.setLayout(grid3 = new GridLayout(11,1));
      add(modStats, BorderLayout.EAST);
      
      squad = new JPanel();
      squad.setLayout(grid4 = new GridLayout(team.size(),1));
      add(squad, BorderLayout.WEST);
    
      recordFGMadeMissed = new JLabel("Field Goals: "+FGMade+" / "+(FGMade+FGMissed));
      recordStats.add(recordFGMadeMissed);
      
      recordFGP = new JLabel("Field Goal %: "+FGPer+"%");
      recordStats.add(recordFGP);
      
      record3PMadeMissed = new JLabel("3 PT Field Goals: "+threesMade+" / "+(threesMade+threesMissed));
      recordStats.add(record3PMadeMissed);
      
      record3PP = new JLabel("3 PT Field Goal %: "+threePer+"%");
      recordStats.add(record3PP);
      
      recordFTMadeMissed = new JLabel("Free Throws: "+FTMakes+" / "+(FTMakes+FTMisses));
      recordStats.add(recordFTMadeMissed);
      
      recordFTP = new JLabel("Free Throw %: "+FTPer+"%");
      recordStats.add(recordFTP);
      
      recordPoints = new JLabel("Points: "+points);
      recordStats.add(recordPoints); 
      
      recordRebs = new JLabel("Rebounds: "+rebounds);
      recordStats.add(recordRebs);
      
      recordORebs = new JLabel("Off. Rebounds: "+oRebounds);
      recordStats.add(recordORebs);
     
      recordAsts = new JLabel("Assists: "+assists);
      recordStats.add(recordAsts);
     
      recordStls = new JLabel("Steals: "+steals);
      recordStats.add(recordStls);
     
      recordBlks = new JLabel("Blocks: "+blocks);
      recordStats.add(recordBlks);
     
      recordDefs = new JLabel("Deflections: "+deflections);
      recordStats.add(recordDefs);
     
      recordCTs = new JLabel("Charges Taken: "+chargesTaken);
      recordStats.add(recordCTs);
     
      recordFls = new JLabel("Personal Fouls: "+fouls);
      recordStats.add(recordFls);
      
      recordTurns = new JLabel("Turnovers: "+turnovers);
      recordStats.add(recordTurns);
            
      madeShot = new JButton("MADE O");
      madeShot.setFont(f);
      madeShot.addActionListener(new MakeListener());
      madeMiss.add(madeShot);
      
      missedShot = new JButton("MISS X");
      missedShot.setFont(f);
      missedShot.addActionListener(new MissListener());
      madeMiss.add(missedShot);
      
      FTMade = new JButton("FREE THROW MADE");
      FTMade.addActionListener(new FTMadeListener());
      modStats.add(FTMade);
      
      FTMissed = new JButton("FREE THROW MISSED");
      FTMissed.addActionListener(new FTMissedListener());
      modStats.add(FTMissed);
      
      addReb = new JPanel();
      addReb.setLayout(grid5 = new GridLayout(1,2));
      modStats.add(addReb);
      
      addOReb = new JButton("OFF REB +1");
      addOReb.addActionListener(new AddORebListener());
      addReb.add(addOReb);
      
      addDReb = new JButton("DEF REB +1");
      addDReb.addActionListener(new AddDRebListener());
      addReb.add(addDReb);
      
      addAst = new JButton("ASSISTS +1");
      addAst.addActionListener(new AddAstListener());
      modStats.add(addAst);
      
      addStl = new JButton("STEALS +1");
      addStl.addActionListener(new AddStlListener());
      modStats.add(addStl);
      
      addBlk = new JButton("BLOCKS +1");
      addBlk.addActionListener(new AddBlkListener());
      modStats.add(addBlk);
      
      addDef = new JButton("DEFLECTIONS +1");
      addDef.addActionListener(new AddDefListener());
      modStats.add(addDef);
      
      addCT = new JButton("CHARGES TAKEN +1");
      addCT.addActionListener(new AddCTListener());
      modStats.add(addCT);
      
      addFl = new JButton("FOULS +1");
      addFl.addActionListener(new AddFlListener());
      modStats.add(addFl);
      
      addTurn = new JButton("TURNOVERS +1");
      addTurn.addActionListener(new AddTurnListener());
      modStats.add(addTurn);
      
      undo = new JButton("UNDO");
      undo.addActionListener(new UndoListener());
      modStats.add(undo);
         
      prompt();   
      
      for(Player p: team)
         addPlayer(squad, showShotChart, p);
   }
            
   public void mouseClicked(MouseEvent e) 
   {
      showTeam = true;
      showPlayer = false;
      
      if(e.getX()<=700 && e.getX()>=220 && e.getY()<=400 && e.getY()>=0)
      {
         xCoord = e.getX();
         yCoord = e.getY();
         
         shotsX.add(xCoord);
         shotsY.add(yCoord);
          
         if(madeClicked)
         {
            current.addShot(xCoord, yCoord, true);
            wasMade.add(true);
            made=true;
            missed=false;
            FGMade++;
            if(threePointer(xCoord,yCoord))
            {
               current.made3();
               points+=3;
               threesMade++;
               lastAction = "Made3";
               updateStats();
               gamePlayLog.add("Made 3 by "+current.getName());
            }
            else
            {
               current.made2();
               points+=2;
               lastAction = "Made2";
               updateStats();
               gamePlayLog.add("Made 2 by "+current.getName());
            }
         }
         else
         {
            current.addShot(xCoord, yCoord, false);
            wasMade.add(false);
            missed=true;
            made=false;
            FGMissed++;
            if(threePointer(xCoord,yCoord))
            {
               current.missed3();
               threesMissed++;
               lastAction = "Missed3";
               updateStats();
               gamePlayLog.add("Missed 3 by "+current.getName());
            
            }
            else
            {
               current.missed2();
               lastAction = "Missed2";
               updateStats();
               gamePlayLog.add("Missed 2 by "+current.getName());
            
            }
         }
      }
      repaint();
   }
   
   public class ViewStatLogListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {               
         statLogFrame = new JFrame("Stat Log");
         statLogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         statLogFrame.setSize(1150,(team.size()+5)*20);
         statLogFrame.setVisible(true);
         
         JTextArea statChart = new JTextArea(team.size()+4, 35);
         statChart.setEditable(false);
         statLogFrame.add(statChart);
      
         statChart.setTabSize(5);
         statChart.append("Player Name\t");
         statChart.append("FGM\t FGA\t FG%\t 3PM\t 3PA\t 3P%\t"); 
         statChart.append("FTM\t FTA\t FT%\t Pts\t OR\t Reb\t Ast\t Stl\t Blk\t Def\t CT\t PF\t TO");
         statChart.append("\n");
         for(Player p: team)
         {
            statChart.append("\n");
            for(Object o: p.retrieveStats())
               if(o.equals(p.getName()) && p.getName().length()>15)
                  statChart.append(""+p.getName().substring(0,15)+"\t ");
               else
                  statChart.append(""+o+"\t ");
         }
         statChart.append("\n\nTeam Totals \t ");
         statChart.append(""+FGMade+"\t "+(FGMissed+FGMade)+"\t "+FGPer+"\t ");
         statChart.append(""+threesMade+"\t "+(threesMissed+threesMade)+"\t "+threePer+"\t ");
         statChart.append(""+FTMakes+"\t "+(FTMisses+FTMakes)+"\t "+FTPer+"\t ");
         statChart.append(""+points+"\t "+oRebounds+"\t "+(rebounds-oRebounds)+"\t "); 
         statChart.append(""+assists+"\t "+steals+"\t "+blocks+"\t ");
         statChart.append(""+deflections+"\t "+chargesTaken+"\t "+fouls+"\t "+turnovers); 
      }
   }

   public class ViewGameLogListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         JFrame gameLogFrame = new JFrame("Game Play Log");
         gameLogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         gameLogFrame.setSize(450,200);
         gameLogFrame.setLayout(new BorderLayout());
         gameLogFrame.setVisible(true);
         
         log = new TextArea("Game Log: ", 10, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
         log.setEditable(false);
         gameLogFrame.add(log, BorderLayout.CENTER);
         
         JPanel addPlay = new JPanel();
         addPlay.setLayout(new GridLayout(1,2));
         addPlayBox = new JTextField("Add events to log", 30);
         addPlay.add(addPlayBox);
         JButton addEvent = new JButton("ADD EVENT"); 
         addEvent.addActionListener(new AddEventToGameLogListener());
         addPlay.add(addEvent);
         gameLogFrame.add(addPlay, BorderLayout.SOUTH);
         
         for(int a=0; a<gamePlayLog.size(); a++)
         {
            log.append("\n"+gamePlayLog.get(a)); 
         }
      }
   }
   public void mousePressed(MouseEvent e) 
   {
      return;
   }
   public void mouseReleased(MouseEvent e) 
   {
      return;
   }

   public void mouseEntered(MouseEvent e) 
   {
      return;
   }

   public void mouseExited(MouseEvent e) 
   {
      return;
   }
   public class PlayerListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         for(Player a: team)
         {
            if(e.getActionCommand().equals(a.getName()))
               current =  a;
         }
      }
   }
   public class AddPlayerListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         String playerName = JOptionPane.showInputDialog("Enter player name: ");
         Player a = new Player(playerName);
         team.add(a);
         addPlayer(squad, showShotChart, a);
         squad.revalidate();
         showShotChart.revalidate();
      }
   }
   public class UpdateClockListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         /////////////////////////////
      }
   }
   public class ShowShotChartListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         selected = team.get(0);
         for(Player a: team)
         {
            if(e.getActionCommand().equals(a.getName()))
               selected =  a;
         }
         
         showTeam = false;
         showPlayer = true;
         repaint();
      }
   }
   public class ShowTeamChartListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      {   
         showTeam = true;
         showPlayer = false;
         repaint();
      }
   }

   public class ShowIndividualStatsListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         if(showPlayer)
         {
            displayStats = true;
            recordFGMadeMissed.setText("Field Goals: "+current.getFGMade()+" / "+(current.getFGMade()+current.getFGMissed()));   
            recordFGP.setText("Field Goal %: "+current.getFGPer()+"%");
            record3PMadeMissed.setText("3 PT Field Goals: "+current.getThreesMade()+" / "+(current.getThreesMade()+current.getThreesMissed()));
            record3PP.setText("3 PT Field Goal %: "+current.getThreePer()+"%");
            recordFTMadeMissed.setText("Free Throws: "+current.getFTMakes()+" / "+(current.getFTMakes()+current.getFTMisses()));
            recordFTP.setText("Free Throw %: "+current.getFTPer()+"%");
            recordPoints.setText("Points: "+current.getPoints());
            recordRebs.setText("Rebounds: "+current.getRebounds());
            recordORebs.setText("Off. Rebounds: "+current.getORebounds());
            recordAsts.setText("Assists: "+current.getAssists());
            recordStls.setText("Steals: "+current.getSteals());
            recordBlks.setText("Blocks: "+current.getBlocks());
            recordDefs.setText("Deflections: "+current.getDeflections());
            recordCTs.setText("Charges Taken: "+current.getChargesTaken());
            recordFls.setText("Personal Fouls: "+current.getFouls());
            recordTurns.setText("Turnovers: "+current.getTurnovers()); 
            showPlayer = false;
            showTeam = true;
         }
         else
         {
            displayStats = false;
            showPlayer = true;
            showTeam = false;
            updateStats();
         }
         repaint();
      }  
   }
   
   public class FinishGameListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         int areYouSure = JOptionPane.showConfirmDialog(null, "By clicking OK the game will end and all stats will be written to output file", "WARNING", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE); 
         if(areYouSure == JOptionPane.OK_OPTION)
         {
            finalScore = JOptionPane.showInputDialog("Final Score (opponent first) ex: \"60 61\"");
            try
            {
               write();
            }
            catch(IOException error)
            {
            
            }
            
         }
      }
   }
   
   public class AddEventToGameLogListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         gamePlayLog.add(addPlayBox.getText());
         log.append("\n"+addPlayBox.getText());
         addPlayBox.setText("");
      }
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
   public class FTMadeListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.madeFree();
         points++;
         FTMakes++;
         lastAction = "FTMade";
         updateStats();
         gamePlayLog.add("Made free throw by "+current.getName());
         repaint();
      }
   }
   public class FTMissedListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.missedFree();
         FTMisses++;
         lastAction = "FTMissed";
         updateStats();
         gamePlayLog.add("Missed free throw by "+current.getName());
         repaint();
      }
   }
   public class AddORebListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addOReb();
         rebounds++;
         oRebounds++;
         lastAction = "AddOReb";
         updateStats();
         gamePlayLog.add("Offensive rebound by "+current.getName());
         repaint();
      }
   }
   public class AddDRebListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addDReb();
         rebounds++;
         lastAction = "AddDReb";
         updateStats();
         gamePlayLog.add("Defensive rebound by "+current.getName());
         repaint();
      }
   }
   public class AddAstListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addAst();
         assists++;
         lastAction = "AddAst";
         updateStats();
         gamePlayLog.add("Assisted by "+current.getName());
         repaint();
      }
   }
   public class AddStlListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addStl();
         steals++;
         lastAction = "AddStl";
         updateStats();
         gamePlayLog.add("Stolen by "+current.getName());
         repaint();
      }
   }
   public class AddBlkListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addBlk();
         blocks++;
         lastAction = "AddBlk";
         updateStats();
         gamePlayLog.add("Shot blocked by "+current.getName());
         repaint();
      }
   }
   public class AddDefListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addDef();
         deflections++;
         lastAction = "AddDef";
         updateStats();
         gamePlayLog.add("Pass deflected by "+current.getName());
         repaint();
      }
   }
   public class AddCTListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addCT();
         chargesTaken++;
         lastAction = "AddCT";
         updateStats();
         gamePlayLog.add("Charge taken by "+current.getName());
         repaint();
      }
   }
   public class AddFlListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addFl();
         fouls++;
         lastAction = "AddFl";
         updateStats();
         gamePlayLog.add("Personal foul against "+current.getName());
         repaint();
      }
   }
   public class AddTurnListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         current.addTurn();
         turnovers++;
         lastAction = "AddTurn";
         updateStats();
         gamePlayLog.add("Turnover by "+current.getName());
         repaint();
      }
   }
   public class UndoListener implements ActionListener 
   { 
      public void actionPerformed(ActionEvent e) 
      { 
         //gamePlayLog.remove(gamePlayLog.size()-1);
         if(lastAction.equals("Made3"))
         {
            points-=3;
            threesMade--;
            FGMade--;
            undoMake=true;
            repaint();
            updateStats();
         }
         else if(lastAction.equals("Made2"))
         {
            points-=2;
            FGMade--;
            undoMake=true;
            repaint();
            updateStats();
         }
         else if(lastAction.equals("Missed3"))
         {
            threesMissed--;
            FGMissed--;
            undoMiss=true;
            repaint();
            updateStats();
         }
         else if(lastAction.equals("Missed2"))
         {
            FGMissed--;
            undoMiss=true;
            repaint();
            updateStats();
         }
         else if(lastAction.equals("FTMade"))
         {
            points--;
            FTMakes--;
            updateStats();
         }
         else if(lastAction.equals("FTMissed"))
         {
            FTMisses--;
            updateStats();
         }
         else if(lastAction.equals("AddOReb"))
         {
            oRebounds--;
            rebounds--;
            updateStats();
         }
         else if(lastAction.equals("AddDReb"))
         {
            rebounds--;
            updateStats();
         }
         else if(lastAction.equals("AddAst"))
         {
            assists--;
            updateStats();
         }
         else if(lastAction.equals("AddStl"))
         {
            steals--;
            updateStats();
         }
         else if(lastAction.equals("AddBlk"))
         {
            blocks--;
            updateStats();
         }
         else if(lastAction.equals("AddDef"))
         {
            deflections--;
            updateStats();
         }
         else if(lastAction.equals("AddCT"))
         {
            chargesTaken--;
            updateStats();
         }
         else if(lastAction.equals("AddFl"))
         {
            fouls--;
            updateStats();
         }
         else if(lastAction.equals("AddTurn"))
         {
            turnovers--;
            updateStats();
         }
         lastAction=" ";
      }
   }
}
