package fr.studiokakou.kakouquest.constants;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Constants {
    //animation
    public static float FRAME_DURATION=0.17f;

    //attack var
    public static float PLAYER_MELEE_WEAPON_DISTANCE=10f;
    public static float ATTACK_PAUSE = 200f; //en millisecondes
    public static int ATTACK_STAMINA_USAGE = 2;

    //dash stats
    public static float DASH_DISTANCE = 50f;
    public static float DASH_SPEED = 500f;
    public static long DASH_PAUSE = 3;   //en secondes
    public static int DASH_STAMINA_USAGE = 10;

    //font
    public static BitmapFont usernameFont = new BitmapFont();
}
