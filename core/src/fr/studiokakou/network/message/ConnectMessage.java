package fr.studiokakou.network.message;

import fr.studiokakou.kakouquest.player.OnlinePlayer;

/**
 * La classe ConnectMessage est un message envoyé lorsqu'un joueur se connecte.
 */
public class ConnectMessage {
    public OnlinePlayer player;

    public ConnectMessage(OnlinePlayer player){
        this.player=player;
    }

    public ConnectMessage(){}
}
