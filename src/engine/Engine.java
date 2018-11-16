package engine;

import java.util.*;

import engine.things.Player;
import engine.things.Effect;
import engine.things.Object;
import engine.words.Verb;
import engine.words.Word;

import engine.Terminal;

public class Engine {
	public Player protag;

	public ArrayList<Room> rooms;// can be accessed by verbs
	private ArrayList<Word> vocabulary;
	Random rand = new Random();

	public Engine() {
		protag = new Player(0, 0, 0);
		protag.setHealth(100);

		rooms = new ArrayList<Room>();
		rooms.add(new Room(0, 0, 0));
		rooms.get(0).objects.add(new Object("red [brick]", "on a nice hand-knitted carpet"));
		rooms.get(0).objects.add(new Object("soft and tender [spider]", "on your face"));
		rooms.get(0).objects.add(Consumable("small [baby] wearing a wool sweater", "sitting on a chair", 10));
		vocabulary = new ArrayList<Word>();
	}

	public void addRoom(Room r) {
		rooms.add(r);
	}

	public void addWord(Word v) {
		vocabulary.add(v);
	}

	public Object Consumable(String accessor, String descriptor, int consumability) {
		Object o = new Object(accessor, descriptor);
		o.consumability = consumability;
		return o;
	}

	public String randOf(String[] s) {
		int x = rand.nextInt(s.length);
		return s[x].substring(0, 1).toUpperCase() + s[x].substring(1, s[x].length());
	}

	public void updatePlayerState() {
		if (protag.hunger++ > 50) {
			if (protag.hunger > 80) {
				Terminal.println(
						"Intuition tells you that if you had eaten something, perhaps you wouldn't be so hungry.");
			} else {
				Terminal.println("Intuition tells you that you might want to eat some food.");
			}
		}
		if (protag.thirst++ > 50) {
			if (protag.thirst > 80) {
				Terminal.println("Intuition tells you that your thirst cannot be quenched by drinking air.");
			} else {
				Terminal.println("Intuition tells you that you might want to drink something.");
			}
		}
		protag.health += protag.health < protag.maxHealth ? 1 : 0;
	}

	public void update() {
		Terminal.println(protag);

		String userText;
		ArrayList<String> words;

		while (true) {// repeats until valid command
			for (Room r : rooms) {
				if (r.toString().equals(protag.toString())) {
					protag.currentRoom = r;
				} else {
					System.out.println("Currently not in any room");
				}
			}
			Terminal.println(protag.health > 90 ? "You are feeling fine."
					: protag.health > 50 ? "You are feeling slightly injured."
							: protag.health > 0
									? "You think that you might have some injuries, but you've forgotten where."
									: "You feel slightly dead, but you aren't sure.");
			for (Object o : protag.currentRoom.objects) {
				Terminal.print(randOf(new String[] { "There is a " + o.compSub + " " + o.description,
						o.description + ", there is a " + o.compSub,
						"You notice a " + o.compSub + " " + o.description }));
				System.out.print(". ");
			}
			Terminal.print("\n");

			userText = Terminal.readln();
			userText = userText.toLowerCase();
			words = new ArrayList<String>();

			String[] s = userText.split(" ");
			for (String str : s) {
				words.add(str);// user text goes to array of words
			}
			if (words.size() != 2) {
				Terminal.println("All commands must be 2 words");
				continue;
			}

			Word w0 = null;
			Word w1 = null;
			Object o1 = null;
			boolean found = false;
			boolean foundObject = false;

			for (Word w : vocabulary) {
				if (w.checkWord(words.get(0))) {
					w0 = w;
					found = true;
				}
			} // finds word in array

			if (!found) {
				Terminal.println("I don't know what '" + words.get(0) + "' means.");
				continue;
			}

			if (w0.getClass() != Verb.class) {
				Terminal.println("Commands always start with a verb.");
				continue;
			}

			found = false;

			for (Word w : vocabulary) {
				if (w.checkWord(words.get(1))) {
					w1 = w;
					found = true;
				}
			}

			for (Object o : protag.currentRoom.objects) {
				if (o.accessor.equals(words.get(1))) {
					o1 = o;
					foundObject = true;
				}
			}

			if (!found && !foundObject) {
				Terminal.println("I don't know what '" + words.get(1) + "' means.");
				continue;
			}

			if (found && !foundObject) {
				if (w1.getClass() == Verb.class) {
					Terminal.println("Commands never end with a verb.");
					continue;
				}
			}

			if (found) {
				w0.perform(w1, this);// fills out word's function
			} else if (foundObject) {
				w0.perform(o1, this);
			}

			updatePlayerState();
			Iterator<Effect> it = protag.effects.iterator();
			while (it.hasNext()) {
				Effect e = it.next();
				e.affect(protag);
				if (e.lifetime == 0) {
					it.remove();
				}
			}
			break;
		}
	}
}
