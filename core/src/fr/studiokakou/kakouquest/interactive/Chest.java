package fr.studiokakou.kakouquest.interactive;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.MeleeWeapon;

/**
 * This class represents a Chest in the game.
 * A Chest is an interactive object that can contain a MeleeWeapon as loot.
 */
public class Chest extends Interactive{

    public MeleeWeapon meleeWeaponLoot;

    boolean isOpened=false;

    public static TextureRegion closed;
    public static TextureRegion opened;

    public static Animation<TextureRegion> openningAnimation;

    public static final int FRAME_COLS = 1;
    public static final int FRAME_ROWS = 3;

    /**
     * Constructor for the Chest class.
     * Initializes the position, currentLevel, and sets isOpened to false.
     * @param pos The position of the Chest.
     * @param currentLevel The current level of the game.
     */
    public Chest(Point pos, int currentLevel){
        super(pos, () -> {
        }, false, false);

        this.isOpened = false;

        super.giveObject(this);

    }

    /**
     * Constructor for the Chest class.
     * Initializes the Chest based on an OnlineChest object.
     * @param onlineChest The OnlineChest object to base this Chest on.
     */
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

    /**
     * Initializes the animations and textures for the Chest.
     * This method should be called before creating any Chest objects.
     */
    public static void init(){
        openningAnimation = Utils.getAnimation("assets/map/chest_open.png", FRAME_COLS, FRAME_ROWS);
        closed = openningAnimation.getKeyFrames()[0];
        opened = openningAnimation.getKeyFrames()[2];
    }

    /**
     * Opens the Chest and gives the loot to the player.
     */
    public void open(){
        if (!isOpened){
            this.isOpened = true;
            if (this.meleeWeaponLoot != null){
                OnlineGameScreen.player.currentWeapon = this.meleeWeaponLoot;
                this.meleeWeaponLoot = null;
            }
        }
    }

    /**
     * Draws the Chest on the screen.
     * @param batch The SpriteBatch used for drawing.
     */
    public void draw(SpriteBatch batch){
        super.draw(batch, closed.getRegionHeight());
        if (! isOpened){
            batch.draw(closed, this.pos.x, this.pos.y);
        } else {
            batch.draw(opened, this.pos.x, this.pos.y);
        }
    }

}