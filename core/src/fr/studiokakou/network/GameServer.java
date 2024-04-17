package fr.studiokakou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.network.message.ConnectMessage;
import fr.studiokakou.network.message.IdMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class GameServer implements Listener {
    Server server;

    public int PORT;
    public int udp;
    public int maxPlayer;
    public String serverName;

    //tableau des joueurs (id, onlinePlayer)
    public static Map<Integer, OnlinePlayer> onlinePlayers = new HashMap<>();

    public GameServer(){
        this.server = new Server();

        this.PORT = GetConfig.getIntProperty("PORT");
        this.udp = GetConfig.getIntProperty("UDP_PORT");
        this.maxPlayer = GetConfig.getIntProperty("MAX_PLAYER");
        this.serverName = GetConfig.getStringProperty("SERVER_NAME");

        onlinePlayers.clear();
    }

    public void startServer() throws IOException {
        SharedFunctions.getSharedRegister(server.getKryo());

        server.start();
        server.bind(PORT, udp);
        server.addListener(this);
        System.out.println("Server started on port : " + PORT);
        System.out.println("Server IP Adress : " + InetAddress.getLocalHost().getHostAddress());
    }

    public void received(Connection connection, Object object) {
        if (object instanceof ConnectMessage){
            ConnectMessage connectMessage = (ConnectMessage) object;
            OnlinePlayer onlinePlayer = connectMessage.player;
            int id = connection.getID();
            if (onlinePlayers.containsKey(id)){
                onlinePlayers.remove(id);
            }
            onlinePlayers.put(id, onlinePlayer);

            System.out.println("New player ["+onlinePlayer.username+"] connected with id : "+connection.getID());

            server.sendToTCP(connection.getID(), new IdMessage(connection.getID()));
        }

        if (object instanceof OnlinePlayer){
            OnlinePlayer player = (OnlinePlayer) object;
            onlinePlayers.replace(connection.getID(), player);

            sendPlayersToAll();
        }
    }

    public void disconnected(Connection connection) {
        onlinePlayers.remove(connection.getID());
    }

    public void sendPlayersToAll(){
        PlayerList playerList = new PlayerList();

        playerList.onlinePlayersArrayList.addAll(onlinePlayers.values());



        onlinePlayers.forEach((integer, onlinePlayer) -> {
            server.sendToTCP(integer, playerList);
        });
    }
}
