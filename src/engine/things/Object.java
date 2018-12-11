package engine.things;

import java.util.ArrayList;

public class Object {
	public boolean alive = false;
	public String accessor;
	public String compSub = "";
	public String description;
	public ArrayList<Object> container = new ArrayList<Object>();
	public String inspection;
	public String text = "";
	public Object reference;
	public Object referencer;
	public Integer consumability = -5;
	public Integer drinkability = null;
	public Boolean holdable = true;
	public Integer damage = 1;
	public Integer playerDamage = 0;
	public Integer health = 10;
	public Integer maxHealth = 10;
	public boolean abstractObj = false;
	public boolean poisonous = false;

	public enum type {
		crumples, shatters, squishes, bruises
	};

	public type injury;

	public Object(String compSub, String description, String inspection) {
		this.accessor = compSub.substring(compSub.indexOf("[") + 1, compSub.indexOf("]"));
		this.compSub = compSub.replace("[", "").replace("]", "");
		this.description = description;
		this.inspection = inspection != null ? inspection : "it's just a " + accessor;
	}

	public Object(String compSub, Object referenced, String inspection) {
		this.accessor = compSub.substring(compSub.indexOf("[") + 1, compSub.indexOf("]"));
		this.compSub = compSub.replace("[", "").replace("]", "");
		referencer = referenced;
		this.inspection = inspection != null ? inspection : "it's just a " + accessor;
	}

	public void setHealth(Integer i) {
		health = i;
		maxHealth = health;
	}

	public void abstractNoun() {
		setHealth(null);
		abstractObj = true;
		consumability = null;
		holdable = null;
		injury = null;
	}
}
