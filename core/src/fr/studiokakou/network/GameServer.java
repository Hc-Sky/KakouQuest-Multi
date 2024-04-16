package fr.studiokakou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import fr.studiokakou.kakouquest.player.OnlinePlayer;

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
    Hashtable<Integer, OnlinePlayer> onlinePlayers = new Hashtable<>();

    public GameServer(){
        this.server = new Server();

        this.PORT = GetConfig.getIntProperty("PORT");
        this.udp = GetConfig.getIntProperty("UDP_PORT");
        this.maxPlayer = GetConfig.getIntProperty("MAX_PLAYER");
        this.serverName = GetConfig.getStringProperty("SERVER_NAME");

        this.onlinePlayers.clear();
    }

    public void startServer() throws IOException {
        SharedFunctions.getSharedRegister(server.getKryo());

        server.start();
        server.bind(PORT, udp);
        server.addListener(this);
        System.out.println("Server started on port : " + PORT);
    }

    public void received(Connection connection, Object object) {
        if (object instanceof OnlinePlayer){
            OnlinePlayer player = (OnlinePlayer) object;
            onlinePlayers.replace(connection.getID(), player);

            sendAllExcept(connection.getID());
        }
    }

    public void connected(Connection connection) {
        if (this.onlinePlayers.contains(connection.getID())){
            this.onlinePlayers.remove(connection.getID());
        }
        this.onlinePlayers.put(connection.getID(), null);
    }

    public void disconnected(Connection connection) {
        this.onlinePlayers.remove(connection.getID());
    }

    public void sendAllExcept(int id){
        Enumeration<Integer> keys = onlinePlayers.keys();

        while(keys.hasMoreElements()) {
            Integer key = keys.nextElement();
            if (id != key){
                server.sendToTCP(key, onlinePlayers);
            }
        }
    }
}
