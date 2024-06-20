package fr.studiokakou.kakouquest.weapon;

/**
 * Classe représentant une arme de mêlée en ligne.
 */
public class OnlineMeleeWeapon {

    public String name;

    //weapon texture
    public String texturePath;

    //weapon stats
    public int damage;
    public int resistance;
    public float attackRange;
    public float attackSpeed;
    public int maxResistance;

    //weapon dimensions
    public float size;

    /**
     * Constructeur de la classe OnlineMeleeWeapon.
     * @param name
     * @param texturePath
     * @param damage
     * @param resistance
     * @param attackRange
     * @param attackSpeed
     * @param size
     */
    public OnlineMeleeWeapon(String name, String texturePath, int damage, int resistance, float attackRange, float attackSpeed, float size){
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
    }

    public OnlineMeleeWeapon(MeleeWeapon weapon){
        if (weapon == null){
            return;
        }
        this.name = weapon.name;
        this.damage = weapon.damage;
        this.maxResistance = weapon.resistance;
        this.resistance = weapon.resistance;
        this.attackRange = weapon.attackRange;
        this.attackSpeed = weapon.attackSpeed;
        this.size = weapon.size;
        this.texturePath = weapon.texturePath;
    }

    public OnlineMeleeWeapon getNew(){

        return new OnlineMeleeWeapon(this.name, this.texturePath, damage, maxResistance, attackRange, attackSpeed, size);
    }

    public OnlineMeleeWeapon(){}
}
