package fr.studiokakou.kakouquest.interactive;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.studiokakou.kakouquest.map.Map;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.network.GameClient;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.MeleeWeapon;

public class Chest extends Interactive{

    public MeleeWeapon meleeWeaponLoot;

    boolean isOpened=false;

    /**
     * The Closed.
     */
    public static TextureRegion closed;
    /**
     * The Opened.
     */
    public static TextureRegion opened;

    /**
     * The Openning animation.
     */
    public static Animation<TextureRegion> openningAnimation;

    public static final int FRAME_COLS = 1;
    /**
     * The Frame rows.
     */
    public static final int FRAME_ROWS = 3;


    public Chest(Point pos, int currentLevel){
        super(pos, () -> {
        }, false, false);

        this.isOpened = false;

        super.giveObject(this);

    }

    public Chest(OnlineChest onlineChest){
        super(onlineChest.pos, () -> {}, false, onlineChest.hasInteracted);

        this.isOpened = onlineChest.isOpened;

        if (onlineChest.meleeWeaponLoot != null){
            this.meleeWeaponLoot = new MeleeWeapon(onlineChest.meleeWeaponLoot.getNew());
        } else {
            this.meleeWeaponLoot = null;
        }

        super.giveObject(this);
    }

    public static void init(){
        openningAnimation = Utils.getAnimation("assets/map/chest_open.png", FRAME_COLS, FRAME_ROWS);
        closed = openningAnimation.getKeyFrames()[0];
        opened = openningAnimation.getKeyFrames()[2];
    }

    public void open(){
        if (!isOpened){
            this.isOpened = true;
            if (this.meleeWeaponLoot != null){
                OnlineGameScreen.player.currentWeapon = this.meleeWeaponLoot;
                this.meleeWeaponLoot = null;
            }
        }
    }

    public void draw(SpriteBatch batch){
        super.draw(batch, closed.getRegionHeight());
        if (! isOpened){
            batch.draw(closed, this.pos.x, this.pos.y);
        } else {
            batch.draw(opened, this.pos.x, this.pos.y);
        }
    }

}
