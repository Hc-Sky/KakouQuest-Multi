package fr.studiokakou.kakouquest.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Le type Hud. Cette classe est utilisée pour créer un objet Hud.
 *
 * @version 1.0
 *
 */
public class Hud {
    /**
     * The Player.
     */
    Player player;

    /**
     * The Health bar.
     */
    ArrayList<Texture> healthBar = new ArrayList<>();
    /**
     * The Health bar outside.
     */
    Texture healthBarOutside;

    /**
     * The Stamina bar.
     */
    ArrayList<Texture> staminaBar = new ArrayList<>();

    /**
     * The Hud size.
     */
    float hudSize;

    BitmapFont font;

    /**
     * The SnapeRenderer
     */
    private ShapeRenderer shapeRenderer;

    /**
     * Constructeur de l'HUD.
     * Sert à créer un objet Hud.
     *
     * @param hudSizeMult  the hud size mult
     */
    public Hud(float hudSizeMult){
        this.player = OnlineGameScreen.player;

        shapeRenderer = new ShapeRenderer();
        this.hudSize = hudSizeMult;

        //health bar textures
        this.healthBarOutside = new Texture("assets/hud/health/outside.png");
        for (int i = 6; i >= 1; i--) {
            this.healthBar.add(new Texture("assets/hud/health/"+i+".png"));
        }

        for (int i = 6; i >= 1; i--) {
            this.staminaBar.add(new Texture("assets/hud/stamina/"+i+".png"));
        }
    }

    /**
     * Dessine l'HUD.
     *
     * @param batch the batch
     */
    public void draw(SpriteBatch batch){
        if (this.player == null) {
            return;
        }

        if (this.player.isDead || !this.player.hasPlayerSpawn) {
            return;
        }

        int healthAmount = getHealthAmount();

        Point healthBarPos = new Point(100, Gdx.graphics.getHeight()-100);
        Point staminaBarPos = new Point(78, Gdx.graphics.getHeight()-120);


        if (healthAmount>=0){
            batch.draw(this.healthBar.get(healthAmount), healthBarPos.x, healthBarPos.y, this.healthBar.get(0).getWidth()*this.hudSize, this.healthBar.get(0).getHeight()*this.hudSize);
        }
        batch.draw(this.healthBarOutside, healthBarPos.x, healthBarPos.y, this.healthBarOutside.getWidth()*this.hudSize, this.healthBarOutside.getHeight()*this.hudSize);

        batch.draw(this.staminaBar.get(getStaminaAmount()), staminaBarPos.x, staminaBarPos.y, this.staminaBar.get(0).getWidth()*this.hudSize, this.staminaBar.get(0).getHeight()*this.hudSize);



        // texte du niveau actuel
        font.draw(batch, "Level : " + player.playerLevel, 100, 90);


    }

    public void drawXpBar(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(100, 50, 300, 15);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(100F, 50F, (float) (300 * (player.experience / player.experienceToNextLevel)), 15);
        shapeRenderer.end();
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    private int getStaminaAmount() {
        int staminaAmount;
        if ((this.player.stamina*100)/this.player.max_stamina >= 98) {
            staminaAmount=5;
        } else if ((this.player.stamina*100)/this.player.max_stamina >= 70) {
            staminaAmount=4;
        } else if ((this.player.stamina*100)/this.player.max_stamina >= 50) {
            staminaAmount=3;
        }else if ((this.player.stamina*100)/this.player.max_stamina >= 30) {
            staminaAmount=2;
        }else if ((this.player.stamina*100)/this.player.max_stamina > 4) {
            staminaAmount=1;
        }else {
            staminaAmount=0;
        }
        return staminaAmount;
    }

    private int getHealthAmount() {
        int healthAmount;
        if ((this.player.hp*100)/this.player.max_hp >= 98) {
            healthAmount=5;
        } else if ((this.player.hp*100)/this.player.max_hp >= 83) {
            healthAmount=4;
        } else if ((this.player.hp*100)/this.player.max_hp >= 66) {
            healthAmount=3;
        }else if ((this.player.hp*100)/this.player.max_hp >= 49) {
            healthAmount=2;
        }else if ((this.player.hp*100)/this.player.max_hp >= 32) {
            healthAmount=1;
        }else if ((this.player.hp*100)/this.player.max_hp > 15){
            healthAmount=0;
        } else {
            healthAmount=-1;
        }
        return healthAmount;
    }
}
