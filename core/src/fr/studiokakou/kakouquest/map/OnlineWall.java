package fr.studiokakou.kakouquest.map;

public class OnlineWall {
    Point pos;
    String assetPath;

    public OnlineWall(Point pos, String assetPath){
        this.pos = pos;
        this.assetPath=assetPath;
    }

    public OnlineWall(Wall wall){
        this.pos = wall.pos;
        this.assetPath = wall.assetPath;
    }

    public OnlineWall(){}
}
