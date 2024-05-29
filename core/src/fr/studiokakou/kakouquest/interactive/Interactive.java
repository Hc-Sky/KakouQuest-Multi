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

public class Interactive {
    /** Touche pour interagir avec les escaliers. */
    public static String interactKey;

    /** Code de la touche pour interagir avec les escaliers. */
    public static int interactKeyCode;

    /** Animation de la touche pour interagir avec les escaliers. */
    public static Animation<TextureRegion> interactKeyAnimation;

    public boolean isClosest = false;

    public boolean multipleInteract=false;

    public boolean canInteract;

    public boolean hasInteracted = false;

    public Point pos;

    private Interactable interactable;

    Object object;

    public Interactive(Point pos, Interactable interactable){
        this.pos = pos;
        this.canInteract=false;
        this.interactable = interactable;
    }

    public Interactive(Point pos, Interactable interactable, boolean multipleInteract){
        this.pos = pos;
        this.canInteract=false;
        this.interactable = interactable;
        this.multipleInteract = multipleInteract;
    }

    public Interactive(Point pos, Interactable interactable, boolean multipleInteract, boolean hasInteracted){
        this.pos = pos;
        this.canInteract=false;
        this.interactable = interactable;
        this.multipleInteract = multipleInteract;
        this.hasInteracted = hasInteracted;
    }

    public void giveObject(Object object){
        this.object = object;
    }

    public static void init(){
        interactKeyCode = GetProperties.getIntProperty("KEY_INTERRACT");
        interactKey = Input.Keys.toString(interactKeyCode);

        interactKeyAnimation = Utils.getAnimationHorizontal("assets/keys/animated/"+interactKey+".png", 2, 1, 1f);
    }

    public float getDistance(Player payer){
        return Utils.getDistance(this.pos, payer.pos);
    }

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

        if (Utils.getDistance(this.pos, player.pos) <= 40 && isClosest && (this.multipleInteract || !this.hasInteracted)){
            this.canInteract = true;
        } else {
            this.canInteract = false;
        }
    }

    public void draw(SpriteBatch batch, int textureHeight) {
        if (canInteract){
            TextureRegion currentKeyFrame = this.interactKeyAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
            batch.draw(currentKeyFrame, this.pos.x, this.pos.y+textureHeight+3, Floor.TEXTURE_WIDTH, Floor.TEXTURE_HEIGHT);
        }
    }
}
