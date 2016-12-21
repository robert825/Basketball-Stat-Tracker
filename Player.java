import java.util.*;
import java.io.Serializable;

public class Player
{
   String name;
   int points, rebounds, oRebounds, assists, steals, blocks, fouls, deflections, chargesTaken, turnovers = 0;
   int FGMade, FGMissed, threesMade, threesMissed, FTMakes, FTMisses = 0;
   double FGPer, threePer, FTPer = 0;
   ArrayList<Integer> shotsX = new ArrayList<Integer>();
   ArrayList<Integer> shotsY = new ArrayList<Integer>();
   ArrayList<Boolean> made = new ArrayList<Boolean>();

   public Player(String n)
   {
      name = n;
   } 
   public String getName()
   {
      return name;
   }
   public ArrayList<Object> retrieveStats()
   {
      ArrayList<Object> stats = new ArrayList<Object>();
      stats.add(getName());
      stats.add(getFGMade());
      stats.add(getFGMissed()+getFGMade());
      stats.add(getFGPer());
      stats.add(getThreesMade());
      stats.add(getThreesMissed()+getThreesMade());
      stats.add(getThreePer());
      stats.add(getFTMakes());
      stats.add(getFTMisses()+getFTMakes());
      stats.add(getFTPer());
      stats.add(getPoints());
      stats.add(getORebounds());
      stats.add(getRebounds());
      stats.add(getAssists());
      stats.add(getSteals());
      stats.add(getBlocks());
      stats.add(getDeflections());
      stats.add(getChargesTaken());
      stats.add(getFouls());
      stats.add(getTurnovers());
      return stats;
   }
   public void missed2()
   {
      FGMissed++;
      FGPer = round((double)FGMade/(FGMade+FGMissed)*100);
   }
   public void missed3()
   {
      threesMissed++;
      FGMissed++;
      FGPer = round((double)FGMade/(FGMade+FGMissed)*100);
      threePer = round((double)threesMade/(threesMade+threesMissed)*100);
   }
   public void made2()
   {
      FGMade++;
      points+=2;
      FGPer = round((double)FGMade/(FGMade+FGMissed)*100);
   }
   public void made3()
   {
      threesMade++;
      FGMade++;
      points+=3;
      FGPer = round((double)FGMade/(FGMade+FGMissed)*100);
      threePer = round((double)threesMade/(threesMade+threesMissed)*100);
   }
   public void missedFree()
   {
      FTMisses++;
      FTPer = round((double)FTMakes/(FTMakes+FTMisses)*100);
   }
   public void madeFree()
   {
      FTMakes++;
      points++;
      FTPer = round((double)FTMakes/(FTMakes+FTMisses)*100);
   }
   public void addOReb()
   {
      rebounds++;
      oRebounds++;
   }
   public void addDReb()
   {
      rebounds++;
   }
   public void addAst()
   {
      assists++;
   }
   public void addStl()
   {
      steals++;
   }
   public void addBlk()
   {
      blocks++;
   }
   public void addDef()
   {
      deflections++;
   }
   public void addCT()
   {
      chargesTaken++;
   }
   public void addFl()
   {
      fouls++;
   }
   public void addTurn()
   {
      turnovers++;
   }
   public int getFGMade()
   {
      return FGMade;
   }
   public int getFGMissed()
   {
      return FGMissed;
   }
   public int getThreesMade()
   {
      return threesMade;
   }
   public int getThreesMissed()
   {
      return threesMissed;
   }
   public double getFGPer()
   {
      return FGPer;
   }
   public double getThreePer()
   {
      return threePer;
   }
   public int getFTMakes()
   {
      return FTMakes;
   }
   public int getFTMisses()
   {
      return FTMisses;
   }
   public double getFTPer()
   {
      return FTPer;
   }
   public int getPoints()
   {
      return points;
   }
   public int getORebounds()
   {
      return oRebounds;
   }
   public int getRebounds()
   {
      return rebounds;
   }
   public int getAssists()
   {
      return assists;
   }
   public int getSteals()
   {
      return steals;
   }
   public int getBlocks()
   {
      return blocks;
   }
   public int getFouls()
   {
      return fouls;
   }
   public int getDeflections()
   {
      return deflections;
   }
   public int getChargesTaken()
   {
      return chargesTaken;
   }
   public int getTurnovers()
   {
      return turnovers;
   }
   public double round(double d)
   {
      d = Math.round(d*10);
      d = d/10;
      return d;
   }  
   public void addShot(int x, int y, boolean good)
   {
      shotsX.add(x);
      shotsY.add(y);
      made.add(good);
   }
   public ArrayList<Integer> getShotsX()
   {
      return shotsX;
   }
   public ArrayList<Integer> getShotsY()
   {
      return shotsY;
   }
   public ArrayList<Boolean> getMadeMissed()
   {
      return made;
   }


}
