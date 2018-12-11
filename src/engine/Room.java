package engine;

import java.util.ArrayList;
import engine.things.Object;

public class Room {
	public int[] coords;// x and y
	public ArrayList<Object> objects = new ArrayList<Object>();
	public ArrayList<Room> nestedMap = new ArrayList<Room>();
	public Room westEntry;
	public Room eastEntry;
	public Room northEntry;
	public Room southEntry;
	public Room fatherRoom = null;
	
	
	Object floor = new Object("the [floor]", (String)null, null);	
	//Object floor = new Object("the [ground]", (String)null, null);
	
	public String description;
	
	public Room() {
		coords = new int[2];
	}

	public Room(int x, int y, String description) {
		coords = new int[2];
		coords[0] = x;
		coords[1] = y;
		floor.abstractNoun();
		objects.add(floor);
		this.description = description;
	}
	
	public void addObject(Object o) {
		objects.add(o);
	}
	
	public void addRoom(Room r) {
		r.fatherRoom = this;
		nestedMap.add(r);
	}
	
	public void setEntries() {
		Room farthest = nestedMap.get(0);
		
		for (Room r: nestedMap) {
			if (r.coords[0] < farthest.coords[0])
				farthest = r;
		}
		westEntry = farthest;
		
		for (Room r: nestedMap) {
			if (r.coords[0] > farthest.coords[0])
				farthest = r;
		}
		eastEntry = farthest;
		
		for (Room r: nestedMap) {
			if (r.coords[1] < farthest.coords[1])
				farthest = r;
		}
		southEntry = farthest;
		
		for (Room r: nestedMap) {
			if (r.coords[1] > farthest.coords[1])
				farthest = r;
		}
		northEntry = farthest;
	}

	public String toString() {
		return coords[0] + ", " + coords[1];
	}
}
