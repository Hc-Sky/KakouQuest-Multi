package fr.studiokakou.network;

import com.esotericsoftware.kryo.Kryo;
import fr.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.OnlinePlayer;

public class SharedFunctions {

    public static void getSharedRegister(Kryo kryo){
        kryo.register(OnlinePlayer.class);
    }
}
