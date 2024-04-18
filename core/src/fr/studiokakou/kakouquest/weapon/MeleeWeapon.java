package fr.studiokakou.kakouquest.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


/**
 * This class represents a MeleeWeapon in the game.
 * A MeleeWeapon has a name, texture, stats, dimensions, and a list of possible MeleeWeapons.
 */
public class MeleeWeapon {

    public String name;

    //weapon texture
    public String texturePath;
    public Texture texture;
    public Sprite sprite;

    //weapon stats
    public int damage;
    public int resistance;
    public float attackRange;
    public float attackSpeed;
    public int maxResistance;

    //weapon dimensions
    public float size;
    public float height;
    public float width;


    /**
     * Constructs a MeleeWeapon with the given parameters.
     * @param name The name of the weapon.
     * @param texturePath The path to the texture of the weapon.
     * @param damage The damage of the weapon.
     * @param resistance The resistance of the weapon.
     * @param attackRange The attack range of the weapon.
     * @param attackSpeed The attack speed of the weapon.
     * @param size The size of the weapon.
     */

    public MeleeWeapon(String name, String texturePath, int damage, int resistance, float attackRange, float attackSpeed, float size) {
        //weapon stats
        this.name = name;
        this.damage = damage;
        this.maxResistance = resistance;
        this.resistance = resistance;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
        this.size = size;

        //weapon texture
        this.texturePath = texturePath;
        this.texture = new Texture(texturePath);
        this.sprite = new Sprite(this.texture);
        this.sprite.setScale(this.size);
        this.height = this.sprite.getHeight();
        this.width = this.sprite.getWidth();
        this.sprite.setOrigin(this.width/2, 0);
        this.sprite.flip(true, false);
    }


    public MeleeWeapon(OnlineMeleeWeapon onlineMeleeWeapon) {
        if (onlineMeleeWeapon==null){
            return;
        }

        //weapon stats
        this.name = onlineMeleeWeapon.name;
        this.damage = onlineMeleeWeapon.damage;
        this.maxResistance = onlineMeleeWeapon.resistance;
        this.resistance = onlineMeleeWeapon.resistance;
        this.attackRange = onlineMeleeWeapon.attackRange;
        this.attackSpeed = onlineMeleeWeapon.attackSpeed;
        this.size = onlineMeleeWeapon.size;

        //weapon texture
        this.texturePath = onlineMeleeWeapon.texturePath;
        this.texture = new Texture(texturePath);
        this.sprite = new Sprite(this.texture);
        this.sprite.setScale(this.size);
        this.height = this.sprite.getHeight();
        this.width = this.sprite.getWidth();
        this.sprite.setOrigin(this.width/2, 0);
        this.sprite.flip(true, false);
    }

    /**
     * Returns a new MeleeWeapon with the same parameters as this MeleeWeapon.
     * @return A new MeleeWeapon.
     */
    public MeleeWeapon getNew(){
        return new MeleeWeapon(this.name, this.texturePath, damage, maxResistance, attackRange, attackSpeed, size);
    }
}

