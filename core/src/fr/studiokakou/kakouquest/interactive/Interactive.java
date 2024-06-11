package fr.studiokakou.kakouquest.interactive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.studiokakou.kakouquest.GetProperties;
import fr.studiokakou.kakouquest.map.Floor;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.utils.Utils;

/**
 * This class represents an interactive object in the game.
 * An interactive object can be interacted with by a player.
 * It is used as a base class for objects that can be interacted with like chests.
 */
public class Interactive {
    /** Key for interacting with stairs. */
    public static String interactKey;

    /** Key code for interacting with stairs. */
    public static int interactKeyCode;

    /** Animation of the key for interacting with stairs. */
    public static Animation<TextureRegion> interactKeyAnimation;

    public boolean isClosest = false;

    public boolean multipleInteract=false;

    public boolean canInteract;

    public boolean hasInteracted = false;

    public Point pos;

    private Interactable interactable;

    Object object;

    /**
     * Constructor for the Interactive class.
     * Initializes the position and interactable function.
     * @param pos The position of the Interactive object.
     * @param interactable The interactable object.
     */
    public Interactive(Point pos, Interactable interactable){
        this.pos = pos;
        this.canInteract=false;
        this.interactable = interactable;
    }

    /**
     * Constructor for the Interactive class.
     * Initializes the position, interactable object, and multipleInteract.
     * If multipleInteract is true, the object can be interacted with multiple times.
     * @param pos The position of the Interactive object.
     * @param interactable The interactable object.
     * @param multipleInteract Whether the object can be interacted with multiple times.
     */
    public Interactive(Point pos, Interactable interactable, boolean multipleInteract){
        this.pos = pos;
        this.canInteract=false;
        this.interactable = interactable;
        this.multipleInteract = multipleInteract;
    }

    /**
     * Constructor for the Interactive class.
     * Initializes the position, interactable object, multipleInteract, and hasInteracted.
     * @param pos The position of the Interactive object.
     * @param interactable The interactable object.
     * @param multipleInteract Whether the object can be interacted with multiple times.
     * @param hasInteracted Whether the object has been interacted with.
     */
    public Interactive(Point pos, Interactable interactable, boolean multipleInteract, boolean hasInteracted){
        this.pos = pos;
        this.canInteract=false;
        this.interactable = interactable;
        this.multipleInteract = multipleInteract;
        this.hasInteracted = hasInteracted;
    }

    /**
     * Gives an object to the Interactive object.
     * @param object The object to be given.
     */
    public void giveObject(Object object){
        this.object = object;
    }

    /**
     * Initializes the interact key and animation.
     */
    public static void init(){
        interactKeyCode = GetProperties.getIntProperty("KEY_INTERRACT");
        interactKey = Input.Keys.toString(interactKeyCode);

        interactKeyAnimation = Utils.getAnimationHorizontal("assets/keys/animated/"+interactKey+".png", 2, 1, 1f);
    }

    /**
     * Gets the distance between the Interactive object and the player.
     * @param payer The player.
     * @return The distance between the Interactive object and the player.
     */
    public float getDistance(Player payer){
        return Utils.getDistance(this.pos, payer.pos);
    }

    /**
     * Refreshes the interact state of the Interactive object.
     * @param player The player.
     * @param isClosest Whether the Interactive object is the closest to the player.
     */
    public void refreshInteract(Player player, boolean isClosest){
        if (canInteract && Gdx.input.isKeyJustPressed(interactKeyCode) && (this.multipleInteract || !this.hasInteracted)){
            if (object != null){
                if (object instanceof Chest){
                    ((Chest) object).open();
                }
            }
            this.interactable.interact();
            if (!multipleInteract){
                this.hasInteracted = true;
                this.canInteract=false;
            }
        }

        // checks if the player can interact with the object it will show the interact key animation
        if (Utils.getDistance(this.pos, player.pos) <= 40 && isClosest && (this.multipleInteract || !this.hasInteracted)){
            this.canInteract = true;
        } else {
            this.canInteract = false;
        }
    }

    /**
     * Draws the Interactive object on the screen.
     * @param batch The SpriteBatch used for drawing.
     * @param textureHeight The height of the texture.
     */
    public void draw(SpriteBatch batch, int textureHeight) {
        if (canInteract){
            TextureRegion currentKeyFrame = this.interactKeyAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
            batch.draw(currentKeyFrame, this.pos.x, this.pos.y+textureHeight+3, Floor.TEXTURE_WIDTH, Floor.TEXTURE_HEIGHT);
        }
    }
}