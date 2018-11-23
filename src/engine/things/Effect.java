package engine.things;

import engine.OneParamFunction;
import engine.Terminal;

public class Effect {
	public OneParamFunction<Player> f;
	public int lifetime;

	public Effect(OneParamFunction<Player> f, int lifetime, String init) {
		this.f = f;
		this.lifetime = lifetime;
		Terminal.println(init);
	}

	public void affect(Player p) {
		f.accept(p);
		lifetime--;
	}
}
