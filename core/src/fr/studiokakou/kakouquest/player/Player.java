package fr.studiokakou.kakouquest.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import fr.studiokakou.kakouquest.constants.Constants;
import fr.studiokakou.kakouquest.entity.Monster;
import fr.studiokakou.kakouquest.keybinds.Keybinds;
import fr.studiokakou.kakouquest.map.Floor;
import fr.studiokakou.kakouquest.map.Map;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.MeleeWeapon;
import fr.studiokakou.kakouquest.weapon.OnlineMeleeWeapon;
import fr.studiokakou.kakouquest.weapon.StaticsMeleeWeapon;

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
    public boolean isDead;


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

    //dash stats
    static float DASH_DISTANCE = 50f;
    /**
     * la vitesse du dash.
     */
    static float DASH_SPEED = 500f;
    /**
     * la pause entre les dashs.
     */
    static long DASH_PAUSE = 3;   //en secondes
    /**
     * la stamina utilisée pour dash.
     */
    static int DASH_STAMINA_USAGE = 10;


    //attack vars
    public static float PLAYER_MELEE_WEAPON_DISTANCE=10f;
    /**
     * la pause entre les attaques.
     */
    public static float ATTACK_PAUSE = 200f; //en millisecondes
    /**
     * la stamina utilisée pour attaquer.
     */
    static int ATTACK_STAMINA_USAGE = 2;
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
        this.currentWeapon = StaticsMeleeWeapon.onlineToMeleeWeapon(StaticsMeleeWeapon.RUSTY_SWORD());
        this.isDead=false;
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

    public void dash(Map map){    //used for the dash animation
        if (this.isDashing){
            if (this.dashFinalPoint == null && this.dashOrientation==null){
                Point mousePos = Utils.mousePosUnproject(Camera.camera);
                this.dashFinalPoint = Utils.getPointDirection(this.pos, mousePos, Player.DASH_DISTANCE);
                this.dashStartPoint = this.pos;
                this.dashOrientation = Point.getOrientation(this.pos, this.dashFinalPoint);
                this.dashTimer = LocalDateTime.now();
                this.dashStateTime = 0f;
            }else {
                if (this.dashTimer.plusSeconds(1).isBefore(LocalDateTime.now())) {
                    this.isDashing=false;
                }

                if (this.dashOrientation==null){
                    this.isDashing = false;
                    this.dashFinalPoint=null;
                    this.dashStartPoint=null;
                } else if (!Point.isPointExceeded(this.pos, this.dashFinalPoint, this.dashOrientation)){
                    assert this.dashFinalPoint != null;
                    Point nextPos = Utils.getPointDirection(this.pos, this.dashFinalPoint, Player.DASH_SPEED*Gdx.graphics.getDeltaTime());

                    boolean canMove1 = true;

                    if (canMove(new Point(this.pos.x, nextPos.y), map)){
                        this.pos = new Point(this.pos.x, nextPos.y);
                    } else {
                        canMove1=false;
                    }
                    if (canMove(new Point(nextPos.x, this.pos.y), map)){
                        this.pos = new Point(nextPos.x, this.pos.y);
                    } else if (!canMove1) {
                        this.isDashing=false;
                    }
                } else {
                    this.isDashing=false;
                    this.dashFinalPoint=null;
                    this.dashStartPoint=null;
                    this.dashOrientation=null;
                }
            }
        } else if (!this.canDash && this.dashTimer.plusSeconds(Player.DASH_PAUSE).isBefore(LocalDateTime.now())) {
            this.dashStartPoint=null;
            this.dashOrientation=null;
            this.canDash=true;
        } else if (Gdx.input.isKeyJustPressed(Keybinds.DASH_KEY) && this.canDash && this.canActionWithStamina(10)) {
            this.canDash=false;
            this.isDashing=true;
            this.stamina-=Player.DASH_STAMINA_USAGE;
            this.staminaTimer = LocalDateTime.now();
        }

    }


    public boolean canMove(Point newPos, Map map){
        Point hitboxTopLeft = newPos.add(3, this.texture_height-5 - Floor.TEXTURE_HEIGHT);
        Point hitboxBottomLeft = newPos.add(3, 0);
        Point hitboxTopRight = newPos.add(this.texture_width-3, this.texture_height-5 - Floor.TEXTURE_HEIGHT);
        Point hitboxBottomRight = newPos.add(this.texture_width-3, 0);

        Point[] points = {hitboxTopLeft, hitboxBottomLeft, hitboxTopRight, hitboxBottomRight};

        return map.arePointsOnFloor(points);
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
        if (currentWeapon==null){
            return;
        }
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
     * Permet d'afficher l'attaque du joueur.
     *
     * @param batch the batch
     */
    public void showAttack(SpriteBatch batch){
        if (this.currentWeapon == null){
            return;
        }
        if (this.attackRotation <= this.attackEndRotation){
            this.attackPos = Point.getPosWithAngle(this.center(), Player.PLAYER_MELEE_WEAPON_DISTANCE, this.attackRotation);

            this.currentWeapon.sprite.setPosition(this.attackPos.x-this.currentWeapon.width/2, this.attackPos.y);
            this.currentWeapon.sprite.setRotation(this.attackRotation-90f);

            this.currentWeapon.sprite.draw(batch);

            this.checkHit();

            this.attackRotation += this.currentWeapon.attackSpeed*Gdx.graphics.getDeltaTime()*1000;
            this.attackPos = Point.getPosWithAngle(this.center(), Player.PLAYER_MELEE_WEAPON_DISTANCE, this.attackRotation);
        }else if (this.attackTimer==null){
            this.isAttacking = false;
            this.attackTimer = LocalDateTime.now();
        } else if (this.attackTimer.plusNanos((long) (Player.ATTACK_PAUSE*1000000)).isBefore(LocalDateTime.now())) {
            this.canAttack=true;
            this.attackTimer=null;
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

    public void checkHit(){
        Rectangle meleeWeaponRectangle = this.currentWeapon.sprite.getBoundingRectangle();
        for (Monster m : Map.monsters){
            Rectangle mRectangle = m.sprite.getBoundingRectangle();
            if (meleeWeaponRectangle.overlaps(mRectangle)){
                boolean damaged = m.hit(this);
                if (damaged){
                    this.currentWeapon.resistance-=1;
                    System.out.println(this.currentWeapon.resistance);
                    if (currentWeapon.resistance<=0 && currentWeapon.resistance>-100){
                        this.currentWeapon = new MeleeWeapon(StaticsMeleeWeapon.RUSTY_SWORD());
                    }
                }
            }
        }
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

            if (!this.canAttack) {
                this.showAttack(batch);
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

        if (this.hp <= 0){
            this.hp = this.max_hp;
            this.isDead=true;
            this.hasPlayerSpawn=false;
            this.currentWeapon = StaticsMeleeWeapon.onlineToMeleeWeapon(StaticsMeleeWeapon.RUSTY_SWORD());
            OnlineGameScreen.gameClient.client.sendTCP(OnlinePlayerConstants.mainToOnlinePlayer(this));
        }
    }

    public void changePlayerStats(OnlinePlayer onlinePlayer){

        this.pos = onlinePlayer.pos;

        // Update the player's stats based on the OnlinePlayer object
        this.hp = onlinePlayer.hp;
        this.max_hp = onlinePlayer.max_hp;
        this.strength = onlinePlayer.strength;
        this.speed = onlinePlayer.speed;
        this.stamina = onlinePlayer.stamina;
        this.max_stamina = onlinePlayer.max_stamina;

        this.isDead = onlinePlayer.isDead;
        this.hasPlayerSpawn = onlinePlayer.hasPlayerSpawn;

        if (onlinePlayer.currentWeapon != null){
            System.out.println("changing weapon");
            this.currentWeapon = StaticsMeleeWeapon.onlineToMeleeWeapon(onlinePlayer.currentWeapon);
        }
    }

}