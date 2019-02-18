package com.fushiginopixel.fushiginopixeldungeon.levels.builders;

import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TreeBuilder extends RegularBuilder {

    @Override
    public ArrayList<Room> build(ArrayList<Room> rooms) {
        setupRooms(rooms);

        if (entrance == null){
            return null;
        }

        entrance.setSize();
        entrance.setPos(0, 0);

        float startAngle = Random.Float(0, 360);

        ArrayList<Room> line = new ArrayList<>();
        int roomsOnLine = (int)(multiConnections.size()*Random.Float(pathLength/3 ,pathLength)) + Random.chances(pathLenJitterChances);
        roomsOnLine = Math.min(roomsOnLine, multiConnections.size());
        roomsOnLine++;

        float[] pathTunnels = pathTunnelChances.clone();
        for (int i = 0; i < roomsOnLine; i++){
            if (i == 0)
                line.add(entrance);
            else
                line.add(multiConnections.remove(0));

            int tunnels = Random.chances(pathTunnels);
            if (tunnels == -1){
                pathTunnels = pathTunnelChances.clone();
                tunnels = Random.chances(pathTunnels);
            }
            pathTunnels[tunnels]--;

            for (int j = 0; j < tunnels; j++){
                line.add(ConnectionRoom.createRoom());
            }
        }

        if(exit != null && !line.contains(exit)) {
            line.add(exit);
        }

        Room prev = entrance;
        float targetAngle = startAngle;
        for (int i = 1; i < line.size(); i++){
            Room r = line.get(i);
            targetAngle += Random.Float(-90, 90);
            if (placeRoom(rooms, prev, r, targetAngle) != -1) {
                prev = r;
                if (!rooms.contains(prev)) {
                    rooms.add(prev);
                }
            } else {
                return null;
                //FIXME this is lazy, there are ways to do this without relying on chance

            }
        }

        if (shop != null) {
            float angle;
            int tries = 10;
            do {
                angle = placeRoom(line, entrance, shop, Random.Float(360f));
                tries--;
            } while (angle == -1 && tries >= 0);
            if (angle == -1) return null;
        }

        ArrayList<Room> branchable = new ArrayList<>(line);

        ArrayList<Room> roomsToBranch = new ArrayList<>();
        roomsToBranch.addAll(multiConnections);
        roomsToBranch.addAll(singleConnections);
        weightRooms(branchable);
        createBranches(rooms, branchable, roomsToBranch, branchTunnelChances);

        findNeighbours(rooms);

        for (Room r : rooms){
            for (Room n : r.neigbours){
                if (!n.connected.containsKey(r)
                        && Random.Float() < extraConnectionChance){
                    r.connect(n);
                }
            }
        }

        return rooms;
    }

    /*
    public static <T extends Room> void buildDistanceMap(Collection<T> nodes, Room focus ) {

        for (T node : nodes) {
            node.distance( Integer.MAX_VALUE );
        }

        LinkedList<Room> queue = new LinkedList<Room>();

        focus.distance( 0 );
        queue.add( focus );

        while (!queue.isEmpty()) {

            Room node = queue.poll();
            int distance = node.distance();
            int price = node.price();

            for (Room edge : node.neigbours) {
                if (edge.distance() > distance + price) {
                    queue.add( edge );
                    edge.distance( distance + price );
                }
            }
        }
    }


    public static <T extends Room> List<T> buildPath( Collection<T> nodes, T from, T to ) {

        List<T> path = new ArrayList<T>();

        T room = from;
        while (room != to) {

            int min = room.distance();
            T next = null;

            Collection<? extends Room> edges = room.neigbours;

            for (Room edge : edges) {

                int distance = edge.distance();
                if (distance < min) {
                    min = distance;
                    next = (T)edge;
                }
            }

            if (next == null) {
                return null;
            }

            path.add( next );
            room = next;
        }

        return path;
    }

    protected void split( Room room ,Rect rect ) {

        int w = rect.width();
        int h = rect.height();

        if (w > room.maxWidth() && h < room.minHeight()) {

            int vw = Random.Int(rect.left + 3, rect.right - 3);
            split(room, new Rect(rect.left, rect.top, vw, rect.bottom));
            split(room, new Rect(vw, rect.top, rect.right, rect.bottom));

        } else if (h > room.maxHeight() && w < room.minWidth()) {

            int vh = Random.Int(rect.top + 3, rect.bottom - 3);
            split(room, new Rect(rect.left, rect.top, rect.right, vh));
            split(room, new Rect(rect.left, vh, rect.right, rect.bottom));

        } else if ((Math.random() <= (room.minWidth() * room.minHeight() / rect.square()) && w <= room.maxWidth() && h <= room.maxHeight()) || w < room.minWidth() || h < room.minHeight()) {

            room.set(rect);

        } else {

            if (Random.Float() < (float) (w - 2) / (w + h - 4)) {
                int vw = Random.Int(rect.left + 3, rect.right - 3);
                split(room, new Rect(rect.left, rect.top, vw, rect.bottom));
                split(room, new Rect(vw, rect.top, rect.right, rect.bottom));
            } else {
                int vh = Random.Int(rect.top + 3, rect.bottom - 3);
                split(room, new Rect(rect.left, rect.top, rect.right, vh));
                split(room, new Rect(rect.left, vh, rect.right, rect.bottom));
            }

        }
    }

    protected void split( ArrayList<Room> rooms ,Rect rect ) {

        int w = rect.width();
        int h = rect.height();
        EmptyRoom r = new EmptyRoom();

        if (w > r.maxWidth() && h < r.minHeight()) {

            int vw = Random.Int( rect.left + 3, rect.right - 3 );
            split( rooms ,new Rect( rect.left, rect.top, vw, rect.bottom ) );
            split( rooms ,new Rect( vw, rect.top, rect.right, rect.bottom ) );

        } else
        if (h > r.maxHeight() && w < r.minWidth()) {

            int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
            split( rooms ,new Rect( rect.left, rect.top, rect.right, vh ) );
            split( rooms ,new Rect( rect.left, vh, rect.right, rect.bottom ) );

        } else
        if ((Math.random() <= (r.minHeight() * r.minWidth() / rect.square()) && w <= r.maxWidth() && h <= r.maxHeight()) || w < r.minWidth() || h < r.minHeight()) {

            rooms.add( (Room)r.set( rect ) );

        } else {

            if (Random.Float() < (float)(w - 2) / (w + h - 4)) {
                int vw = Random.Int( rect.left + 3, rect.right - 3 );
                split( rooms , new Rect( rect.left, rect.top, vw, rect.bottom ) );
                split( rooms , new Rect( vw, rect.top, rect.right, rect.bottom ) );
            } else {
                int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
                split( rooms , new Rect( rect.left, rect.top, rect.right, vh ) );
                split( rooms , new Rect( rect.left, vh, rect.right, rect.bottom ) );
            }

        }
    }
    */
}
