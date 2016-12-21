# Basketball-Stat-Tracker

Basketball Stat Tracker Program
Robert Fox

Features

   •	Court diagram
  
    o	Use graphics object and paintComponent method
    
  •	Buttons on right side to update stats
  
    o	Update team stats as well as player stats
    
    o	Use “current” player (set with left side buttons) to update individual stats
    
  •	Lower labels and made miss buttons
  
    o	Displays team statistics
    
    o	Buttons modify “made” and “missed” Boolean variables that determine if subsequent shot is made or missed
    
  •	Right side player buttons
  
    o	When clicked, will change the “current” player object and will modify their stats based on following stat change/ shot
    
  •	Plotting shots on court
  
    o	Using a mouseListener
    
    o	Display ‘X’ or ‘O’ and determines a three or two point shot based on the x and y coordinates of mouse event
    
    o	Increments player’s and team’s stats
    
  •	Top menu bar
  
    o	Ability to view stat log and game log (pop up windows)
    
      	stat log can easily be copied and pasted to an excel spreadsheet
      
      	game log updates based on any event of game, can also manually add to log, example for other team’s plays in order to document entire flow of game
      
    o	add a player to the team (button will appear)
    
    o	view individual players’ chart (use arraylists in player class to store coordinates of shots and toggle between team and player shots
    
Player class

  •	Accessor and modifier methods to retrieve and update individual statistics and shots
  
Future Improvements

  •	Update clock feature: use in game log
  
  •	Highlighting selected player for modifying stats/ viewing shot chart
  
  •	Two teams (more like a gamecast for viewers to follow all events + stats of game
