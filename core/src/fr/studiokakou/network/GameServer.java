package fr.studiokakou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import fr.studiokakou.kakouquest.entity.OnlineMonster;
import fr.studiokakou.kakouquest.entity.StaticMonster;
import fr.studiokakou.kakouquest.interactive.OnlineStairs;
import fr.studiokakou.kakouquest.map.Floor;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.map.Room;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.OnlinePlayerConstants;
import fr.studiokakou.kakouquest.player.PlayerList;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.StaticsMeleeWeapon;
import fr.studiokakou.network.message.ChangePlayerStatsMessage;
import fr.studiokakou.network.message.ConnectMessage;
import fr.studiokakou.network.message.IdMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class GameServer implements Listener {
    public static Server server;

    public int PORT;
    public int udp;
    public int maxPlayer;
    public String serverName;

    //map & elements
    public static int currentLevel;
    public ServerMap map;
    public OnlineStairs stairs;
    public ArrayList<OnlineMonster> monsters = new ArrayList<>();

    Thread commandThread;
    Thread monsterThread;

    //tableau des joueurs (id, onlinePlayer)
    public static Map<Integer, OnlinePlayer> onlinePlayers = new HashMap<>();

    public GameServer(){
        server = new Server((int)2e6, (int)5e5);

        this.PORT = GetConfig.getIntProperty("PORT");
        this.udp = GetConfig.getIntProperty("UDP_PORT");
        this.maxPlayer = GetConfig.getIntProperty("MAX_PLAYER");
        this.serverName = GetConfig.getStringProperty("SERVER_NAME");

        this.commandThread = new Thread(new CommandsManager(this));
        this.monsterThread = new Thread(new MonsterManager(this));

        onlinePlayers.clear();

        System.out.println("Creating map...");

        currentLevel=1;

        StaticsMeleeWeapon.createPossibleMeleeWeapons();
        this.map = new ServerMap(80, 80);
        System.out.println("Map created");
        this.stairs = new OnlineStairs(map.getStairsPos());

        System.out.println("Generating monsters...");
        StaticMonster.createPossibleMonsters(currentLevel);
        genMonsters();
        System.out.println("Monsters generated");

    }

    public void restart(){
        System.out.println("Creating map...");

        currentLevel=1;
        this.map = new ServerMap(80, 80);
        this.stairs = new OnlineStairs(map.getStairsPos());
        StaticMonster.createPossibleMonsters(currentLevel);
        genMonsters();

        sendMapToAll();
        sendMonstersToAll();

        for (OnlinePlayer player : onlinePlayers.values()){
            player.pos = map.getPlayerSpawn();
            player.hasPlayerSpawn = true;
            player.isDead= false;

            player.max_hp= OnlinePlayerConstants.defaultHp;
            player.hp=OnlinePlayerConstants.defaultHp;
            player.strength=OnlinePlayerConstants.defaultStrength;
            player.speed=OnlinePlayerConstants.defaultSpeed;
            player.max_stamina = OnlinePlayerConstants.defaultStamina;
            player.stamina = OnlinePlayerConstants.defaultStamina;

            player.isAttacking=false;
            player.dashStartPoint=null;
            player.dashOrientation=null;
            player.canDash=true;
            changePlayerStats(player);
        }
    }

    public void genMonsters(){
        monsters.clear();
        ArrayList<Integer> randomRarity = new ArrayList<>();

        int currentID=0;

        for (int i = 1; i <= currentLevel; i++) {
            for (int j = 0; j <= currentLevel-i; j++) {
                if (StaticMonster.possibleMonsters.get(i)!=null){
                    randomRarity.add(i);
                }
            }
        }

        for (Room r : map.rooms.subList(1, map.rooms.size())){
            for (int i = (int) r.start.x+1; i < r.end.x-1; i++) {
                if (Utils.randint(0, 7)==0){
                    int rarity = randomRarity.get(Utils.randint(0, randomRarity.size() - 1));
                    ArrayList<OnlineMonster> mList = StaticMonster.possibleMonsters.get(rarity);
                    while ( mList==null || mList.isEmpty()){
                        rarity = randomRarity.get(Utils.randint(0, randomRarity.size() - 1));
                        mList = StaticMonster.possibleMonsters.get(rarity);
                    }
                    OnlineMonster m = mList.get(Utils.randint(0, mList.size()-1));
                    m.place(new Point(i* Floor.TEXTURE_WIDTH, Utils.randint((int) r.start.y+1, (int) r.end.y-1)*Floor.TEXTURE_HEIGHT));
                    m.setId(currentID);
                    monsters.add(m);
                    StaticMonster.createPossibleMonsters(currentLevel);
                    currentID++;
                }
            }
        }
    }

    public void nextLevel(){
        currentLevel++;
        this.map = new ServerMap(80, 80);
        this.stairs = new OnlineStairs(map.getStairsPos());
        StaticMonster.createPossibleMonsters(currentLevel);
        genMonsters();

        sendMapToAll();
        sendMonstersToAll();
        for (OnlinePlayer player : onlinePlayers.values()){
            player.pos = map.getPlayerSpawn();
            player.isDead = false;
            player.hp = player.max_hp;
            player.isAttacking=false;
            player.dashStartPoint=null;
            player.dashOrientation=null;
            player.canDash=true;
            changePlayerStats(player);
        }
    }

    public void startServer() throws IOException {
        SharedFunctions.getSharedRegister(server.getKryo());

        server.start();
        server.bind(PORT, udp);
        server.addListener(this);
        System.out.println("Server started on port : " + PORT);
        System.out.println("Server IP Adress : " + InetAddress.getLocalHost().getHostAddress());

        commandThread.start();
        monsterThread.start();
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
            OnlinePlayer onlinePlayer = (OnlinePlayer) object;

            onlinePlayers.replace(connection.getID(), onlinePlayer);

            if (onlinePlayer.isDead){
                boolean allDead=true;
                for (OnlinePlayer p : onlinePlayers.values()){
                    if (!p.isDead){
                        allDead=false;
                    }
                }

                if (allDead){
                    System.out.println("All players are dead");
                    restart();
                }
            }

            sendPlayersToAll();
        }

        if (object instanceof OnlineMonster){
            System.out.println("Received monster");
            OnlineMonster monster = (OnlineMonster) object;
            monsters.forEach(m -> {
                if (m.id == monster.id){
                    System.out.println("Taking damage "+monster.hp);
                    m.hp = monster.hp;
                    m.isDying = monster.isDying;
                    m.isRed = monster.isRed;
                    m.bloodStateTime = monster.bloodStateTime;
                    m.hitStart = monster.hitStart;
                    m.player_hitted = monster.player_hitted;
                    m.currentAttackTime = monster.currentAttackTime;
                }
            });
        }

        if (object instanceof ArrayList){
            Object firstElem = ((ArrayList<?>) object).get(0);
        }

        if (object instanceof String){
            String message = (String) object;

            if (message.equals("getMap")){
                server.sendToTCP(connection.getID(), map);
                server.sendToTCP(connection.getID(), stairs);
                server.sendToTCP(connection.getID(), monsters);
                OnlinePlayer player = onlinePlayers.get(connection.getID());
                player.pos = map.getPlayerSpawn();
                onlinePlayers.get(connection.getID()).hasPlayerSpawn = true;
                server.sendToTCP(connection.getID(), new ChangePlayerStatsMessage(player));
            }

            if (message.equals("stairs")){
                OnlinePlayer player = onlinePlayers.get(connection.getID());
                if (!this.stairs.players.contains(player)){
                    this.stairs.players.add(player);
                    player.hasPlayerSpawn = false;
                    player.pos = new Point(-100, -100);
                    server.sendToTCP(connection.getID(), new ChangePlayerStatsMessage(player));

                    if (this.stairs.players.size() == onlinePlayers.size()){
                        nextLevel();
                    }
                }
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

    public void sendMapToAll(){
        server.sendToAllTCP(map);
        server.sendToAllTCP(stairs);
    }

    public void sendMonstersToAll(){
        server.sendToAllTCP(monsters);
    }

    public void changePlayerStats(OnlinePlayer player){
        System.out.println("Sending stat changes");
        server.sendToTCP(getIdWithUsername(player.username), new ChangePlayerStatsMessage(player));
    }


    static int researchInt;
    public static int getIdWithUsername(String username){
        onlinePlayers.forEach((integer, onlinePlayer) -> {
            if(Objects.equals(onlinePlayer.username, username)){
                researchInt = integer.intValue();
            }
        });

        System.out.println("found id : "+researchInt);
        return researchInt;
    }
}
