package fr.studiokakou.kakouquest.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;

import java.util.ArrayList;

/**
 * This class represents the HUD (Heads-Up Display) of the game.
 * It is responsible for displaying the player's health, stamina, and level.
 */
public class Hud {
    Player player;
    ArrayList<Texture> healthBar = new ArrayList<>();
    Texture healthBarOutside;
    ArrayList<Texture> staminaBar = new ArrayList<>();
    float hudSize;

    BitmapFont font;

    private ShapeRenderer shapeRenderer;

    /**
     * Constructor for the Hud class.
     * Initializes the player, shapeRenderer, hudSize, and textures for the health and stamina bars.
     * @param hudSizeMult Multiplier for the size of the HUD.
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
     * Draws the HUD on the screen.
     * @param batch The SpriteBatch used for drawing.
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

    /**
     * Draws the XP bar on the screen.
     * In LibGdx the ShapeRenderer is used appart from the batch because it causes conflicts with the font utilisation.
     * @param shapeRenderer The ShapeRenderer used for drawing.
     */
    public void drawXpBar(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(100, 50, 300, 15);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(100F, 50F, (float) (300 * (player.experience / player.experienceToNextLevel)), 15);
        shapeRenderer.end();
    }

    /**
     * Sets the font for the HUD.
     * @param font The BitmapFont to be used.
     */
    public void setFont(BitmapFont font) {
        this.font = font;
    }

    /**
     * Calculates the amount of stamina to be displayed on the HUD.
     * @return The amount of stamina to be displayed.
     */
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

    /**
     * Calculates the amount of health to be displayed on the HUD.
     * @return The amount of health to be displayed.
     */
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