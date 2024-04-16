package fr.studiokakou.network.message;

import fr.studiokakou.kakouquest.player.OnlinePlayer;

public class ConnectMessage {
    public int id;
    public OnlinePlayer player;

    public ConnectMessage(OnlinePlayer player){
        this.player=player;
        this.id = player.id;
    }

    public ConnectMessage(){}
}
