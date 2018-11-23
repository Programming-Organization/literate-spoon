package engine.words;

import java.util.*;

import engine.Engine;
import engine.things.Object;

public abstract class Word {
	public ArrayList<String> synonyms;

	public String value; // for use of verbs

	public Word() {
		synonyms = new ArrayList<String>();
	}

	public Word(String list) {
		synonyms = new ArrayList<String>();
		addSynonyms(list);
	}

	public void perform(Word w, Engine t) {
		System.out.println(this.getClass());
	}

	public void perform(Object o, Engine t) {
		System.out.println(this.getClass());
	}

	public void addSynonyms(String list) {
		String[] s = list.split(" ");
		for (String str : s) {
			synonyms.add(str);
		}
	}

	public boolean checkWord(String word) {
		if (synonyms.contains(word)) {
			return true;
		}
		return false;
	}
}
