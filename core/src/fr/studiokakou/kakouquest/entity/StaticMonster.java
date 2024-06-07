package fr.studiokakou.kakouquest.entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class StaticMonster {

    public static Dictionary<Integer, ArrayList<OnlineMonster>> possibleMonsters = new Hashtable<>();

    public static void createPossibleMonsters(int currentLevel){
        possibleMonsters = new Hashtable<>();
        possibleMonsters.put(1, new ArrayList<>());
        possibleMonsters.put(2, new ArrayList<>());
        possibleMonsters.put(3, new ArrayList<>());
        possibleMonsters.put(4, new ArrayList<>());
        possibleMonsters.put(5, new ArrayList<>());
        possibleMonsters.put(6, new ArrayList<>());
        possibleMonsters.put(7, new ArrayList<>());
        possibleMonsters.put(8, new ArrayList<>());
        possibleMonsters.put(9, new ArrayList<>());
        possibleMonsters.put(10, new ArrayList<>());
        possibleMonsters.put(11, new ArrayList<>());
        possibleMonsters.put(12, new ArrayList<>());


        possibleMonsters.get(10).add(BIG_DEMON(currentLevel));
        possibleMonsters.get(12).add(BIG_ZOMBIE(currentLevel));
        possibleMonsters.get(5).add(CHORT(currentLevel));
        possibleMonsters.get(1).add(GOBLIN(currentLevel));
        possibleMonsters.get(3).add(IMP(currentLevel));
        possibleMonsters.get(2).add(MASKED_ORC(currentLevel));
        possibleMonsters.get(5).add(MUDDY(currentLevel));
        possibleMonsters.get(12).add( OGRE(currentLevel));
        possibleMonsters.get(1).add(ORC_WARRIOR(currentLevel));
        possibleMonsters.get(2).add(SKELET(currentLevel));
        possibleMonsters.get(7).add(SWAMPY(currentLevel));
        possibleMonsters.get(1).add(TINY_ZOMBIE(currentLevel));
        possibleMonsters.get(4).add(WOGOL(currentLevel));
    }

    static OnlineMonster BIG_DEMON(int currentLevel){
        return new OnlineMonster("Big Demon", "assets/entities/big_demon_idle.png", "assets/entities/big_demon_run.png", 400, 25, 1200, 40f, 150, currentLevel, 32.0, 36.0);
    }
    static OnlineMonster BIG_ZOMBIE(int currentLevel){
        return new OnlineMonster("Big Zombie", "assets/entities/big_zombie_idle.png", "assets/entities/big_zombie_run.png", 450, 35, 1500, 45f, 200, currentLevel, 32.0, 36.0);
    }
    static OnlineMonster CHORT(int currentLevel){
        return new OnlineMonster("Chort", "assets/entities/chort_idle.png", "assets/entities/chort_run.png", 70, 15, 700, 60f, 80, currentLevel, 16.0, 23.0);
    }
    static OnlineMonster GOBLIN(int currentLevel){
        return new OnlineMonster("Goblin", "assets/entities/goblin_idle.png", "assets/entities/goblin_run.png", 60, 10, 700, 50f, 100, currentLevel, 16.0, 16.0);
    }
    static OnlineMonster IMP(int currentLevel){
        return new OnlineMonster("Imp", "assets/entities/imp_idle.png", "assets/entities/imp_run.png", 35, 15, 600, 60f, 100, currentLevel, 16.0, 16.0);
    }
    static OnlineMonster MASKED_ORC(int currentLevel){
        return new OnlineMonster("Masked Orc", "assets/entities/masked_orc_idle.png", "assets/entities/masked_orc_run.png", 150, 20, 600, 50f, 120, currentLevel, 16.0, 23.0);
    }
    static OnlineMonster MUDDY(int currentLevel){
        return new OnlineMonster("Muddy", "assets/entities/muddy.png", "assets/entities/muddy.png", 250, 40, 600, 15f, 200, currentLevel, 16.0, 16.0);
    }
    static OnlineMonster OGRE(int currentLevel){
        return new OnlineMonster("Ogre", "assets/entities/ogre_idle.png", "assets/entities/ogre_run.png", 500, 25, 2000, 50f, 200, currentLevel, 32.0, 36.0);
    }
    static OnlineMonster ORC_WARRIOR(int currentLevel){
        return new OnlineMonster("Orc Warrior", "assets/entities/orc_warrior_idle.png", "assets/entities/orc_warrior_run.png", 120, 20, 600, 50f, 120, currentLevel, 16.0, 23.0);
    }
    static OnlineMonster SKELET(int currentLevel){
        return new OnlineMonster("Skelet", "assets/entities/skelet_idle.png", "assets/entities/skelet_run.png", 30, 30, 300, 50f, 120, currentLevel, 16.0, 16.0);
    }
    static OnlineMonster SWAMPY(int currentLevel){
        return new OnlineMonster("Swampy", "assets/entities/swampy.png", "assets/entities/swampy.png", 400, 50, 800, 18f, 200, currentLevel, 16.0, 16.0);
    }
    static OnlineMonster TINY_ZOMBIE(int currentLevel){
        return new OnlineMonster("Tiny Zombie", "assets/entities/tiny_zombie_idle.png", "assets/entities/tiny_zombie_run.png", 20, 25, 600, 55f, 100, currentLevel, 16.0, 16.0);
    }
    static OnlineMonster WOGOL(int currentLevel){
        return new OnlineMonster("Wogol", "assets/entities/wogol_idle.png", "assets/entities/wogol_run.png", 200, 20, 600, 50f, 150, currentLevel, 16.0, 23.0);
    }
}
