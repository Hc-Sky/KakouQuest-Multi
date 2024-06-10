package fr.studiokakou.kakouquest.entity;

import com.badlogic.gdx.Gdx;
import fr.studiokakou.kakouquest.map.Floor;
import fr.studiokakou.kakouquest.map.Map;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.network.GameServer;
import fr.studiokakou.network.ServerMap;
import fr.studiokakou.network.message.PlayerHitMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OnlineMonster {
    public String name;
    public Point pos;
    public float speed;
    public int damage;

    /** The time pause between attacks. */
    public float attackPause;

    public LocalDateTime currentAttackTime;
    public int hp;
    public int detectRange;

    public float height;
    public float width;

    //animation vars
    public boolean isRunning;
    public boolean isFlip;

    public boolean isDying;
    public boolean isDead;

    public boolean isRed;
    public LocalDateTime hitStart=null;
    public float bloodStateTime=0f;

    public String idleAnimationPath;
    public String runAnimationPath;

    public static float delta = 0.016664876f;

    public ArrayList<String> player_hitted = new ArrayList<>();

    public int id;

    public OnlineMonster(String name, String idleAnimationPath, String runAnimationPath, int hp, int damage, float attackPause, float speed, int detectRange, int currentLevel, double height, double width){
        this.name = name;
        this.speed = speed;
        this.damage = damage;
        this.attackPause = attackPause;
        this.hp = hp;
        this.detectRange = detectRange;
        this.idleAnimationPath = idleAnimationPath;
        this.runAnimationPath = runAnimationPath;
        this.isRed=false;
        this.upgradeStats(currentLevel);
        this.height = (float) height;
        this.width = (float) width;
        this.isFlip =  Utils.randint(0, 1)==0;
        this.isRunning = false;
        this.player_hitted.clear();
        this.isDying = false;
        this.isDead = false;
    }

    public OnlineMonster (Monster monster){
        this.name = monster.name;
        this.speed = monster.speed;
        this.damage = monster.damage;
        this.attackPause = monster.attackPause;
        this.hp = monster.hp;
        this.detectRange = monster.detectRange;
        this.idleAnimationPath = monster.idleAnimationPath;
        this.runAnimationPath = monster.runAnimationPath;
        this.isRed=monster.isRed;
        this.width = monster.width;
        this.height = monster.height;
        this.pos = monster.pos;
        this.currentAttackTime = monster.currentAttackTime;
        this.isDying = monster.isDying;
        this.isDead = monster.isDead;
        this.isRunning = monster.isRunning;
        this.isFlip = monster.isFlip;
        this.player_hitted = monster.player_hitted;
        this.id = monster.id;
        this.hitStart = monster.hitStart;

    }

    public OnlineMonster(){}

    public void setId(int id){
        this.id = id;
    }

    public void place(Point pos){
        this.pos = pos;
    }

    public void upgradeStats(int currentLevel){
        this.hp = this.hp +(this.hp * currentLevel/4);
        this.damage = this.damage + (this.damage * currentLevel /4);
    }

    public Point center(){
        return new Point(this.pos.x+ this.width /2,this.pos.y+ this.height /4);
    }

    public boolean canMove(Point orientation, ServerMap map){
        if (this.isDead || this.isDying){
            return false;
        }

        if (this.pos== null){
            return false;
        }

        Point newPos = this.pos.add(orientation.x*(this.speed)*delta, orientation.y*(this.speed)*delta);
        Point hitboxTopLeft = newPos.add(3, this.height - Floor.TEXTURE_HEIGHT);
        Point hitboxBottomLeft = newPos.add(3, 0);
        Point hitboxTopRight = newPos.add(this.width-3, this.height - Floor.TEXTURE_HEIGHT);
        Point hitboxBottomRight = newPos.add(this.width-3, 0);

        Point[] points = {hitboxTopLeft, hitboxBottomLeft, hitboxTopRight, hitboxBottomRight};

        return map.arePointsOnFloor(points);
    }

    public OnlinePlayer getClosestPlayer(){
        OnlinePlayer closestPlayer = null;
        double minDistance = Double.MAX_VALUE;
        for (OnlinePlayer player : GameServer.onlinePlayers.values()){
            double distance = Utils.distance(this.pos, player.pos);
            if (distance<minDistance && player.hasPlayerSpawn && !player.isDead ){
                minDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    public void move(ServerMap map){
        OnlinePlayer player = this.getClosestPlayer();


        if (player==null){
            return;
        }

        if (isDying || isRed || !player.hasPlayerSpawn){
            return;
        }

        Point playerPos = player.pos;

        if (Utils.distance(playerPos, this.pos)<=10){
            this.attack(player);
            return;
        }

        if (detectPlayer(playerPos)){

            this.isRunning = true;
            this.getOrientation(player);
            Point orientation = Point.getOrientation(this.pos, playerPos);
            if (canMove(new Point(orientation.x, 0), map)){
                this.pos = this.pos.add(orientation.x*(this.speed)*delta, 0);
            }
            if (canMove(new Point(0, orientation.y), map)){
                this.pos = this.pos.add(0, orientation.y*(this.speed)*delta);
            }
        }else {
            this.isRunning=false;
        }
    }

    private void attack(OnlinePlayer player) {
        if (this.isDying || !player.hasPlayerSpawn){
            return;
        }
        if (this.currentAttackTime==null || this.currentAttackTime.plusNanos((long) (1000000*this.attackPause)).isBefore(LocalDateTime.now())){
            GameServer.server.sendToTCP(GameServer.getIdWithUsername(player.username), new PlayerHitMessage(this));
            this.currentAttackTime = LocalDateTime.now();
        }
    }

    public void getOrientation(OnlinePlayer player){
        if (player.center().x-1<this.center().x){
            this.isFlip = true;
        }else {
            this.isFlip=false;
        }
    }

    public void updateHit(){
        if (hitStart!= null && this.isRed && this.hitStart.plusNanos(200000000).isBefore(LocalDateTime.now())){
            this.isRed=false;
            this.player_hitted.remove(this.player_hitted.get(0));
            this.hitStart=null;
            if (isDying){
                this.isDead=true;
            }
        }
    }

    public boolean detectPlayer(Point playerPos){
        return Utils.distance(this.pos, playerPos) <= this.detectRange;
    }
}
