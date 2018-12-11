package engine;

import engine.words.Direction;
import engine.words.Verb;
import engine.words.Word;
import engine.things.Effect;
import engine.things.Entity;
import engine.things.Object;

import java.util.ArrayList;

import engine.Terminal;

public class Main {

	public static Engine game;

	public static void main(String args[]) {

		game = new Engine();

		game.addWord(new Verb("move go walk run climb jog travel journey venture", (Word w, Engine t) -> { //begin lambda
			if (w.getClass() != Direction.class) {
				Terminal.println("...What?");
				return;
			}

			int x, y;

			int dx = Integer.parseInt(w.value.substring(0, 1)) - 1;
			int dy = Integer.parseInt(w.value.substring(1, 2)) - 1;

			Room currentRoom = t.protag.currentRoom;

			while (true) {//recursion without recursion
				x = t.protag.currentRoom.coords[0];
				y = t.protag.currentRoom.coords[1];

				x += dx;
				y += dy;

				for (Room r: t.protag.currentRoom.fatherRoom.nestedMap) {
					if (x == r.coords[0] && y == r.coords[1]) {
						t.protag.currentRoom = r;

						if (dx > 0) {//flipped from how you'd think
							while (t.protag.currentRoom.westEntry != null)
								t.protag.currentRoom = t.protag.currentRoom.westEntry;
						} else if (dx < 0) {
							while (t.protag.currentRoom.eastEntry != null)
								t.protag.currentRoom = t.protag.currentRoom.eastEntry;
						} else if (dy > 0) {
							while (t.protag.currentRoom.southEntry != null)
								t.protag.currentRoom = t.protag.currentRoom.southEntry;
						} else if (dy < 0) {
							while (t.protag.currentRoom.northEntry != null)
								t.protag.currentRoom = t.protag.currentRoom.northEntry;
						}

						t.changedSurroundings = true;
						return;
					}
				}

				if (t.protag.currentRoom.fatherRoom.fatherRoom != null)//main map only has 1 room, so can't be there either
					t.protag.currentRoom = t.protag.currentRoom.fatherRoom;
				else {
					break;
				}
			}
			t.protag.currentRoom = currentRoom;
			Terminal.println("You can't move that way.");

			//t.protag.changePos(w.value);
		}, null)); //end lambda

		game.addWord(new Verb("eat consume", null, (Object o, Engine t) -> { //begin lambda
			if(o.abstractObj) {
				Terminal.println("Impossible.");
				return;
			}

			if(!o.alive) {
				t.protag.hunger -= o.consumability;

				if (o.drinkability != null) {
					t.protag.thirst -= o.drinkability;
				}

				if (o.consumability < 0) {
					if(o.poisonous) {
						t.protag.effects.add(new Effect((p) -> {
							t.protag.health--;
						}, 30, "That was painful to eat."));
					} else {
						t.protag.effects.add(new Effect((p) -> {//lambda
							t.protag.health += o.consumability * 2;
						}, 3, "That was painful to eat."));//end lambda
					}
				} else {
					Terminal.println("You ate the " + o.accessor + ". Delicious.");
				}
				t.protag.currentRoom.objects.remove(o);
				t.protag.inventory.remove(o);
				removal(o, t);
			} else {
				boolean b = (Boolean) null;
			}
		})); //end lambda
		game.addWord(new Verb("inspect investigate examine scrutinize study observe look search", (Word w, Engine t) -> { //lambda
			if (w.represents == t.protag.inventory) {
				Terminal.println("Try checking your inventory instead.");
				return;
			} else if (w.represents == t.protag.currentRoom) {
				t.inspectRoom();
			}


		}, (Object o, Engine t) -> { //beginning ended lambda. Ending of line == new lambda
			if (o.container.isEmpty()) {
				Terminal.print(t.uRandOf(new String[] { "Upon inspection, you realize that " + o.inspection,
						"It looks like " + o.inspection, "You now can see that " + o.inspection }));
			} else {
				Terminal.print(t.uRandOf(
							new String[] { "Upon inspection, you observe that there is a " + o.container.get(0).compSub,
									"It looks like there is a " + o.container.get(0).compSub,
									"You now can see that there is a " + o.container.get(0).compSub }));
				if (o.container.size() == 2) {
					Terminal.print(" as well as a " + o.container.get(1).compSub);
				} else if(o.container.size() > 2){
					for (int i = 1; i < o.container.size() - 1; i++) {
						Terminal.print(", a ");
						Terminal.print(o.container.get(i).compSub);
					}
					Terminal.print(", and a " + o.container.get(o.container.size() - 1).compSub);
				}
				Terminal.print(" inside the " + o.accessor);
			}
			Terminal.println(".");
		})); //end lambda

		game.addWord(new Verb("interact talk speak converse negotiate chat gossip approach apprehend", null, (Object o, Engine t) -> {
			if(o.alive) {
				Entity e = (Entity)o;
				e.interaction.accept(t.protag, t);
			}
		})); //just a lambda block

		game.addWord(new Verb("attack assault assail hit pummel strike kill destroy ", null, (Object o, Engine t) -> { //begin lambda
			if (o.equals(t.protag)) {
				t.protag.health = 0;
				Terminal.println("You killed yourself." + t.uRandOf(new String[] { "Fantastic work.",
						"What a glorious day.", "I hope it hurt.", "Great job.", "Nice going.", "I hope you meant to do that."}));
				return;
			}

			o.health -= t.protag.strength * t.protag.weapon.damage;
			t.protag.health -= t.protag.strength * t.protag.weapon.playerDamage;

			if (!t.protag.weapon.abstractObj)
				t.protag.weapon.health -= t.protag.strength;

			if(o.alive) {
				try {
				Entity e = (Entity) o;
				if(e.anger < e.restraint) e.anger = e.restraint;
				} catch(Exception e) {};
			}
			//Terminal.println("You attacked the " + o.accessor + " with the " + t.protag.weapon.accessor + ".");
			Terminal.println("Weapon: " + o.accessor);
		}, (Object o1, Object with, Engine t) -> {//end lambda and begin another
			t.protag.weapon = with;

			if (o1.equals(t.protag)) {
				t.protag.health = 0;
				Terminal.println("You killed yourself." + t.uRandOf(new String[] { "Fantastic work.",
						"What a glorious day.", "I hope it hurt.", "Great job.", "Nice going.", "I hope you meant to do that."}));
				return;
			}

			o1.health -= t.protag.strength * t.protag.weapon.damage;
			t.protag.health -= t.protag.strength * t.protag.weapon.playerDamage;

			if (!t.protag.weapon.abstractObj)
				t.protag.weapon.health -= t.protag.strength;

			if(o1.alive) {
				try {
				Entity e = (Entity) o1;
				if(e.anger < e.restraint) e.anger = e.restraint;
				} catch(Exception e) {};
			}
			//Terminal.println("You attacked the " + o1.accessor + " with the " + t.protag.weapon.accessor + ".");
			Terminal.println("Weapon: " + with.accessor);
		})); // end lambda

		game.addWord(new Verb("hold", null, (Object o, Engine t) -> {
			boolean b = o.holdable;
			if(!t.protag.inventory.contains(o)) {
				t.protag.rightHand = o;
				t.protag.inventory.add(o);
				t.protag.currentRoom.objects.remove(o);
				Terminal.println("You are now holding a " + o.accessor + ".");
			} else {
				b = (Boolean)null;
			}
		})); // yet another lambda block jesus christ

		game.addWord(new Verb("take get steal grab seize apprehend liberate collect pick snag pilfer aquire", null, (Object o, Engine t) -> {
			boolean b = o.holdable;
			if(o.alive) {
				b = (Boolean)null;
			}
			if(!t.protag.inventory.contains(o)) {
				t.protag.inventory.add(o);
				t.protag.currentRoom.objects.remove(o);
				removal(o, t);
				Terminal.println("You took the " + o.accessor + ".");
			} else {
				b = (Boolean)null;
			}
		})); // another lambda for god's sake

		game.addWord(new Verb("drop leave put", null, (Object o, Engine t) -> {
			if(o.abstractObj) {
				Terminal.println("Impossible.");
				return;
			}

			if(t.protag.inventory.contains(o)) {
				t.protag.inventory.remove(o);
				o.description = "on";
				o.reference = t.protag.currentRoom.floor;

				if (t.protag.rightHand.equals(o))
					t.protag.rightHand = t.protag.fist;

				t.protag.currentRoom.objects.add(o);
				Terminal.println("You dropped the " + o.accessor + ".");
			} else {
				Terminal.println("You don't have a " + o.accessor + " to drop.");
			}
		})); // honestly, what god can there even be anymore


		game.addWord(new Verb("view open check", (Word n, Engine t) -> {
			if (n.represents == t.protag.inventory) {
				ArrayList<Object> realStuff = (ArrayList<Object>) t.protag.inventory.clone();
				for (int i = 0; i < realStuff.size(); i++) {
					if (realStuff.get(i).abstractObj) {
						realStuff.remove(i);
						i--;
					}
				}

				if (realStuff.isEmpty()) {
					Terminal.print("You have nothing in your inventory");
				} else {
					Terminal.print("You have a " + realStuff.get(0).compSub);
					if (realStuff.size() == 2) {
						Terminal.print(" as well as a " + realStuff.get(1).compSub);
					} else if(realStuff.size() > 2){
						for (int i = 1; i < realStuff.size() - 1; i++) {
							Terminal.print(", a ");
							Terminal.print(realStuff.get(i).compSub);
						}
						Terminal.print(", and a " + realStuff.get(realStuff.size() - 1).compSub);
					}
				}
				Terminal.println(".");
			}
		}, null)); //the lambdas, like so many stars

		game.addWord(new Verb("read", null, (Object o, Engine t) -> {
			if (o.text.length() == 0) {
				Terminal.println("The " + o.accessor + " says nothing.");
				return;
			}
			String str = o.text;
			String finalStr = "";
			String word;
			int index = 0;
			while (index != -1) {
				if (str.substring(index).indexOf(' ') != -1)
					word = str.substring(index, str.substring(index).indexOf(' ') + index);
				else
					word = str.substring(index);

				if (word.length() > t.protag.literacy) {
					for (int i = 0; i < word.length(); i++) {
						if (word.charAt(i) != '\n')
						word = word.substring(0, i) + t.lRandOf(new String[] {"@", "#", "&", "/", "%", "$", "!", "*", "+"}) + word.substring(i+1);
					}
				}

				finalStr += word + " ";

				if (str.substring(index).indexOf(' ') != -1)
					index = str.substring(index).indexOf(' ') + index + 1;
				else
					break;
			}

			Terminal.println(finalStr + "\n");
			Terminal.println(t.protag.literacy < 1? "You can't read.":
				t.protag.literacy < 4? "You can barely read.":
					t.protag.literacy < 6? "You're reading skills are so-so.":
						t.protag.literacy < 8? "You can almost read perfectly.":
							"You're an amazing reader.");

		})); //they suffocate vision in breathtaking totality

		game.addWord(new Word("inventory", game.protag.inventory));
		game.addWord(new Word("self yourself me myself", game.protag));
		game.addWord(new Word("room area surroundings place around", game.protag.currentRoom));

		game.addWord(new Direction("north forwards", "12"));
		game.addWord(new Direction("south backwards", "10"));
		game.addWord(new Direction("east right", "21"));
		game.addWord(new Direction("west left", "01"));

		//and yet so quickly they are gone

		while (true) {
			game.update();
		}
	}
	public static void removal(Object o, Engine t) {
		try {
			o.referencer.reference = t.protag.currentRoom.floor;
			o.referencer.description = t.lRandOf(new String[] { "lying", "sitting", "resting" }) + " on";
		} catch (Exception e) {

		}
		try {
			o.reference.reference = t.protag.currentRoom.floor;
			o.reference.description = "on";
		} catch (Exception e) {

		}
	}
}
//Some say the world will end in fire, some say in ice.
//I say end it with a comment, and it's really just as nice.
//So farewell to you my friend, you who have ventured brave and bold
//through the convoluted forests of Aidan's story, in code, told.
//What you have read may yet escape you, as it once did for me,
//but rest assured, the holy Lambda is something men can learn to see.
//I rest my case with the finality of the ending close paren —
//And I swear I'll never, EVER set Aidan's lambdas free again.
// — Nico Mantione, 11 December 2018
