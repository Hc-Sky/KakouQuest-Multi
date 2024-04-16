package fr.studiokakou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.network.message.ConnectMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class GameServer implements Listener {
    Server server;

    public int PORT;
    public int udp;
    public int maxPlayer;
    public String serverName;

    //tableau des joueurs (id, onlinePlayer)
    public static Hashtable<Integer, OnlinePlayer> onlinePlayers = new Hashtable<>();

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
    }

    public void received(Connection connection, Object object) {
        if (object instanceof ConnectMessage){
            ConnectMessage connectMessage = (ConnectMessage) object;
            OnlinePlayer onlinePlayer = connectMessage.player;
            int id = connectMessage.id;
            if (onlinePlayers.contains(connection.getID())){
                onlinePlayers.remove(connection.getID());
            }
            onlinePlayers.put(connection.getID(), onlinePlayer);

            for (OnlinePlayer player : onlinePlayers.values()){
                System.out.println(player.username);
            }
        }

        if (object instanceof OnlinePlayer){
            OnlinePlayer player = (OnlinePlayer) object;
            onlinePlayers.replace(connection.getID(), player);

            sendAllExcept(connection.getID());
        }
    }

//    public void disconnected(Connection connection) {
//        onlinePlayers.remove(connection.getID());
//    }

    public void sendAllExcept(int id){
        Enumeration<Integer> keys = onlinePlayers.keys();
        PlayerList playerList = new PlayerList();

        while(keys.hasMoreElements()) {
            Integer key = keys.nextElement();
            if (id != key){
                playerList.onlinePlayersArrayList.add(onlinePlayers.get(key));
            }
        }


        keys = onlinePlayers.keys();
        while(keys.hasMoreElements()) {
            Integer key = keys.nextElement();
            if (id != key){
                server.sendToTCP(key, playerList);
            }
        }
    }
}
