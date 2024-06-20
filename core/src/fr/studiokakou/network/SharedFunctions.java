package fr.studiokakou.network;

import com.esotericsoftware.kryo.Kryo;
import fr.studiokakou.kakouquest.entity.OnlineMonster;
import fr.studiokakou.kakouquest.interactive.OnlineChest;
import fr.studiokakou.kakouquest.interactive.OnlineStairs;
import fr.studiokakou.kakouquest.interactive.Stairs;
import fr.studiokakou.kakouquest.map.*;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.kakouquest.weapon.OnlineMeleeWeapon;
import fr.studiokakou.network.message.ChangePlayerStatsMessage;
import fr.studiokakou.network.message.ConnectMessage;
import fr.studiokakou.network.message.IdMessage;
import fr.studiokakou.network.message.PlayerHitMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * SharedFunctions class contains functions that are shared between the server and the client.
 * This is a particularity of the KryoNet library.
 * Because you have to specify every class that you want to send over the network, I created a Online Version of
 * every class because things like Texture or Animation can't be send over the network.
 */
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
        kryo.register(OnlineStairs.class);
        kryo.register(OnlineChest.class);
        kryo.register(OnlineMonster.class);
        kryo.register(PlayerHitMessage.class);
        kryo.register(Double.class);

    }
}
