package creature;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.*;
import formation.*;
public abstract class Creature implements Runnable{
	String name;
	Image image;
	ImageView imageview;
	Battlefield field;
	boolean isAlive = true;
	public Position position;
	boolean isWait = false;
	public void setIsWait(boolean b) {
		isWait = b;
	}
	public boolean getIsWait() {
		return isWait;
	}
	
	public String getName() {
		return name;
	}
	public Image getImage() {
		return image;
	}
	public void setPosition(Position p) {
		position = p;
	}
	public Position getPosition() {
		return position;
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setDeath() {
		isAlive = false;
		image = new Image("file:.\\src\\main\\resources\\death.png");
		imageview.setImage(image);
	}
	public void setAlive() {
		isAlive = true;
	}
	public abstract void fightWith(Creature c, double rand);
	@Override
	public void run() {
		//everybody sleep waiting for showing formation
		try{Thread.sleep(1500);}catch(Exception e) {}
		//System.out.println("run first " + this.getName() + "  " +this.isAlive);
		//Thread.yield();
		try{Thread.sleep(10);}catch(Exception e) {}
		try {
			field.fight(this);
		}catch(NullPointerException e) {
		}
		field.move(this);
		//Thread.yield();
		try{Thread.sleep(10);}catch(Exception e) {}
		while(!field.isEnd()) {
			if (isAlive) {
				try{Thread.sleep(10);}catch(Exception e) {}
				//Thread.yield();
				try {
					field.fight(this);
				}catch(NullPointerException e) {
				}
				field.move(this);
				//Thread.yield();
				try{Thread.sleep(10);}catch(Exception e) {}
			}else {
				break;
			}
			synchronized (this){
				try{
					isWait = true;
					this.wait();
					}catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		System.out.println("run last " + this.getName() + "  " +this.isAlive);
	};
}
