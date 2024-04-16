package fr.studiokakou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import fr.studiokakou.kakouquest.player.OnlinePlayer;

import java.util.ArrayList;

public class GameServer implements Listener {
    Server server;

    public int PORT;
    public int udp;
    public int maxPlayer;
    public String serverName;

    ArrayList<OnlinePlayer> onlinePlayers = new ArrayList<>();

    public GameServer(){
        this.server = new Server();

        this.PORT = GetConfig.getIntProperty("PORT");
        this.udp = GetConfig.getIntProperty("UDP_PORT");
        this.maxPlayer = GetConfig.getIntProperty("MAX_PLAYER");
        this.serverName = GetConfig.getStringProperty("SERVER_NAME");

        this.onlinePlayers.clear();
    }

    @Override
    public void connected(Connection connection) {

    }
}
