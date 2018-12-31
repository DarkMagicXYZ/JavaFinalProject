package formation;

public abstract class Formation {
	String name;
	Position[] array;
	public String getName() {
		return name;
	}
	public int getLength() {
		return array.length;
	}
	public final Position[] getArray() {
		return array;
	}
}
