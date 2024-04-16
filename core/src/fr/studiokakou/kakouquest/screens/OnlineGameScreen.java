package fr.studiokakou.kakouquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryonet.Connection;
import fr.studiokakou.kakouquest.GameSpace;
import fr.studiokakou.kakouquest.map.Map;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.Camera;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.OnlinePlayerConstants;
import fr.studiokakou.kakouquest.player.Player;

import java.util.ArrayList;

public class OnlineGameScreen implements Screen {

    //network infos
    public Connection connection;
    public int id;

    //main player info
    public static String username = "swann";
    public Camera cam;

    //écran original utilisé pour dessiner
    GameSpace game;

    //map
    public Map map;

    //batch permettant de dessiner sur l'écran
    SpriteBatch batch;
    SpriteBatch hudBatch;

    //players
    public Player player;
    public ArrayList<OnlinePlayer> onlinePlayers = new ArrayList<>();

    // Temps écoulé depuis le début du jeu pour les animations.
    public static float stateTime=0f;

    public OnlineGameScreen(GameSpace game){
        this.game=game;
        this.batch = game.batch;
        this.hudBatch = game.hudBatch;

        this.map = new Map(1, 1);

        // Initialisation du joueur
        this.player = new Player(new Point(100, 100));
        this.cam = new Camera(this.player);

    }

    @Override
    public void show() {
        OnlineGameScreen.stateTime=0f;
        Pixmap pm = new Pixmap(Gdx.files.internal("assets/cursor/melee_attack.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, pm.getWidth()/2, pm.getHeight()/2));
        pm.dispose();

        onlinePlayers.add(OnlinePlayerConstants.mainToOnlinePlayer(new Player(new Point(110, 110))));

        onlinePlayers.get(0).hasPlayerSpawn=true;

        player.hasPlayerSpawn=true;
    }

    @Override
    public void render(float delta) {

        OnlineGameScreen.stateTime += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        Gdx.gl.glClearColor(34/255f, 34/255f, 34/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.getKeyboardMove(this.map);
        player.getOrientation();

        cam.update();

        batch.setProjectionMatrix(Camera.camera.combined);

        batch.begin();

        OnlinePlayerConstants.drawOnlinePlayers(onlinePlayers, batch);

        player.draw(this.batch);

        batch.end();


    }

    @Override
    public void resize(int width, int height) {
        this.batch.getProjectionMatrix().setToOrtho2D(0, 0, width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.game.dispose();
    }
}
