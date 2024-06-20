package fr.studiokakou.network.message;

import fr.studiokakou.kakouquest.entity.OnlineMonster;

/**
 * Message envoyé lorsqu'un joueur frappe un monstre.
 */
public class PlayerHitMessage {
    public OnlineMonster monster;

    public PlayerHitMessage(OnlineMonster monster) {
        this.monster = monster;
    }

    public PlayerHitMessage() {
    }
}
