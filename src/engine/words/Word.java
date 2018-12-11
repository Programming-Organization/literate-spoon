package engine.words;

import java.util.*;

import engine.Engine;
import engine.things.Object;

public class Word {
	public ArrayList<String> synonyms;

	public String value; // for use of verbs
	public java.lang.Object represents;

	public Word() {
		synonyms = new ArrayList<String>();
	}

	public Word(String list) {
		synonyms = new ArrayList<String>();
		addSynonyms(list);
	}

	public Word(String list, java.lang.Object o) {
		synonyms = new ArrayList<String>();
		addSynonyms(list);
		represents = o;
	}

	public void perform(Word w, String prepUsed, Engine t) {
		System.out.println(this.getClass());
	}

	public void perform(Object o, String s, Engine t) {
		System.out.println(this.getClass());
	}
	
	public void perform(Object o, Object o2, String s1, String s2, Engine t) {
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
