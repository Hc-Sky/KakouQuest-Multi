package fr.studiokakou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.network.message.ChangePlayerStatsMessage;
import fr.studiokakou.network.message.ConnectMessage;
import fr.studiokakou.network.message.IdMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class GameServer implements Listener {
    public Server server;

    public int PORT;
    public int udp;
    public int maxPlayer;
    public String serverName;

    public ServerMap map;

    Thread commandThread;

    //tableau des joueurs (id, onlinePlayer)
    public static Map<Integer, OnlinePlayer> onlinePlayers = new HashMap<>();

    public GameServer(){
        server = new Server((int)2e6, (int)5e5);

        this.PORT = GetConfig.getIntProperty("PORT");
        this.udp = GetConfig.getIntProperty("UDP_PORT");
        this.maxPlayer = GetConfig.getIntProperty("MAX_PLAYER");
        this.serverName = GetConfig.getStringProperty("SERVER_NAME");

        this.commandThread = new Thread(new CommandsManager(this));

        onlinePlayers.clear();

        System.out.println("Creating map...");
        this.map = new ServerMap(80, 80);
        System.out.println("Map created");
    }

    public void startServer() throws IOException {
        SharedFunctions.getSharedRegister(server.getKryo());

        server.start();
        server.bind(PORT, udp);
        server.addListener(this);
        System.out.println("Server started on port : " + PORT);
        System.out.println("Server IP Adress : " + InetAddress.getLocalHost().getHostAddress());

        commandThread.start();
    }

    public void received(Connection connection, Object object) {
        if (object instanceof ConnectMessage){
            connection.setKeepAliveTCP(8000);
            connection.setTimeout(120000);
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

        if (object instanceof String){
            String message = (String) object;

            if (message.equals("getMap")){
                server.sendToTCP(connection.getID(), map);
                OnlinePlayer player = onlinePlayers.get(connection.getID());
                player.pos = map.getPlayerSpawn();
                onlinePlayers.get(connection.getID()).hasPlayerSpawn = true;
                server.sendToTCP(connection.getID(), new ChangePlayerStatsMessage(player));
            }
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

    public void changePlayerStats(OnlinePlayer player){
        System.out.println("Sending stat changes");
        server.sendToTCP(getIdWithUsername(player.username), new ChangePlayerStatsMessage(player));
    }


    static int researchInt;
    public int getIdWithUsername(String username){
        onlinePlayers.forEach((integer, onlinePlayer) -> {
            if(Objects.equals(onlinePlayer.username, username)){
                researchInt = integer.intValue();
            }
        });

        System.out.println("found id : "+researchInt);
        return researchInt;
    }
}
