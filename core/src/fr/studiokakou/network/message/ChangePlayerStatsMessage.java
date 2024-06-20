package fr.studiokakou.network.message;

import fr.studiokakou.kakouquest.player.OnlinePlayer;

/**
 * La classe ChangePlayerStatsMessage est un message envoyé par le serveur pour changer les statistiques d'un joueur.
 */
public class ChangePlayerStatsMessage {
    public OnlinePlayer onlinePlayer;

    public ChangePlayerStatsMessage(OnlinePlayer onlinePlayer){
        this.onlinePlayer = onlinePlayer;
    }

    public ChangePlayerStatsMessage(){}
}
