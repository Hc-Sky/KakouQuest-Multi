package fr.studiokakou.network.message;

import fr.studiokakou.kakouquest.player.OnlinePlayer;

public class ChangePlayerStatsMessage {
    public OnlinePlayer onlinePlayer;

    public ChangePlayerStatsMessage(OnlinePlayer onlinePlayer){
        this.onlinePlayer = onlinePlayer;
    }

    public ChangePlayerStatsMessage(){}
}
