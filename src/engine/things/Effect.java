package engine.things;

import engine.OneParamFunc;
import engine.Terminal;

public class Effect {
	public OneParamFunc<Player> f;
	public int lifetime;

	public Effect(OneParamFunc<Player> f, int lifetime, String init) {
		this.f = f;
		this.lifetime = lifetime;
		Terminal.println(init);
	}

	public void affect(Player p) {
		f.accept(p);
		lifetime--;
	}
}
