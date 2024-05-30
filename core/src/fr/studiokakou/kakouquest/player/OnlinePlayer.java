package fr.studiokakou.kakouquest.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import fr.studiokakou.kakouquest.constants.Constants;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.screens.OnlineGameScreen;
import fr.studiokakou.kakouquest.weapon.OnlineMeleeWeapon;
import fr.studiokakou.kakouquest.weapon.StaticsMeleeWeapon;

import java.time.LocalDateTime;

public class OnlinePlayer {
    public String username;

    public Point pos; // The current position of the player
    Point lastPos; // The last position of the player


    // Player stats
    public int hp; // The health points of the player
    public int max_hp; // The maximum health points of the player
    public int strength; // The strength of the player
    public float speed; // The speed of the player
    public float stamina; // The stamina of the player
    public int max_stamina; // The maximum stamina of the player

    //attach infos
    LocalDateTime staminaTimer;
    LocalDateTime attackTimer;
    public boolean isAttacking=false;
    public boolean canAttack = true;
    Point attackDirection;
    Point attackPos;
    float attackEndRotation;
    float attackRotation;

    //dash infos
    boolean isDashing = false;
    boolean canDash = true;
    Point dashFinalPoint;
    Point dashStartPoint;
    Point dashOrientation;
    LocalDateTime dashTimer;
    float dashStateTime;

    //player weapon
    public OnlineMeleeWeapon currentWeapon;

    //player texture infos
    public int texture_height;
    public int texture_width;
    boolean flip = false;
    boolean isRunning = false;

    // si le joueur est en train de spawn
    public boolean isPlayerSpawning = false;
    // si le joueur a spawn
    public boolean hasPlayerSpawn = false;

    float bloodStateTime;

    public OnlinePlayer() {}

    public OnlinePlayer(Player player){
        this.username = OnlineGameScreen.username;

        this.pos = player.pos;
        this.lastPos = player.lastPos;

        this.currentWeapon = StaticsMeleeWeapon.meleeWeaponToOnline(player.currentWeapon);

        this.hp=player.hp;
        this.max_hp= player.max_hp;
        this.strength = player.strength;
        this.stamina=player.stamina;
        this.max_stamina=player.max_stamina;
        this.speed = player.speed;

        this.staminaTimer=player.staminaTimer;
        this.attackTimer=player.attackTimer;
        this.isAttacking=player.isAttacking;
        this.canAttack=player.canAttack;
        this.attackDirection=player.attackDirection;
        this.attackPos=player.attackPos;
        this.attackEndRotation=player.attackEndRotation;
        this.attackRotation=player.attackRotation;

        this.isDashing=player.isDashing;
        this.canDash=player.canDash;
        this.dashFinalPoint=player.dashFinalPoint;
        this.dashStartPoint=player.dashStartPoint;
        this.dashOrientation=player.dashOrientation;
        this.dashTimer=player.dashTimer;
        this.dashStateTime=player.dashStateTime;

        this.texture_height=player.texture_height;
        this.texture_width=player.texture_width;
        this.flip=player.flip;
        this.isRunning=player.isRunning;

        this.isPlayerSpawning=player.isPlayerSpawning;
        this.hasPlayerSpawn=player.hasPlayerSpawn;

        this.bloodStateTime = player.bloodStateTime;

    }

    public void takeDamage(int damage){
        this.hp -= damage;
        this.bloodStateTime = 0f;
    }

    public void addMeleeWeapon(OnlineMeleeWeapon meleeWeapon){
        this.currentWeapon = meleeWeapon;
    }

    public Point center(){
        return new Point(this.pos.x+((float) this.texture_width /2), this.pos.y+((float) this.texture_height /4));
    }

    public Point textureCenter(){
        return new Point(this.pos.x+((float) this.texture_width /2), this.pos.y+((float) this.texture_height /2));
    }

    public void showAttack(SpriteBatch batch){
        if (this.currentWeapon == null){
            return;
        }

        Sprite weaponSprite = new Sprite(StaticsMeleeWeapon.textureDictionary.get(this.currentWeapon.texturePath));

        weaponSprite.setScale(this.currentWeapon.size);
        weaponSprite.setOrigin(weaponSprite.getWidth()/2, 0);
        weaponSprite.flip(true, false);

        this.attackPos = Point.getPosWithAngle(this.center(), Player.PLAYER_MELEE_WEAPON_DISTANCE, this.attackRotation);

        weaponSprite.setPosition(this.attackPos.x-weaponSprite.getWidth()/2, this.attackPos.y);
        weaponSprite.setRotation(this.attackRotation-90f);

        weaponSprite.draw(batch);
    }


    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame;
        if (this.isRunning) {
            if (!flip && this.lastPos.x > this.pos.x) {
                currentFrame = OnlinePlayerConstants.runAnimationRevert.getKeyFrame(OnlineGameScreen.stateTime, true);
            } else if (flip && this.lastPos.x < this.pos.x) {
                currentFrame = OnlinePlayerConstants.runAnimationRevert.getKeyFrame(OnlineGameScreen.stateTime, true);
            } else {
                currentFrame = OnlinePlayerConstants.runAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
            }
        } else {
            currentFrame = OnlinePlayerConstants.idleAnimation.getKeyFrame(OnlineGameScreen.stateTime, true);
        }

        //dash animation
        if (!this.canDash && this.dashOrientation != null && !OnlinePlayerConstants.dashAnimation.isAnimationFinished(this.dashStateTime)) {
            this.dashStateTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentDashFrame = OnlinePlayerConstants.dashAnimation.getKeyFrame(this.dashStateTime, false);
            batch.draw(currentDashFrame, this.dashOrientation.x >= 0 ? this.dashStartPoint.x - (float) currentDashFrame.getRegionWidth() / 4 : this.dashStartPoint.x + (float) currentDashFrame.getRegionWidth() / 2, this.dashStartPoint.y, this.dashOrientation.x >= 0 ? (float) currentDashFrame.getRegionWidth() / 2 : (float) -currentDashFrame.getRegionWidth() / 2, (float) currentDashFrame.getRegionHeight() / 2);
        }

        batch.draw(currentFrame, flip ? this.pos.x + this.texture_width : this.pos.x, this.pos.y, this.flip ? -this.texture_width : this.texture_width, this.texture_height);

        //blood animation
        if (bloodStateTime >= 0) {
            bloodStateTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentBloodFrame = OnlinePlayerConstants.bloodEffect.getKeyFrame(bloodStateTime, false);
            batch.draw(currentBloodFrame,
                    this.pos.x - (float) currentBloodFrame.getRegionWidth() / 4 + (float) this.texture_width / 2,
                    this.pos.y - (float) currentBloodFrame.getRegionHeight() / 4 + (float) this.texture_height / 2,
                    (float) currentBloodFrame.getRegionWidth() / 2,
                    (float) currentBloodFrame.getRegionHeight() / 2
            );
        }

        if (OnlinePlayerConstants.bloodEffect.isAnimationFinished(bloodStateTime)) {
            this.bloodStateTime = -1;
        }

        if (this.isAttacking) {
            this.showAttack(batch);
        }

        //draw username
        Constants.usernameFont.getData().setScale(0.3f);
        final GlyphLayout layout = new GlyphLayout(Constants.usernameFont, this.username);

        Constants.usernameFont.draw(batch, layout, this.center().x-layout.width/2, this.pos.y+this.texture_height);
    }

}