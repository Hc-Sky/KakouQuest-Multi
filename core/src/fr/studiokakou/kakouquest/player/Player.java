package fr.studiokakou.kakouquest.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.studiokakou.kakouquest.constants.Constants;
import fr.studiokakou.kakouquest.keybinds.Keybinds;
import fr.studiokakou.kakouquest.map.Floor;
import fr.studiokakou.kakouquest.map.Map;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.MeleeWeapon;

import java.time.LocalDateTime;

public class Player {
    public Point pos; // The current position of the player
    Point lastPos; // The last position of the player


    // Player stats
    public int hp; // The health points of the player
    public int max_hp; // The maximum health points of the player
    public int strength; // The strength of the player
    public float speed; // The speed of the player
    public float stamina; // The stamina of the player
    public int max_stamina; // The maximum stamina of the player


    //l'arme actuelle
    public MeleeWeapon currentWeapon;


    //dash infos
    boolean isDashing = false;
    boolean canDash = true;
    Point dashFinalPoint;
    Point dashStartPoint;
    Point dashOrientation;
    LocalDateTime dashTimer;
    float dashStateTime;


    //attack vars
    LocalDateTime staminaTimer;
    LocalDateTime attackTimer;
    public boolean isAttacking=false;
    public boolean canAttack = true;
    Point attackDirection;
    Point attackPos;
    float attackEndRotation;
    float attackRotation;


    //player texture infos
    public int texture_height;
    public int texture_width;
    boolean flip=false;
    boolean isRunning=false;


    // si le joueur est en train de spawn
    public boolean isPlayerSpawning=false;
    // si le joueur a spawn
    public boolean hasPlayerSpawn=false;


    //animations
    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> runAnimation;
    Animation<TextureRegion> runAnimationRevert;
    Animation<TextureRegion> dashAnimation;
    Animation<TextureRegion> spawnAnimation;

    Animation<TextureRegion> bloodEffect;
    float bloodStateTime=0f;


    // le nombre de colonne de l'animation
    static final int FRAME_COLS = 1;
    // le nombre de lignes de l'animation
    static final int FRAME_ROWS = 4;

    public Player(Point spawn){
        //player animations
        this.idleAnimation = Utils.getAnimation("assets/player/knight_1_idle.png", FRAME_COLS, FRAME_ROWS);
        this.runAnimation = Utils.getAnimation("assets/player/knight_1_run.png", FRAME_COLS, FRAME_ROWS);
        this.runAnimationRevert =  Utils.getAnimationRevert("assets/player/knight_1_run.png", FRAME_COLS, FRAME_ROWS);
        this.dashAnimation = Utils.getAnimation("assets/effects/dash.png", FRAME_COLS, 5, 0.07f);
        this.spawnAnimation = Utils.getAnimation("assets/effects/player_spawn.png", 1, 16, 0.06f);
        this.bloodEffect = Utils.getAnimation("assets/effects/blood.png", 6, 4, 0.02f);

        //get player texture height and width
        this.texture_width = Utils.getAnimationWidth(this.idleAnimation);
        this.texture_height = Utils.getAnimationHeight(this.idleAnimation);

        //spawn player pos
        this.pos = new Point(spawn.x-((float) this.texture_width /2), spawn.y);
        this.lastPos = this.pos;

        //default values
        this.max_hp=100;
        this.hp=100;
        this.strength=10;
        this.speed=40f;
        this.max_stamina = 100;
        this.stamina = 100;

        //default weapon
        this.currentWeapon = MeleeWeapon.ANIME_SWORD();
    }

    /**
     * sert a déplacer le joueur à un poit précis
     * @param pos
     */
    public void setPos(Point pos){
        this.pos = pos;
    }

    public void move(float x, float y, Map map){
        if (canMove(this.pos.add(x*Gdx.graphics.getDeltaTime()*this.speed, y*Gdx.graphics.getDeltaTime()*this.speed), map)){
            this.lastPos = this.pos;
            this.pos = this.pos.add(x*Gdx.graphics.getDeltaTime()*this.speed, y*Gdx.graphics.getDeltaTime()*this.speed);
        }
    }

    public boolean canMove(Point newPos, Map map){
        return true;
    }

    public boolean canActionWithStamina(int amount){
        return this.stamina-amount >=0;
    }

    /**
     * Texture center point.
     * @return the point
     */
    public Point center(){
        return new Point(this.pos.x+((float) this.texture_width /2), this.pos.y+((float) this.texture_height /4));
    }

    /**
     * Texture center point.
     *
     * @return the point
     */
    public Point textureCenter(){
        return new Point(this.pos.x+((float) this.texture_width /2), this.pos.y+((float) this.texture_height /2));
    }


