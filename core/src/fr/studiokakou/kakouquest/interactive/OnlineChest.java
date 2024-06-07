package fr.studiokakou.kakouquest.interactive;

import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.OnlineMeleeWeapon;
import fr.studiokakou.kakouquest.weapon.StaticsMeleeWeapon;

import java.util.ArrayList;

public class OnlineChest {
    public OnlineMeleeWeapon meleeWeaponLoot;
    public boolean isOpened=false;
    public Point pos;
    boolean hasInteracted;

    public OnlineChest(Point pos, int currentLevel){
        this.pos = pos;
        this.isOpened = false;
        this.meleeWeaponLoot = getRandomMeleeWeapon(currentLevel);
        this.hasInteracted = false;
    }

    public OnlineChest(Chest chest){
        this.pos = chest.pos;
        this.isOpened = chest.isOpened;
        if (chest.meleeWeaponLoot != null){
            this.meleeWeaponLoot = new OnlineMeleeWeapon(chest.meleeWeaponLoot);
        } else {
            this.meleeWeaponLoot = null;
        }
        this.hasInteracted = chest.hasInteracted;
    }

    public OnlineChest(){}

    public OnlineMeleeWeapon getRandomMeleeWeapon(int currentLevel){
        ArrayList<Integer> randomRarity = new ArrayList<>();

        for (int i = 1; i <= currentLevel; i++) {
            for (int j = 0; j <= currentLevel-i; j++) {
                if (StaticsMeleeWeapon.possibleMeleeWeapon.get(i) != null){
                    randomRarity.add(i);
                }
            }
        }

        int rarity = randomRarity.get(Utils.randint(0, randomRarity.size() - 1));
        ArrayList<OnlineMeleeWeapon> rarityMeleeWeapon = StaticsMeleeWeapon.possibleMeleeWeapon.get(rarity);

        while (rarityMeleeWeapon==null || rarityMeleeWeapon.isEmpty()){
            rarity = randomRarity.get(Utils.randint(0, randomRarity.size() - 1));
            rarityMeleeWeapon = StaticsMeleeWeapon.possibleMeleeWeapon.get(rarity);
        }

        return rarityMeleeWeapon.get(Utils.randint(0, rarityMeleeWeapon.size()-1)).getNew();
    }

    public static ArrayList<OnlineChest> chestsListToOnlineChests(ArrayList<Chest> chests){
        ArrayList<OnlineChest> onlineChests = new ArrayList<>();
        for (Chest chest : chests){
            onlineChests.add(new OnlineChest(chest));
        }
        return onlineChests;
    }

}
