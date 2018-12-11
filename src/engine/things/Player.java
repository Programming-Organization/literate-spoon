package engine.things;

import java.util.ArrayList;

import engine.Room;
import engine.things.Object;

public class Player extends Object {
	//private int[] pos;
	public Room currentRoom;
	public ArrayList<Effect> effects = new ArrayList<Effect>();
	public ArrayList<Object> inventory = new ArrayList<Object>();
	public int hunger = 0;
	public int thirst = 0;
	public int strength = 5;
	public int intelligence = 10;
	public int agility = 10;
	public int charisma = 10;
	public int literacy = 0;
	public Object rightHand;
	public Object weapon;
	public Object fist = new Object("[fist]", (String) null, null);

	public Player(int x, int y) {
		super("[player]", (String)null, null);
		
		fist.abstractNoun();
		inventory.add(fist);
		weapon = fist;
		
		Object o = new Object("[foot]", (String) null, null);
		o.abstractNoun();
		inventory.add(o);
		
		o = new Object("[head]", (String) null, null);
		o.playerDamage = 2;
		o.abstractNoun();
		inventory.add(o);
		
		alive = true;
	}
}
