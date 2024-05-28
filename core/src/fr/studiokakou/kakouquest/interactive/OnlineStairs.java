package fr.studiokakou.kakouquest.interactive;

import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.OnlinePlayer;

import java.util.ArrayList;

public class OnlineStairs {
    public Point pos;
    public ArrayList<OnlinePlayer> players = new ArrayList<>();

    public OnlineStairs(Stairs stairs){
        this.pos = stairs.pos;
    }

    public OnlineStairs(Point pos){
        this.pos = pos;
    }

    public OnlineStairs(){}

    public Stairs toStairs(){
        Stairs stairs = new Stairs(this.pos);
        return stairs;
    }
}
