package fr.studiokakou.network.message;

import fr.studiokakou.kakouquest.entity.OnlineMonster;

public class PlayerHitMessage {
    public OnlineMonster monster;

    public PlayerHitMessage(OnlineMonster monster) {
        this.monster = monster;
    }

    public PlayerHitMessage() {
    }
}
