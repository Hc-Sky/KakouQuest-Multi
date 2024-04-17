package fr.studiokakou.kakouquest.player;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerList {
    public ArrayList<OnlinePlayer> onlinePlayersArrayList;

    public PlayerList(){
        this.onlinePlayersArrayList = new ArrayList<>();
    }
    public PlayerList(ArrayList<OnlinePlayer> players){
        this.onlinePlayersArrayList = players;
    }

    public void removePlayer(String username){
        for (OnlinePlayer p : onlinePlayersArrayList){
            if (p.username.equals(username)){
                onlinePlayersArrayList.remove(p);
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (OnlinePlayer player : onlinePlayersArrayList){
            result.append(player.username).append(", ");
        }
        return result.toString();
    }
}
