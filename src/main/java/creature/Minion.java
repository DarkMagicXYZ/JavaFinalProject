package creature;
import attribute.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.*;
public class Minion extends Creature {
	public Minion(Battlefield bf){
		name = "Minion";
		field = bf;
		image = new Image("Minion.png");
		imageview = new ImageView();
		imageview.setImage(image);
	}
	public void fightWith(Creature c, double rand) {
		if(rand <= Attributes.BAD_ALIVE_RATE) {
			c.setDeath();
			System.out.print(this.getName() + " kill " + c.getName() + "   ");
			System.out.print(c.getName() + "isAlive:" + c.isAlive + "   \n");
			field.disappearCreature(c);
		}else {
			this.setDeath();
			System.out.print(c.getName() + " kill " + this.getName() + "   ");
			System.out.print(this.getName() + "isAlive:" + this.isAlive + "   \n");
			field.disappearCreature(this);
		}
	}
}
