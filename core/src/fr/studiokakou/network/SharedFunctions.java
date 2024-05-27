package fr.studiokakou.network;

import com.esotericsoftware.kryo.Kryo;
import fr.studiokakou.kakouquest.map.*;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.kakouquest.weapon.OnlineMeleeWeapon;
import fr.studiokakou.network.message.ChangePlayerStatsMessage;
import fr.studiokakou.network.message.ConnectMessage;
import fr.studiokakou.network.message.IdMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SharedFunctions {

    public static void getSharedRegister(Kryo kryo){
        kryo.register(ConnectMessage.class);
        kryo.register(OnlineMeleeWeapon.class);
        kryo.register(OnlinePlayer.class);
        kryo.register(PlayerList.class);
        kryo.register(LocalDateTime.class);
        kryo.register(Point.class);
        kryo.register(ArrayList.class);
        kryo.register(IdMessage.class);
        kryo.register(ChangePlayerStatsMessage.class);
        kryo.register(OnlineWall.class);
        kryo.register(OnlineFloor.class);
        kryo.register(Bridge.class);
        kryo.register(Room.class);
        kryo.register(ServerMap.class);

    }
}
