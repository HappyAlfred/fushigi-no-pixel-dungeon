package com.fushiginopixel.fushiginopixeldungeon.levels.builders;

import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WebBuilder extends RegularBuilder {

    int tunnelLength = 6;

    public WebBuilder setWebTunnelLength(int tunnelLength){
        this.tunnelLength = Math.abs(tunnelLength);
        return this;
    }

    @Override
    public ArrayList<Room> build(ArrayList<Room> rooms) {
        setupRooms(rooms);

        if (entrance == null){
            return null;
        }

        entrance.setSize();
        entrance.setPos(0, 0);

        //multiConnections rooms will place
        ArrayList<Room> web = new ArrayList<>();
        int webCore = multiConnections.size() + 1;

        float[] pathTunnels = pathTunnelChances.clone();

        //start at the entrance room
        for (int i = 0; i < webCore; i++){
            if (i == 0)
                web.add(entrance);
            else
                web.add(multiConnections.remove(0));
        }

        //end at the exit room
        if(exit != null && !web.contains(exit)) {
            web.add(exit);
        }

        int connectingRooms = Random.Int(0,3);
        int tl = this.tunnelLength * (connectingRooms + 1);
        Room prev;
        //placed non connection rooms
        ArrayList<Room> placed = new ArrayList<>();
        for (int i = 0; i < web.size(); i++){
            //current room
            prev = web.get(i);
            float targetAngle = Random.Float(0, 360);
            //3~6 rotate branch
            int rotate = Random.IntRange(3,6);
            if(prev.isEmpty()){
                return null;
            }
            for(int j = 0, k = 1; j < rotate; j++){

                if(i + k >= web.size() || (!placed.isEmpty() && placed.contains(web.get(i + k))))break;
                for(int curRotate = 0; curRotate < rotate; curRotate++) {
                    targetAngle += 360 / rotate;
                    ArrayList<Room> tunnels = new ArrayList<>();

                    Room curr = prev;
                    for (int l = 0; l < connectingRooms; l++){
                        ConnectionRoom t = ConnectionRoom.createRoom();
                        if (placeRoom(rooms, curr, t, targetAngle) != -1) {
                            tunnels.add(t);
                            rooms.add(t);

                            curr = t;
                        } else{
                            for (Room c1 : tunnels){
                                c1.clearConnections();
                                rooms.remove(c1);
                            }
                            tunnels.clear();

                            curr = prev;
                        }
                    }

                    if (tunnels.size() != connectingRooms){
                        break;
                    }

                    //next room
                    Room r = web.get(i + k);
                    if (placeRoom(rooms, curr, r, targetAngle) != -1) {
                        placed.add(r);
                        if (!rooms.contains(r)) {
                            rooms.add(r);
                        }
                        k++;
                        break;
                    } else {
                        for (Room t : tunnels){
                            t.clearConnections();
                            rooms.remove(t);
                        }
                        tunnels.clear();
                        if (!r.isEmpty()) {//not empty but not connected
                            r.setEmpty();
                            break;
                        }
                    }
                }

                /*
                //next room oversize or placed rooms already has next room, stop place
                if(i + k >= web.size() || (!placed.isEmpty() && placed.contains(web.get(i + k))))break;
                for(int curRotate = 0; curRotate < rotate; curRotate++) {
                    //next room
                    Room r = web.get(i + k);
                    targetAngle += 360 / rotate;
                    if (placeRoom(rooms, prev, r, targetAngle) != -1) {
                        placed.add(r);
                        if (!rooms.contains(r)) {
                            rooms.add(r);
                        }
                        k++;
                        break;
                    } else if (!r.isEmpty()) {//not empty but not connected
                        r.setEmpty();
                        break;
                    }
                }
                */
            }
        }

        for(int i = 0; i < web.size() - 1; i++){
            Room ra = web.get(i);
            for(int j = 1; j < web.size(); j++){
                Room rb = web.get(j);
                Rect rect = ra.apart( rb );
                if(ra.connect(rb)){

                }
                else if (rect.width() <= tl && rect.height() <= tl
                        && !rb.checkTunnelConnect(ra)
                        && !(rb instanceof ConnectionRoom) && !(ra instanceof ConnectionRoom) /*&& (web.size() <= webCore * 3 || !(rb instanceof ConnectionRoom))*/) {
                    prev = rb;
                    ArrayList<Room> tunnels = new ArrayList<>();
                    while(!prev.connect(ra)){
                        ConnectionRoom c = ConnectionRoom.createRoom();
                        if (placeRoom(rooms, prev, c, angleBetweenRooms(prev, ra)) == -1){
                            for (Room c1 : tunnels){
                                c1.clearConnections();
                                rooms.remove(c1);
                                //web.remove(c1);
                            }
                            tunnels.clear();
                            break;
                        } else {
                            prev = c;
                            tunnels.add(c);
                            rooms.add(c);
                            //web.add(c);
                        }

                    }

                    /*
                    LinkedHashMap<Room, Room.Door> connectedA = new LinkedHashMap<>(ra.connected);
                    LinkedHashMap<Room, Room.Door> connectedB = new LinkedHashMap<>(rb.connected);
                    while(!prev.connect(ra)){
                        ConnectionRoom c = ConnectionRoom.createRoom();
                        if (placeRoom(rooms, prev, c, angleBetweenRooms(prev, ra)) == -1){

                            ra.neigbours.remove(c);
                            if(!tunnels.isEmpty()){
                                rooms.removeAll(tunnels);
                            }
                            for(Room clr :tunnels){
                                ra.neigbours.remove(clr);
                                clr.setEmpty();
                            }
                            ra.connected = connectedA;
                            rb.connected = connectedB;
                            tunnels.clear();
                            break;
                        }
                        rooms.add(c);
                        tunnels.add(c);
                        prev = c;

                    }
                    if(!tunnels.isEmpty()){
                        web.addAll(tunnels);
                        //rooms.addAll(tunnels);
                    }
                    */
                }
            }
        }

        if (shop != null) {
            float angle;
            int tries = 10;
            do {
                angle = placeRoom(rooms, entrance, shop, Random.Float(360f));
                tries--;
            } while (angle == -1 && tries >= 0);
            if (angle == -1) return null;
        }

        ArrayList<Room> branchable = new ArrayList<>(web);

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
