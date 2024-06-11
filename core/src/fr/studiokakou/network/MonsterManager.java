package fr.studiokakou.network;

import fr.studiokakou.kakouquest.entity.OnlineMonster;

/**
 * La classe MonsterManager est un thread qui gère les monstres du serveur.
 */
public class MonsterManager implements Runnable {

    GameServer gameServer;

    /**
     * Constructeur de la classe MonsterManager.
     * @param gameServer Le serveur de jeu.
     */
    public MonsterManager(GameServer gameServer){
        this.gameServer = gameServer;
    }

    /**
     * Méthode appelée lors du démarrage du thread.
     */
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
            for (int i = 0; i < gameServer.monsters.size(); i++) {
                if (gameServer.monsters.get(i).isDead) {
                    gameServer.monsters.remove(i);
                    i--;
                }
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
