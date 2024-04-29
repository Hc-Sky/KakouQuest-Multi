package fr.studiokakou.kakouquest.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.utils.Utils;

import java.util.ArrayList;

public class OnlinePlayerConstants {
    // le nombre de colonne de l'animation
    static final int FRAME_COLS = 1;
    // le nombre de lignes de l'animation
    static final int FRAME_ROWS = 4;

    public static Animation<TextureRegion> idleAnimation;
    public static Animation<TextureRegion> runAnimation;
    public static Animation<TextureRegion> runAnimationRevert;
    public static Animation<TextureRegion> dashAnimation;
    public static Animation<TextureRegion> spawnAnimation;
    public static Animation<TextureRegion> bloodEffect;

    public static void drawOnlinePlayers(SpriteBatch batch){
        for (OnlinePlayer onlinePlayer : OnlineGameScreen.onlinePlayers){
            onlinePlayer.draw(batch);
        }
    }

    public static void animationInit(){
        idleAnimation = Utils.getAnimation("assets/player/knight_1_idle.png", FRAME_COLS, FRAME_ROWS);
        runAnimation = Utils.getAnimation("assets/player/knight_1_run.png", FRAME_COLS, FRAME_ROWS);
        runAnimationRevert =  Utils.getAnimationRevert("assets/player/knight_1_run.png", FRAME_COLS, FRAME_ROWS);
        dashAnimation = Utils.getAnimation("assets/effects/dash.png", FRAME_COLS, 5, 0.07f);
        spawnAnimation = Utils.getAnimation("assets/effects/player_spawn.png", 1, 16, 0.06f);
        bloodEffect = Utils.getAnimation("assets/effects/blood.png", 6, 4, 0.02f);
    }

    public static OnlinePlayer mainToOnlinePlayer(Player player){
        return new OnlinePlayer(player);
    }
}
