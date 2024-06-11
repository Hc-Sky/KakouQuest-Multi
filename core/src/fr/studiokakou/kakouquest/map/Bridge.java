package fr.studiokakou.kakouquest.map;

import java.util.ArrayList;

/**
 * The Bridge class represents a bridge in the game, which connects two rooms.
 * It is used only during the map creation process.
 */
public class Bridge {
    /**
     * A list of points representing the bridge's path.
     */
    public ArrayList<Point> points;

    /**
     * Constructs a Bridge object connecting two rooms and generates the bridge path.
     *
     * @param room1 the first room to connect
     * @param room2 the second room to connect
     * @param rooms a list of all rooms to check for intersections
     */
    public Bridge(Room room1, Room room2, ArrayList<Room> rooms) {
        this.points = new ArrayList<>();
        generateBridge(room1, room2, rooms);
    }

    /**
     * Default constructor for Bridge.
     */
    public Bridge() {}

    /**
     * Generates the path of the bridge connecting two rooms.
     *
     * @param room1 the first room to connect
     * @param room2 the second room to connect
     * @param rooms a list of all rooms to check for intersections
     */
    public void generateBridge(Room room1, Room room2, ArrayList<Room> rooms) {
        Point center1 = room1.getCenter().add(-0.5f, -0.5f); // Get center of the 2 rooms
        Point center2 = room2.getCenter().add(-0.5f, -0.5f);

        // Starts at the center of the first room and moves towards the center of the second room

        float currentX = center1.x;
        float currentY = center1.y;
        while (currentX < center2.x) {
            currentX += 1;
            Point p = new Point(currentX, currentY);
            if (!isPointInRooms(p, rooms)) {
                this.points.add(p);
            }
        }
        while (currentX > center2.x) {
            currentX -= 1;
            Point p = new Point(currentX, currentY);
            if (!isPointInRooms(p, rooms)) {
                this.points.add(p);
            }
        }
        while (currentY < center2.y) {
            currentY += 1;
            Point p = new Point(currentX, currentY);
            if (!isPointInRooms(p, rooms)) {
                this.points.add(p);
            }
        }
        while (currentY > center2.y) {
            currentY -= 1;
            Point p = new Point(currentX, currentY);
            if (!isPointInRooms(p, rooms)) {
                this.points.add(p);
            }
        }
    }

    /**
     * Checks if a given point is inside any of the rooms.
     * If it is, the bridge will stop to get generated.
     *
     * @param p the point to check
     * @param rooms a list of rooms to check against
     * @return true if the point is inside any room, false otherwise
     */
    public boolean isPointInRooms(Point p, ArrayList<Room> rooms) {
        for (Room r : rooms) {
            if (Room.isPointInRoom(p, r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given point is touching any of the rooms.
     *
     * @param p the point to check
     * @param rooms a list of rooms to check against
     * @return true if the point is touching any room, false otherwise
     */
    public boolean isPointInRoomsTouching(Point p, ArrayList<Room> rooms) {
        for (Room r : rooms) {
            if (Room.isPointInRoomTouching(p, r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given point is part of any existing bridges.
     *
     * @param point the point to check
     * @param bridges a list of bridges to check against
     * @return true if the point is part of any bridge, false otherwise
     */
    public boolean isPointsInBridges(Point point, ArrayList<Bridge> bridges) {
        for (Bridge b : bridges) {
            if (!b.equals(this)) {
                for (Point p : b.points) {
                    if (p.equals(point)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if a given point is a turning point in the bridge.
     *
     * @param pointIndex the index of the point to check
     * @return true if the point is a turning point, false otherwise
     */
    public boolean isTurn(int pointIndex) {
        if (pointIndex == 0 || pointIndex == this.points.size() - 1) {
            return false;
        }
        if (this.points.get(pointIndex).x == this.points.get(pointIndex + 1).x && this.points.get(pointIndex).y == this.points.get(pointIndex - 1).y) {
            return true;
        }
        return false;
    }

    /**
     * Generates walls for the bridge.
     * The hardest part of the bridge class.
     * For every posssibilities of walls, we need to check if the wall is not in a room, not in a bridge and not in a turning point.
     *
     * @param rooms a list of rooms to check for intersections
     * @param bridges a list of existing bridges to avoid overlaps
     * @return a list of generated walls for the bridge
     */
    public ArrayList<OnlineWall> genBridgeWall(ArrayList<Room> rooms, ArrayList<Bridge> bridges) {
        ArrayList<OnlineWall> result = new ArrayList<>();

        for (int i = 0; i < this.points.size() - 1; i++) {
            if (!isPointInRoomsTouching(this.points.get(i), rooms) && !isTurn(i) && !isPointsInBridges(this.points.get(i), bridges)) {
                if (i == this.points.size() - 1) {
                    if (this.points.get(i).y == this.points.get(i - 1).y) {
                        result.add(new OnlineWall(this.points.get(i).add(0, 1), "assets/map/wall_mid.png"));
                        result.add(new OnlineWall(this.points.get(i).add(0, 2), "assets/map/wall_top_mid.png"));
                        result.add(new OnlineWall(this.points.get(i).add(0, -1), "assets/map/wall_mid.png"));
                        result.add(new OnlineWall(this.points.get(i), "assets/map/wall_top_mid.png"));
                    } else if (this.points.get(i).x == this.points.get(i - 1).x) {
                        result.add(new OnlineWall(this.points.get(i).add(1, 0), "assets/map/wall_outer_mid_right.png"));
                        result.add(new OnlineWall(this.points.get(i).add(-1, 0), "assets/map/wall_outer_mid_left.png"));
                    }
                } else {
                    if (this.points.get(i).y == this.points.get(i + 1).y) {
                        result.add(new OnlineWall(this.points.get(i).add(0, 1), "assets/map/wall_mid.png"));
                        result.add(new OnlineWall(this.points.get(i).add(0, 2), "assets/map/wall_top_mid.png"));
                        result.add(new OnlineWall(this.points.get(i).add(0, -1), "assets/map/wall_mid.png"));
                        result.add(new OnlineWall(this.points.get(i), "assets/map/wall_top_mid.png"));
                    } else if (this.points.get(i).x == this.points.get(i + 1).x) {
                        result.add(new OnlineWall(this.points.get(i).add(1, 0), "assets/map/wall_outer_mid_right.png"));
                        result.add(new OnlineWall(this.points.get(i).add(-1, 0), "assets/map/wall_outer_mid_left.png"));
                    }
                }
            }
        }

        return result;
    }
}
