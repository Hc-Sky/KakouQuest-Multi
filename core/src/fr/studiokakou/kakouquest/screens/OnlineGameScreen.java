package fr.studiokakou.kakouquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import fr.studiokakou.kakouquest.GameSpace;
import fr.studiokakou.kakouquest.entity.Monster;
import fr.studiokakou.kakouquest.hud.Hud;
import fr.studiokakou.kakouquest.interactive.Chest;
import fr.studiokakou.kakouquest.interactive.Interactive;
import fr.studiokakou.kakouquest.map.Map;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.network.GameClient;
import fr.studiokakou.kakouquest.player.Camera;
import fr.studiokakou.kakouquest.player.OnlinePlayer;
import fr.studiokakou.kakouquest.player.OnlinePlayerConstants;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.upgradeCard.UpgradeCardScreen;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.StaticsMeleeWeapon;

import java.util.ArrayList;
import java.util.Scanner;

public class OnlineGameScreen implements Screen {

    //network infos
    public static GameClient gameClient;
    public String ipAdress = "localhost";
    public int port = 8215;
    public int udp = 8216;

    //main player info
    public static String username = "guest";
    public Camera cam;

    //écran original utilisé pour dessiner
    GameSpace game;

    //map
    public static Map map;

    //batch permettant de dessiner sur l'écran
    SpriteBatch batch;
    SpriteBatch hudBatch;
    SpriteBatch upgradeBatch;

    //hud
    Hud hud;
    BitmapFont font;

    //players
    public static Player player;
    public static ArrayList<OnlinePlayer> onlinePlayers = new ArrayList<>();

    // Temps écoulé depuis le début du jeu pour les animations.
    public static float stateTime=0f;

    public OnlineGameScreen(GameSpace game){
        username = UsernameSreen.username;

        ipAdress = Utils.getIpAddress();


        OnlinePlayerConstants.animationInit();

        this.game=game;
        this.batch = game.batch;
        this.hudBatch = game.hudBatch;
        this.upgradeBatch = game.upgradeBatch;
        StaticsMeleeWeapon.createTextureDictionary();

        map = new Map(1, 1);
        Monster.init();

        // Initialisation du joueur
        this.player = new Player(new Point(100, 100));
        this.cam = new Camera(this.player);


        gameClient = new GameClient(ipAdress, port, udp, player);

        gameClient.startClient();


    }

    @Override
    public void show() {
        OnlineGameScreen.stateTime=0f;
        Pixmap pm = new Pixmap(Gdx.files.internal("assets/cursor/melee_attack.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, pm.getWidth()/2, pm.getHeight()/2));
        pm.dispose();

        Interactive.init();
        Chest.init();

        this.hud = new Hud(this.cam.zoom);

        font = new BitmapFont();
        hud.setFont(font);

        UpgradeCardScreen.initUpgradeCards();


        gameClient.client.sendTCP("getMap");

        while (!player.hasPlayerSpawn){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        cam.centerPlayer();
    }

    @Override
    public void render(float delta) {
        OnlineGameScreen.stateTime += delta;

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        Gdx.gl.glClearColor(34/255f, 34/255f, 34/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (player.hasPlayerSpawn){
            if (!UpgradeCardScreen.isUpgrading){
                player.getKeyboardMove(map);
                player.getOrientation();
            }

            player.dash(map);
        }

        map.refreshInteract();

        if (!player.isDead){
            gameClient.sendPlayer(player);
        }

        cam.update();

        batch.setProjectionMatrix(Camera.camera.combined);

        batch.begin();

        map.drawMap(batch);

        OnlinePlayerConstants.drawOnlinePlayers(batch);

        player.draw(this.batch);

        batch.end();

        player.checkUpgrade();

        if (!UpgradeCardScreen.isUpgrading){
            hudBatch.begin();
            hud.draw(hudBatch);
            hudBatch.end();

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            this.hud.drawXpBar(shapeRenderer);
        }

        if (UpgradeCardScreen.isUpgrading){
            upgradeBatch.begin();
            UpgradeCardScreen.draw(upgradeBatch, player);
            upgradeBatch.end();
        }


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
