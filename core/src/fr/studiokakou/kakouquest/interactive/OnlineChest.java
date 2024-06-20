package fr.studiokakou.kakouquest.interactive;

import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.utils.Utils;
import fr.studiokakou.kakouquest.weapon.OnlineMeleeWeapon;
import fr.studiokakou.kakouquest.weapon.StaticsMeleeWeapon;

import java.util.ArrayList;

/**
 * The OnlineChest class represents a chest in the online game, containing a random melee weapon.
 */
public class OnlineChest {
    public OnlineMeleeWeapon meleeWeaponLoot;


    public boolean isOpened = false;
    public Point pos;
    boolean hasInteracted;

    /**
     * Constructs an OnlineChest at a given position with a specified current level.
     *
     * @param pos the position of the chest
     * @param currentLevel the current level, used to determine the loot rarity
     */
    public OnlineChest(Point pos, int currentLevel) {
        this.pos = pos;
        this.isOpened = false;
        this.meleeWeaponLoot = getRandomMeleeWeapon(currentLevel);
        this.hasInteracted = false;
    }

    /**
     * Constructs an OnlineChest based on an existing Chest.
     *
     * @param chest the existing Chest to base this OnlineChest on
     */
    public OnlineChest(Chest chest) {
        this.pos = chest.pos;
        this.isOpened = chest.isOpened;
        if (chest.meleeWeaponLoot != null) {
            this.meleeWeaponLoot = new OnlineMeleeWeapon(chest.meleeWeaponLoot);
        } else {
            this.meleeWeaponLoot = null;
        }
        this.hasInteracted = chest.hasInteracted;
    }

    /**
     * Default constructor for Kryo.
     */
    public OnlineChest() {}

    /**
     * Gets a random melee weapon based on the current level.
     *
     * @param currentLevel the current level, used to determine the loot rarity
     * @return a randomly selected OnlineMeleeWeapon
     */
    public OnlineMeleeWeapon getRandomMeleeWeapon(int currentLevel) {
        ArrayList<Integer> randomRarity = new ArrayList<>();

        for (int i = 1; i <= currentLevel; i++) {
            for (int j = 0; j <= currentLevel - i; j++) {
                if (StaticsMeleeWeapon.possibleMeleeWeapon.get(i) != null) {
                    randomRarity.add(i);
                }
            }
        }

        int rarity = randomRarity.get(Utils.randint(0, randomRarity.size() - 1));
        ArrayList<OnlineMeleeWeapon> rarityMeleeWeapon = StaticsMeleeWeapon.possibleMeleeWeapon.get(rarity);

        while (rarityMeleeWeapon == null || rarityMeleeWeapon.isEmpty()) {
            rarity = randomRarity.get(Utils.randint(0, randomRarity.size() - 1));
            rarityMeleeWeapon = StaticsMeleeWeapon.possibleMeleeWeapon.get(rarity);
        }

        return rarityMeleeWeapon.get(Utils.randint(0, rarityMeleeWeapon.size() - 1)).getNew();
    }

    /**
     * Converts a list of Chest objects to a list of OnlineChest objects.
     *
     * @param chests the list of Chest objects to convert
     * @return a list of OnlineChest objects
     */
    public static ArrayList<OnlineChest> chestsListToOnlineChests(ArrayList<Chest> chests) {
        ArrayList<OnlineChest> onlineChests = new ArrayList<>();
        for (Chest chest : chests) {
            onlineChests.add(new OnlineChest(chest));
        }
        return onlineChests;
    }
}
