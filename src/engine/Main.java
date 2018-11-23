package engine;

import engine.words.Direction;
import engine.words.Verb;
import engine.words.Word;
import engine.things.Effect;
import engine.things.Entity;
import engine.things.Object;

import java.io.File;

import engine.Terminal;

public class Main {

	public static Engine game;

	public static void main(String args[]) {
		game = new Engine();

		game.addWord(new Verb("move go walk run climb jog travel journey venture", (Word w, Engine t) -> {

		
		File f = new File("src/engine/rooms.txt");
		game.readFile(f);
		
		
		game.addWord(new Verb("move go walk run climb jog strut", (Word w, Engine t) -> {

			if (w.getClass() != Direction.class) {
				Terminal.println("Please specify a direction");
				return;
			}

			t.protag.changePos(w.value);
		}, null));
		game.addWord(new Verb("eat consume", null, (Object o, Engine t) -> {
			t.protag.hunger -= o.consumability;
			if (o.consumability < 0) {
				t.protag.effects.add(new Effect((p) -> {
					t.protag.health += o.consumability * 2;
				}, 3, "That was painful to eat."));
			} else {
				Terminal.println("You ate a " + o.accessor + ". Delicious.");
			}
			t.protag.currentRoom.objects.remove(o);
			try {
				o.referencer.reference.compSub = t.lRandOf(new String[] { "floor", "ground" });
				o.referencer.description = o.referencer.description.replace(" a", " the");
			} catch (Exception e) {

			}
		}));
		game.addWord(new Verb("inspect investigate examine scrutinize study observe", null, (Object o, Engine t) -> {
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
		}));
		game.addWord(new Verb("interact", null, (Object o, Engine t) -> {
			if(o.alive) {
				Entity e = (Entity)o;
				e.interaction.accept(t.protag, t);
			}
		}));
		game.addWord(new Verb("attack assault assail punch hit kick pummel strike", null, (Object o, Engine t) -> {
			o.health -= t.protag.strength;
			Terminal.println("You attacked the " + o.accessor + ".");
		}));
		game.addWord(new Verb("take steal grab seize apprehend liberate", null, (Object o, Engine t) -> {
			boolean b = o.holdable;
			t.protag.inventory.add(o);
			t.protag.currentRoom.objects.remove(o);
			Terminal.println("You took the " + o.accessor + ".");
		}));


		game.addWord(new Verb("open check", (Word n, Engine t) -> {
			if (n.represents == t.protag.inventory) {
				if (t.protag.inventory.isEmpty()) {
					Terminal.print("You have nothing in your inventory");
				} else {
					Terminal.print("You have a " + t.protag.inventory.get(0).compSub);
					if (t.protag.inventory.size() == 2) {
						Terminal.print(" as well as a " + t.protag.inventory.get(1).compSub);
					} else if(t.protag.inventory.size() > 2){
						for (int i = 1; i < t.protag.inventory.size() - 1; i++) {
							Terminal.print(", a ");
							Terminal.print(t.protag.inventory.get(i).compSub);
						}
						Terminal.print(", and a " + t.protag.inventory.get(t.protag.inventory.size() - 1).compSub);
					}
				}
				Terminal.println(".");
			}
		}, null));

		game.addWord(new Word("inventory", game.protag.inventory));

		game.addWord(new Direction("north forwards", "12"));
		game.addWord(new Direction("south backwards", "10"));
		game.addWord(new Direction("east right", "21"));
		game.addWord(new Direction("west left", "01"));

		while (true) {
			game.update();
		}

	}
}
