package creature;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.*;
import attribute.*;
public class CalabashBrother extends Creature {
	private String color;
	private int index;
	public CalabashBrother(int i, Battlefield bf) {
		index = i;
		field = bf;
		switch (i) {
		case 0:
			name = "老大";
			color = "红娃";
			image = new Image("file:.\\src\\main\\resources\\大娃.png");
			break;
		case 1:
			name = "老二";
			color = "橙娃";
			image = new Image("file:.\\src\\main\\resources\\二娃.png");
			break;
		case 2:
			name = "老三";
			color = "黄娃";
			image = new Image("file:.\\src\\main\\resources\\三娃.png");
			break;
		case 3:
			name = "老四";
			color = "绿娃";
			image = new Image("file:.\\src\\main\\resources\\四娃.png");
			break;
		case 4:
			name = "老五";
			color = "青娃";
			image = new Image("file:.\\src\\main\\resources\\五娃.png");
			break;
		case 5:
			name = "老六";
			color = "蓝娃";
			image = new Image("file:.\\src\\main\\resources\\六娃.png");
			break;
		case 6:
			name = "老七";
			color = "紫娃";
			image = new Image("file:.\\src\\main\\resources\\七娃.png");
			break;
		default:
			throw new IndexOutOfBoundsException("You are trying to build calabashbrother which is not existing");
		}
		imageview = new ImageView();
		imageview.setImage(image);
	}
	public int getIndex() {
		return index;
	}
	public String getColor() {
		return color;
	}
	public void fightWith(Creature c, double rand) {
		if(rand <= Attributes.GOOD_ALIVE_RATE) {
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
