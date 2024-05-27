package fr.studiokakou.kakouquest.weapon;


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

    public OnlineMeleeWeapon getNew(){
        return new OnlineMeleeWeapon(this.name, this.texturePath, damage, maxResistance, attackRange, attackSpeed, size);
    }

    public OnlineMeleeWeapon(){}
}
