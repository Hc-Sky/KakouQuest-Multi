package fr.studiokakou.kakouquest.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.OnlinePlayerConstants;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.network.SharedFunctions;
import fr.studiokakou.network.message.ChangePlayerStatsMessage;
import fr.studiokakou.network.message.ConnectMessage;
import fr.studiokakou.network.message.IdMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GameClient implements Listener {
    Client client;

    String ipAdress;
    int port;
    int udp;
    int id;

    Player player;

    public static boolean isConnected = false;

    Thread sendingThread;

    public GameClient(String ipAdress, int tcp_port, int udp_port, Player player){
        this.player = player;
        this.client = new Client();

        SharedFunctions.getSharedRegister(client.getKryo());

        this.ipAdress = ipAdress;
        this.port = tcp_port;
        this.udp = udp_port;
    }

    public void startClient() {
        client.start();

        try {
            client.connect(5000, ipAdress, port, udp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.addListener(this);

    }

    public void startSendingThread(){
        this.sendingThread = new Thread(() -> {
            while (isConnected){
                try {
                    client.sendTCP(OnlinePlayerConstants.mainToOnlinePlayer(player));
                } catch (Exception e){
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

    public void received(Connection connection, Object object) {
        if (object instanceof IdMessage){
            IdMessage message = (IdMessage) object;
            this.id = message.id;
        }

        if (object instanceof ChangePlayerStatsMessage){
            System.out.println("Received a stat request");

            ChangePlayerStatsMessage changePlayerStatsMessage = (ChangePlayerStatsMessage) object;

            player.changePlayerStats(changePlayerStatsMessage.onlinePlayer);
        }

        if (object instanceof PlayerList) {
            PlayerList playerList = (PlayerList) object;

            playerList.removePlayer(OnlineGameScreen.username);

            OnlineGameScreen.onlinePlayers = playerList.onlinePlayersArrayList;
        }
    }

    public void sendPlayer(Player player){
        try {
            client.sendTCP(OnlinePlayerConstants.mainToOnlinePlayer(player));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
