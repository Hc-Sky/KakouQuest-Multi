package fr.studiokakou.network;

import com.esotericsoftware.kryo.Kryo;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.network.message.ConnectMessage;

import java.util.ArrayList;

public class SharedFunctions {

    public static void getSharedRegister(Kryo kryo){
        kryo.register(OnlinePlayer.class);
        kryo.register(PlayerList.class);
        kryo.register(ConnectMessage.class);
    }
}
