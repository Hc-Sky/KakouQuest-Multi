package fr.studiokakou.network;

import com.esotericsoftware.kryo.Kryo;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.kakouquest.weapon.MeleeWeapon;
import fr.studiokakou.network.message.ConnectMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SharedFunctions {

    public static void getSharedRegister(Kryo kryo){
        kryo.register(ConnectMessage.class);
        kryo.register(MeleeWeapon.class);
        kryo.register(OnlinePlayer.class);
        kryo.register(PlayerList.class);
        kryo.register(LocalDateTime.class);
        kryo.register(Point.class);
        kryo.register(ArrayList.class);
    }
}
