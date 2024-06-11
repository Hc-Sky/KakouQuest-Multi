package fr.studiokakou.kakouquest.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.studiokakou.kakouquest.entity.Monster;
import fr.studiokakou.kakouquest.entity.OnlineMonster;
import fr.studiokakou.kakouquest.interactive.Chest;
import fr.studiokakou.kakouquest.interactive.OnlineChest;
import fr.studiokakou.kakouquest.interactive.OnlineStairs;
import fr.studiokakou.kakouquest.map.Map;
import fr.studiokakou.kakouquest.player.OnlinePlayerConstants;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.network.ServerMap;
import fr.studiokakou.network.SharedFunctions;
import fr.studiokakou.network.message.ChangePlayerStatsMessage;
import fr.studiokakou.network.message.ConnectMessage;
import fr.studiokakou.network.message.IdMessage;
import fr.studiokakou.network.message.PlayerHitMessage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The GameClient class represents a client for the game's network communication.
 * It connects to the server and handles incoming and outgoing messages.
 */
public class GameClient implements Listener {
    /**
     * The KryoNet client used for network communication.
     */
    public Client client;

    String ipAdress;
    int port;
    int udp;
    int id;

    Player player;


    public static boolean isConnected = false;
    Thread sendingThread;

    /**
     * Constructs a GameClient object with the specified server IP address, TCP port, UDP port, and player.
     *
     * @param ipAdress the IP address of the server
     * @param tcp_port the TCP port of the server
     * @param udp_port the UDP port of the server
     * @param player   the local player object
     */
    public GameClient(String ipAdress, int tcp_port, int udp_port, Player player) {
        this.player = player;
        this.client = new Client((int) 2e6, (int) 5e5);

        SharedFunctions.getSharedRegister(client.getKryo());

        this.ipAdress = ipAdress;
        this.port = tcp_port;
        this.udp = udp_port;
    }

    /**
     * Starts the client and establishes a connection to the server.
     */
    public void startClient() {
        client.start();

        try {
            client.connect(5000, ipAdress, port, udp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.addListener(this);
    }

    /**
     * Starts the sending thread for continuously sending player updates to the server.
     */
    public void startSendingThread() {
        this.sendingThread = new Thread(() -> {
            while (isConnected) {
                try {
                    if (player != null && player.pos != null){
                        client.sendTCP(OnlinePlayerConstants.mainToOnlinePlayer(player));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        sendingThread.start();
    }

    // Listener methods

    public void connected(Connection connection) {
        isConnected = true;
        client.sendTCP(new ConnectMessage(OnlinePlayerConstants.mainToOnlinePlayer(player)));
        startSendingThread();
    }

    public void disconnected(Connection connection) {
        isConnected = false;
        System.out.println("You got disconnected from the game");
        System.exit(0);
    }

    /**
     * Handles incoming messages from the server.
     * Uses instanceof to determine the type of the message and processes it accordingly.
     *
     * @param connection
     * @param object
     */
    public void received(Connection connection, Object object) {
        if (object instanceof IdMessage){
            IdMessage message = (IdMessage) object;
            this.id = message.id;
        }

        if (object instanceof ChangePlayerStatsMessage){
            ChangePlayerStatsMessage changePlayerStatsMessage = (ChangePlayerStatsMessage) object;

            System.out.println("received a stats change");
            player.changePlayerStats(changePlayerStatsMessage.onlinePlayer);
        }

        if (object instanceof PlayerList) {
            PlayerList playerList = (PlayerList) object;

            playerList.removePlayer(OnlineGameScreen.username);

            OnlineGameScreen.onlinePlayers = playerList.onlinePlayersArrayList;
        }

        if (object instanceof ServerMap){
            ServerMap onlineMap = (ServerMap) object;

            OnlineGameScreen.map = new Map(onlineMap);
        }

        if (object instanceof OnlineStairs){
            OnlineStairs onlineStairs = (OnlineStairs) object;

            OnlineGameScreen.map.stairs = onlineStairs.toStairs();
            OnlineGameScreen.map.interactives.add(OnlineGameScreen.map.stairs);
        }

        if (object instanceof PlayerHitMessage){
            PlayerHitMessage playerHitMessage = (PlayerHitMessage) object;

            OnlineGameScreen.player.takeDamage(playerHitMessage.monster.damage);
            System.out.println("You got hit by a monster : "+OnlineGameScreen.player.hp);
        }

        if (object instanceof ArrayList){
            Object firstElem = ((ArrayList<?>) object).get(0);
            if (firstElem instanceof OnlineChest){
                Map.chests.clear();
                for (Object o : (ArrayList<?>) object){
                    OnlineChest chest = (OnlineChest) o;
                    Map.chests.add(new Chest(chest));
                }
            }

            if (firstElem instanceof OnlineMonster){

                if (!OnlineGameScreen.map.drawingMonsters){
                    Map.monsters.clear();
                    for (Object o : (ArrayList<?>) object){
                        OnlineMonster monster = (OnlineMonster) o;
                        Map.monsters.add(new Monster(monster));
                    }
                }
            }
        }

        if (object instanceof String){
            String command = (String) object;

            System.out.println("Received a command");

            if (command.equals("kick")) {
                System.out.println("you got kicked");
                client.stop();
                client.close();
                System.exit(0);
            }
        }
    }

    /**
     * Sends the local player object to the server.
     *
     * @param player the local player object
     */
    public void sendPlayer(Player player){
        try {
            client.sendTCP(OnlinePlayerConstants.mainToOnlinePlayer(player));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
