package fr.studiokakou.kakouquest.interactive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
 * La classe Stairs représente un objet qui permet de changer de niveau dans le jeu.
 * Lorsque le joueur interagit avec les escaliers, le niveau actuel est augmenté et une nouvelle carte est générée,
 * tout en restaurant le joueur à l'endroit approprié.
 */
public class Stairs extends Interactive {
    /**
     * Constructeur de la classe Stairs.
     * @param pos Position des escaliers sur la carte.
     */
    public Stairs(Point pos){
        super(pos, () -> {
            OnlineGameScreen.gameClient.client.sendTCP("stairs");
        });
    }

    /**
     * Dessine les escaliers sur l'écran de jeu.
     * @param batch Batch pour dessiner les textures.
     */
    public void draw(Texture texture, SpriteBatch batch){
        super.draw(batch, texture.getHeight());

        batch.draw(texture, this.pos.x, this.pos.y);
    }
}
