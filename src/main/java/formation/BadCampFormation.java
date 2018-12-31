package formation;


public class BadCampFormation extends Formation {
	public BadCampFormation(){
		name = "BadCamp";
		array = new Position[] {new Position(5,3),
				new Position(6,2),new Position(7,1),new Position(8,0),
				new Position(6,4),new Position(7,5),new Position(8,6)};
	}
}