    /**
     * Permet de faire attaquer le joueur.
     */
    public void attack() {
        if (canAttack && !this.isAttacking && this.canActionWithStamina(Constants.ATTACK_STAMINA_USAGE)){
            this.isAttacking=true;
            this.canAttack = false;

            this.stamina-= Constants.ATTACK_STAMINA_USAGE;
            this.staminaTimer = LocalDateTime.now();

            this.attackDirection = Utils.mousePosUnproject(Camera.camera);
            this.attackRotation = Utils.getAngleWithPoint(this.center(), this.attackDirection)-this.currentWeapon.attackRange/2;
            this.attackEndRotation = this.attackRotation+this.currentWeapon.attackRange;
        }
    }




    /**
     *
     * Permet de récupérer l'orientation du joueur.
     */
    public void getOrientation(){
        Point mousePos = Utils.mousePosUnproject(Camera.camera);
        this.flip= mousePos.x < this.center().x;
    }


    /**
     * Permet de récupérer les mouvements du clavier.
     *
     */
    public void getKeyboardMove(Map map){
        if (!this.isDashing){
            if (Gdx.input.isKeyPressed(Keybinds.UP_KEY)){
                this.move(0, 1, map);
                this.isRunning=true;
            } else if (Gdx.input.isKeyPressed(Keybinds.DOWN_KEY)){
                this.move(0, -1, map);
                this.isRunning=true;
            } if (Gdx.input.isKeyPressed(Keybinds.LEFT_KEY)){
                this.move(-1, 0, map);
                this.isRunning=true;
            } else if (Gdx.input.isKeyPressed(Keybinds.RIGHT_KEY)){
                this.move(1, 0, map);
                this.isRunning=true;
            } if (!(Gdx.input.isKeyPressed(Keybinds.UP_KEY) || Gdx.input.isKeyPressed(Keybinds.DOWN_KEY) || Gdx.input.isKeyPressed(Keybinds.LEFT_KEY) || Gdx.input.isKeyPressed(Keybinds.RIGHT_KEY))){
                this.isRunning=false;
            } if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                this.attack();
            }
        }
    }

    /**
     * Dessine le joueur.
     *
     * @param batch the batch
     */
    public void draw(SpriteBatch batch) {

        if (hasPlayerSpawn) {
            TextureRegion currentFrame;
            if (this.isRunning) {
                if (!flip && this.lastPos.x > this.pos.x) {
                    currentFrame = this.runAnimationRevert.getKeyFrame(OnlineGameScreen.stateTime, true);
                } else if (flip && this.lastPos.x < this.pos.x) {
                    currentFrame = this.runAnimationRevert.getKeyFrame(OnlineGameScreen.stateTime, true);
                } else {
                    currentFrame = this.runAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
                }
            } else {
                currentFrame = this.idleAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
            }

            //dash animation
            if (!this.canDash && this.dashOrientation != null && !this.dashAnimation.isAnimationFinished(this.dashStateTime)) {
                this.dashStateTime += Gdx.graphics.getDeltaTime();
                TextureRegion currentDashFrame = this.dashAnimation.getKeyFrame(this.dashStateTime, false);
                batch.draw(currentDashFrame, this.dashOrientation.x >= 0 ? this.dashStartPoint.x - (float) currentDashFrame.getRegionWidth() / 4 : this.dashStartPoint.x + (float) currentDashFrame.getRegionWidth() / 2, this.dashStartPoint.y, this.dashOrientation.x >= 0 ? (float) currentDashFrame.getRegionWidth() / 2 : (float) -currentDashFrame.getRegionWidth() / 2, (float) currentDashFrame.getRegionHeight() / 2);
            }
            batch.draw(currentFrame, flip ? this.pos.x + this.texture_width : this.pos.x, this.pos.y, this.flip ? -this.texture_width : this.texture_width, this.texture_height);

            //blood animation
            if (bloodStateTime >= 0) {
                bloodStateTime += Gdx.graphics.getDeltaTime();
                TextureRegion currentBloodFrame = this.bloodEffect.getKeyFrame(bloodStateTime, false);
                batch.draw(currentBloodFrame,
                        this.pos.x - (float) currentBloodFrame.getRegionWidth() / 4 + (float) this.texture_width / 2,
                        this.pos.y - (float) currentBloodFrame.getRegionHeight() / 4 + (float) this.texture_height / 2,
                        (float) currentBloodFrame.getRegionWidth() / 2,
                        (float) currentBloodFrame.getRegionHeight() / 2
                );
            }

            if (this.bloodEffect.isAnimationFinished(bloodStateTime)) {
                this.bloodStateTime = -1;
            }

        }
    }

    /**
     * gère les dégàts fais au joueur
     * @param damage
     */
    public void takeDamage(int damage){
        this.hp -= damage;
        this.bloodStateTime=0f;
    }

}