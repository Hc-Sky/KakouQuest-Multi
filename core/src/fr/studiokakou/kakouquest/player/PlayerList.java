package fr.studiokakou.kakouquest.player;

import java.util.ArrayList;

public class PlayerList {
    public ArrayList<OnlinePlayer> onlinePlayersArrayList;

    public PlayerList(){
        this.onlinePlayersArrayList = new ArrayList<>();
    }
    public PlayerList(ArrayList<OnlinePlayer> players){
        this.onlinePlayersArrayList = players;
    }
}
