package main;

import creature.*;
import formation.*;

public class Unit {
	private Position position;
	public Creature creature = null;
	public Unit(Position p){
		position = p;
	}
	public void setCreature(Creature c) {
		creature = c;
	}
	public Creature getCreature() {
		return creature;
	}
	public final Position getPosition() {
		return position;
	}
	public void clearCreature() {
		creature = null;
	}
	public boolean isFilled() {
		if (creature == null) {
			return false;
		}else {
			return true;
		}
	}
}
