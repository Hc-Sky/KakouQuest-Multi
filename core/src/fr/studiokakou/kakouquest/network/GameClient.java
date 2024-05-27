package fr.studiokakou.kakouquest.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
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

import java.io.IOException;

public class GameClient implements Listener {
    public Client client;

    String ipAdress;
    int port;
    int udp;
    int id;

    Player player;

    public static boolean isConnected = false;

    Thread sendingThread;

    public GameClient(String ipAdress, int tcp_port, int udp_port, Player player){
        this.player = player;
        this.client = new Client((int)2e6, (int)5e5);

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

            System.out.println("Received a map");
            System.out.println(onlineMap.floors.size());

            OnlineGameScreen.map = new Map(onlineMap);
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

    public void sendPlayer(Player player){
        try {
            client.sendTCP(OnlinePlayerConstants.mainToOnlinePlayer(player));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
