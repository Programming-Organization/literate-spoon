package engine.things;

import java.util.ArrayList;

import engine.Room;
import engine.things.Object;

public class Player {
	private int[] pos;
	public Room currentRoom;
	public ArrayList<Effect> effects = new ArrayList<Effect>();
	public ArrayList<Object> inventory = new ArrayList<Object>();
	public int health;
	public int maxHealth = 0;
	public int hunger = 0;
	public int thirst = 0;
	public int strength = 5;
	public int intelligence = 10;
	public int agility = 10;
	public int charisma = 10;
	public Object rightHand;

	public Player(int x, int y, int z) {
		pos = new int[3];
		pos[0] = x;
		pos[1] = y;
		pos[2] = z;
	}

	public void changePos(String amt) {// amt example in Direction class
		pos[0] += Integer.parseInt(amt.substring(0, 1)) - 1;
		pos[1] += Integer.parseInt(amt.substring(1, 2)) - 1;
		pos[2] += Integer.parseInt(amt.substring(2)) - 1;
	}

	public void setHealth(int health) {
		this.health = health;
		this.maxHealth = health > maxHealth ? health : maxHealth;
	}

	public String toString() {
		return pos[0] + ", " + pos[1] + ", " + pos[2];
	}
}
