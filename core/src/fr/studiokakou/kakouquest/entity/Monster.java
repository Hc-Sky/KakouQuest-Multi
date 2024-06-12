package fr.studiokakou.kakouquest.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Monster {

    //monster stats
    public String name;
    public int id;
    public Point pos;
    public float speed;
    public int damage;
    public float attackPause;
    public int xp;
    public int hp;
    public int detectRange;

    // animiation vars
    public static int FRAME_COLS = 1;
    public static int FRAME_ROWS = 4;
    float height;
    float width;
    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> runAnimation;
    boolean isRunning;
    boolean isFlip;
    public Sprite sprite;
    public static Dictionary<String, Animation<TextureRegion>> monsterTextures = new Hashtable<>();

    // manage death
    public boolean isDying;
    public boolean isDead;

    // attack vars
    LocalDateTime currentAttackTime;
    ArrayList<String> player_hitted = new ArrayList<>();

    // player hit vars
    public boolean isRed;
    public LocalDateTime hitStart;
    Animation<TextureRegion> bloodEffect;
    float bloodStateTime=0f;

    // animation paths
    public String idleAnimationPath;
    public String runAnimationPath;


    // Monster constructor from a OnlineMonster object
    public Monster(OnlineMonster monster){
        this.name = monster.name;
        this.speed = monster.speed;
        this.damage = monster.damage;
        this.attackPause = monster.attackPause;
        this.hp = monster.hp;
        this.xp = monster.xp;

        this.detectRange = monster.detectRange;
        this.idleAnimationPath = monster.idleAnimationPath;
        this.runAnimationPath = monster.runAnimationPath;
        this.isRed=monster.isRed;
        this.isFlip = monster.isFlip;
        this.isRunning = monster.isRunning;

        this.isDead = monster.isDead;
        this.isDying = monster.isDying;

        this.idleAnimation = monsterTextures.get(this.idleAnimationPath);
        this.runAnimation = monsterTextures.get(this.runAnimationPath);
        this.bloodEffect = monsterTextures.get("assets/effects/blood.png");

        this.height = monster.height;
        this.width = monster.width;

        this.sprite = new Sprite();

        if (monster.pos != null){
            this.pos = monster.pos;
        }

        this.currentAttackTime = monster.currentAttackTime;
        this.player_hitted = monster.player_hitted;
        this.hitStart= monster.hitStart;

        this.id = monster.id;

    }

    // when a player hit this monster
    public boolean hit(Player player){
        if (!this.player_hitted.contains(OnlineGameScreen.username)){
            this.takeDamage(player);
            this.bloodStateTime=0f;
            this.isRed=true;
            this.player_hitted.add(OnlineGameScreen.username);   //save it so the player can't hit the monster again in the same attack
            this.hitStart=LocalDateTime.now();
            OnlineGameScreen.gameClient.client.sendTCP(new OnlineMonster(this));    //send the monster to the server
            return true;
        }
        return false;
    }

    // update the monster hp when he gets hit
    public void takeDamage(Player player){
        this.hp -= player.currentWeapon.damage*(player.strength/10);
        if (this.hp <= 0){
            this.isDying=true;
            player.gainExperience(this.xp);
        }
    }

    // manage the monster attack on the player
    public void attack(Player player){
        if (this.isDying || !player.hasPlayerSpawn){
            return;
        }
        if (this.currentAttackTime==null || this.currentAttackTime.plusNanos((long) (1000000*this.attackPause)).isBefore(LocalDateTime.now())){
            player.takeDamage(this.damage);
            this.currentAttackTime = LocalDateTime.now();
        }
    }


    // show the monster on screen
    public void draw(SpriteBatch batch){
        if (this.pos == null){
            return;
        }

        TextureRegion currentFrame;
        if(isRunning){
            currentFrame = this.runAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
        } else {
            currentFrame = this.idleAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
        }


        this.sprite = new Sprite(currentFrame);

        this.sprite.setX(this.pos.x);
        this.sprite.setY(this.pos.y);

        this.sprite.flip(this.isFlip, false);

        if (isRed){
            this.sprite.setColor(1, 0, 0, 1f);
        }

        this.sprite.draw(batch);

    }

    // get the center pos of the monster
    public Point center(){
        return new Point(this.pos.x+ this.width /2,this.pos.y+ this.height /4);
    }

    // initialise the monster textures
    public static void init(){
        monsterTextures.put("assets/entities/big_demon_idle.png", Utils.getAnimation("assets/entities/big_demon_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/big_demon_run.png", Utils.getAnimation("assets/entities/big_demon_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/big_zombie_idle.png", Utils.getAnimation("assets/entities/big_zombie_idle.png", FRAME_COLS, 5));
        monsterTextures.put("assets/entities/big_zombie_run.png", Utils.getAnimation("assets/entities/big_zombie_run.png", FRAME_COLS, 5));
        monsterTextures.put("assets/entities/chort_idle.png", Utils.getAnimation("assets/entities/chort_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/chort_run.png", Utils.getAnimation("assets/entities/chort_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/goblin_idle.png", Utils.getAnimation("assets/entities/goblin_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/goblin_run.png", Utils.getAnimation("assets/entities/goblin_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/imp_idle.png", Utils.getAnimation("assets/entities/imp_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/imp_run.png", Utils.getAnimation("assets/entities/imp_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/masked_orc_idle.png", Utils.getAnimation("assets/entities/masked_orc_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/masked_orc_run.png", Utils.getAnimation("assets/entities/masked_orc_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/muddy.png", Utils.getAnimation("assets/entities/muddy.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/ogre_idle.png", Utils.getAnimation("assets/entities/ogre_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/ogre_run.png", Utils.getAnimation("assets/entities/ogre_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/orc_warrior_idle.png", Utils.getAnimation("assets/entities/orc_warrior_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/orc_warrior_run.png", Utils.getAnimation("assets/entities/orc_warrior_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/skelet_idle.png", Utils.getAnimation("assets/entities/skelet_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/skelet_run.png", Utils.getAnimation("assets/entities/skelet_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/swampy.png", Utils.getAnimation("assets/entities/swampy.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/tiny_zombie_idle.png", Utils.getAnimation("assets/entities/tiny_zombie_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/tiny_zombie_run.png", Utils.getAnimation("assets/entities/tiny_zombie_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/wogol_idle.png", Utils.getAnimation("assets/entities/wogol_idle.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/entities/wogol_run.png", Utils.getAnimation("assets/entities/wogol_run.png", FRAME_COLS, FRAME_ROWS));
        monsterTextures.put("assets/effects/blood.png", Utils.getAnimation("assets/effects/blood.png", 6, 4, 0.02f));

    }
}
