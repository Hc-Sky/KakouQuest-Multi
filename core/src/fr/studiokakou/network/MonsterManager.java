package fr.studiokakou.network;

import fr.studiokakou.kakouquest.entity.OnlineMonster;

import java.util.ArrayList;

public class MonsterManager implements Runnable {

    GameServer gameServer;

    public MonsterManager(GameServer gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        while (true) {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;

            OnlineMonster.delta = (float) deltaTime;

            // Update monsters
            for (int i = 0; i < gameServer.monsters.size(); i++) {
                gameServer.monsters.get(i).move(gameServer.map);
                gameServer.monsters.get(i).updateHit();
            }
            gameServer.sendMonstersToAll();
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
