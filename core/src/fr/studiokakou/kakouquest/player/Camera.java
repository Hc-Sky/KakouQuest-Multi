package fr.studiokakou.kakouquest.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import fr.studiokakou.kakouquest.network.GameClient;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;


/**
 * Represents a camera object used for rendering the game view.
 */
public class Camera {
    public static OrthographicCamera camera;

    Player player;

    // Camera constants
    public static float DEFAULT_ZOOM = (float) (Gdx.graphics.getHeight() * 2.5) / 720;
    public static float CAM_Y_DISTANCE = 53;
    public static float CAM_X_DISTANCE = 95;
    public float zoom;

    /**
     * Constructs a new Camera object.
     *
     * @param player The player object to associate with the camera.
     */
    public Camera(Player player){
        this.player = player;

        // Always maintain the same zoom regardless of screen size
        this.zoom = Camera.DEFAULT_ZOOM;

        // Initialize camera
        Camera.camera = new OrthographicCamera(Gdx.graphics.getWidth() / this.zoom, Gdx.graphics.getHeight() / this.zoom);
        Camera.camera.position.x = this.player.center().x;
        Camera.camera.position.y = this.player.center().y;
    }

    /**
     * Centers the camera on the player.
     */
    public void centerPlayer(){
        Camera.camera.position.x = this.player.center().x;
        Camera.camera.position.y = this.player.center().y;
    }

    /**
     * Updates the camera position based on the player's movement.
     */
    public void update(){

        if (!player.hasPlayerSpawn){  // If the player has not spawned yet, center the camera on the first player that has spawned (spectator)
            for (OnlinePlayer onlinePlayer : OnlineGameScreen.onlinePlayers){
                if (onlinePlayer.hasPlayerSpawn){
                    Camera.camera.position.x = onlinePlayer.center().x;
                    Camera.camera.position.y = onlinePlayer.center().y;
                    break;
                }
            }
        } else {   // If the player has spawned, center the camera on the player
            if (Camera.camera.position.x + Camera.CAM_X_DISTANCE < this.player.center().x){
                Camera.camera.position.x = this.player.center().x - Camera.CAM_X_DISTANCE;
            }
            if (Camera.camera.position.x - Camera.CAM_X_DISTANCE > this.player.center().x){
                Camera.camera.position.x = this.player.center().x + Camera.CAM_X_DISTANCE;
            }
            if (Camera.camera.position.y + Camera.CAM_Y_DISTANCE < this.player.center().y){
                Camera.camera.position.y = this.player.center().y - Camera.CAM_Y_DISTANCE;
            }
            if (Camera.camera.position.y - Camera.CAM_Y_DISTANCE > this.player.center().y){
                Camera.camera.position.y = this.player.center().y + Camera.CAM_Y_DISTANCE;
            }
        }


        Camera.camera.update();
    }
}
