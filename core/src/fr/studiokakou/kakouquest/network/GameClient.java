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
import fr.studiokakou.network.message.ConnectMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GameClient implements Listener {
    Client client;

    String ipAdress;
    int port;
    int udp;

    Player player;

    //Thread clientThread;

    public GameClient(String ipAdress, int tcp_port, int udp_port, Player player){
        this.player = player;
        this.client = new Client();
        //this.clientThread = new Thread(client);

        SharedFunctions.getSharedRegister(client.getKryo());

        this.ipAdress = ipAdress;
        this.port = tcp_port;
        this.udp = udp_port;
    }

    public void startClient() {
        //clientThread.start();
        client.start();

        try {
            client.connect(5000, ipAdress, port, udp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.addListener(this);

    }


    public void connected(Connection connection) {
        client.sendTCP(new ConnectMessage(OnlinePlayerConstants.mainToOnlinePlayer(player)));
    }

    public void received(Connection connection, Object object) {
        System.out.println("received");
        if (object instanceof PlayerList) {
            PlayerList playerList = (PlayerList) object;

            for (OnlinePlayer onlinePlayer : playerList.onlinePlayersArrayList){
                System.out.println(onlinePlayer.username);
            }

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
