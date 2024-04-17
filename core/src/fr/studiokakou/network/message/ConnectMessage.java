package fr.studiokakou.network.message;

import fr.studiokakou.kakouquest.player.OnlinePlayer;

public class ConnectMessage {
    public OnlinePlayer player;

    public ConnectMessage(OnlinePlayer player){
        this.player=player;
    }

    public ConnectMessage(){}
}
