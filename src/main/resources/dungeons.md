## Dungeons System



## Game Seperation

```mermaid
graph TD
    Party_Manager -- "Create_New_Party()" --> Dungeon_Manager
    Dungeon_Manager -- "Create_New_Dungeon_Session()" --> Game_Manager
    Game_Manager -- "Create_Game_Session" --> Game_Object
    Game_Object -- "Cancelled Game" --> Dungeon_Manager
    Game_Object --> Set_Campaign
    Set_Campaign --> Set_Map
    Set_Map -- "Is already being played" --> Set_Campaign
    
    
    
    
```

A Game is like a session, it is an abstract class that handles a game locally.
Methods: 
```JAVA
public abstract class Game {
    
    private int minPartySize;
    
    public Game (int minPartySize) {
        
    }
    
    public void onGameReady () {
        // Is triggered when all criteria for the Game is ready. 
    }
}
``` 
