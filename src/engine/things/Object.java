package engine.things;

public class Object {
	public String accessor;
	public String compSub = "";
	public String description;
	public Integer consumability = -5;

	public Object(String compSub, String description) {
		this.accessor = compSub.substring(compSub.indexOf("[") + 1, compSub.indexOf("]"));
		this.compSub = compSub.replace("[", "").replace("]", "");
		this.description = description;
	}
}
