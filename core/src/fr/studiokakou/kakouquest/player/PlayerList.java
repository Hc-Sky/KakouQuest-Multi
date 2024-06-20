package fr.studiokakou.kakouquest.player;

import java.util.ArrayList;

/**
 * This class is used to store the list of online players in the GameServer.
 */
public class PlayerList {
    public ArrayList<OnlinePlayer> onlinePlayersArrayList;

    /**
     * Default constructor
     */
    public PlayerList(){
        this.onlinePlayersArrayList = new ArrayList<>();
    }

    /**
     * removes a player from the list
     * @param username the username of the player
     */
    public void removePlayer(String username){
        for (OnlinePlayer p : onlinePlayersArrayList){
            if (p.username.equals(username)){
                onlinePlayersArrayList.remove(p);
                return;
            }
        }
    }

    /**
     * Shows the list of online players
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (OnlinePlayer player : onlinePlayersArrayList){
            result.append(player.username).append(", ");
        }
        return result.toString();
    }
}
