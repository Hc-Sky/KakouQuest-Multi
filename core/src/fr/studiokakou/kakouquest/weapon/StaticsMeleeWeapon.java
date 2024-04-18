package fr.studiokakou.kakouquest.weapon;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class StaticsMeleeWeapon {

    public static OnlineMeleeWeapon meleeWeaponToOnline(MeleeWeapon meleeWeapon){
        if (meleeWeapon==null){
            return null ;
        }

        return new OnlineMeleeWeapon(
            meleeWeapon.name,
            meleeWeapon.texturePath,
            meleeWeapon.damage,
            meleeWeapon.resistance,
            meleeWeapon.attackRange,
            meleeWeapon.attackSpeed,
            meleeWeapon.size
        );
    }

    public static MeleeWeapon onlinetToMeleeWeapon(OnlineMeleeWeapon meleeWeapon){
        if (meleeWeapon == null){
            return null;
        }

        return new MeleeWeapon(
            meleeWeapon.name,
            meleeWeapon.texturePath,
            meleeWeapon.damage,
            meleeWeapon.resistance,
            meleeWeapon.attackRange,
            meleeWeapon.attackSpeed,
            meleeWeapon.size
        );
    }

    public static Dictionary<Integer, ArrayList<OnlineMeleeWeapon>> possibleMeleeWeapon = new Hashtable<>();

    public static void createPossibleMeleeWeapons(){
        possibleMeleeWeapon = new Hashtable<>();
        possibleMeleeWeapon.put(1, new ArrayList<>());
        possibleMeleeWeapon.put(2, new ArrayList<>());
        possibleMeleeWeapon.put(3, new ArrayList<>());
        possibleMeleeWeapon.put(4, new ArrayList<>());
        possibleMeleeWeapon.put(5, new ArrayList<>());
        possibleMeleeWeapon.put(6, new ArrayList<>());
        possibleMeleeWeapon.put(7, new ArrayList<>());
        possibleMeleeWeapon.put(8, new ArrayList<>());
        possibleMeleeWeapon.put(9, new ArrayList<>());
        possibleMeleeWeapon.put(10, new ArrayList<>());

        possibleMeleeWeapon.get(8).add(ANIME_SWORD());
        possibleMeleeWeapon.get(6).add(BATON_WITH_SPIKES());
        possibleMeleeWeapon.get(8).add(BIG_HAMMER());
        possibleMeleeWeapon.get(5).add(CLEAVER());
        possibleMeleeWeapon.get(6).add(DOUBLE_AXE());
        possibleMeleeWeapon.get(4).add(DUEL_SWORD());
        possibleMeleeWeapon.get(10).add(GOLDEN_SWORD());
        possibleMeleeWeapon.get(3).add(HAMMER());
        possibleMeleeWeapon.get(8).add(KATANA());
        possibleMeleeWeapon.get(1).add(KNIFE());
        possibleMeleeWeapon.get(4).add(KNIGHT_SWORD());
        possibleMeleeWeapon.get(10).add(LAVISH_SWORD());
        possibleMeleeWeapon.get(6).add(MACE());
        possibleMeleeWeapon.get(3).add(MACHETE());
        possibleMeleeWeapon.get(9).add(RED_GEM_SWORD());
        possibleMeleeWeapon.get(3).add(REGULAR_SWORD());
        possibleMeleeWeapon.get(4).add(SAW_SWORD());
        possibleMeleeWeapon.get(1).add(THROWING_AXE());
        possibleMeleeWeapon.get(4).add(WARAXE());
    }

    public static OnlineMeleeWeapon ANIME_SWORD () {return new OnlineMeleeWeapon("Anime sword", "assets/weapon/weapon_anime_sword.png", 80, 100, 140, 0.6f, 1);}
    public static OnlineMeleeWeapon BATON_WITH_SPIKES () {return new OnlineMeleeWeapon("Baton with spikes", "assets/weapon/weapon_baton_with_spikes.png", 40, 35, 150, 0.2f, 1.2f);}
    public static OnlineMeleeWeapon BIG_HAMMER () {return new OnlineMeleeWeapon("Big hammer", "assets/weapon/weapon_big_hammer.png", 70, 55, 160, 0.3f, 1.5f);}
    public static OnlineMeleeWeapon CLEAVER () {return new OnlineMeleeWeapon("Cleaver", "assets/weapon/weapon_cleaver.png", 20, 60, 100, 0.6f, 1);}
    public static OnlineMeleeWeapon DOUBLE_AXE () {return new OnlineMeleeWeapon("Double axe", "assets/weapon/weapon_double_axe.png", 50, 60, 180, 0.4f, 1);}
    public static OnlineMeleeWeapon DUEL_SWORD () {return new OnlineMeleeWeapon("Duel sword", "assets/weapon/weapon_duel_sword.png", 35, 70, 100, 0.9f, 1);}
    public static OnlineMeleeWeapon GOLDEN_SWORD () {return new OnlineMeleeWeapon("Golden sword", "assets/weapon/weapon_golden_sword.png", 100, 100, 120, 0.6f, 1);}
    public static OnlineMeleeWeapon HAMMER () {return new OnlineMeleeWeapon("Hammer", "assets/weapon/weapon_hammer.png", 40, 40, 90, 0.5f, 1);}
    public static OnlineMeleeWeapon KATANA () {return new OnlineMeleeWeapon("Katana", "assets/weapon/weapon_katana.png", 50, 100, 130, 0.7f, 1);}
    public static OnlineMeleeWeapon KNIFE () {return new OnlineMeleeWeapon("Knife", "assets/weapon/weapon_knife.png", 12, 40, 90, 0.7f, 1);}
    public static OnlineMeleeWeapon KNIGHT_SWORD () {return new OnlineMeleeWeapon("Knight sword", "assets/weapon/weapon_knight_sword.png", 35, 80, 110, 0.7f, 1);}
    public static OnlineMeleeWeapon LAVISH_SWORD () {return new OnlineMeleeWeapon("Lavish sword", "assets/weapon/weapon_lavish_sword.png", 90, 120, 120, 0.8f, 1);}
    public static OnlineMeleeWeapon MACE () {return new OnlineMeleeWeapon("Mace", "assets/weapon/weapon_mace.png", 60, 30, 180, 0.3f, 1);}
    public static OnlineMeleeWeapon MACHETE () {return new OnlineMeleeWeapon("Machete", "assets/weapon/weapon_machete.png", 20, 40, 90, 0.5f, 1);}
    public static OnlineMeleeWeapon RED_GEM_SWORD () {return new OnlineMeleeWeapon("Red gem sword", "assets/weapon/weapon_red_gem_sword.png", 70, 120, 100, 0.5f, 1);}
    public static OnlineMeleeWeapon REGULAR_SWORD () {return new OnlineMeleeWeapon("Regular sword", "assets/weapon/weapon_regular_sword.png", 25, 70, 110, 0.5f, 1);}
    public static OnlineMeleeWeapon RUSTY_SWORD () {return new OnlineMeleeWeapon("Rusty sword", "assets/weapon/weapon_rusty_sword.png", 10, -1000, 90, 0.5f, 1);}
    public static OnlineMeleeWeapon SAW_SWORD () {return new OnlineMeleeWeapon("Saw sword", "assets/weapon/weapon_saw_sword.png", 65, 80, 120, 0.5f, 1);}
    public static OnlineMeleeWeapon THROWING_AXE () {return new OnlineMeleeWeapon("Throwing axe", "assets/weapon/weapon_throwing_axe.png", 15, 50, 90, 0.7f, 1);}
    public static OnlineMeleeWeapon WARAXE () {return new OnlineMeleeWeapon("Waraxe", "assets/weapon/weapon_waraxe.png", 45, 60, 120, 0.4f, 1.3f);}

    //dev weapon
    public static OnlineMeleeWeapon DEV_SWORD () {return new OnlineMeleeWeapon("Dev sword", "assets/weapon/weapon_golden_sword.png", 200, -1, 120, 0.7f, 1);}
}
