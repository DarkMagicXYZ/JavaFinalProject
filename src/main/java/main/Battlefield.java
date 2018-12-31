package main;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import creature.*;
import threads.*;
import formation.*;

public class Battlefield {
	private Unit[][] units;
	private Image background = new Image("file:.\\src\\main\\resources\\Battlefield.png");
	private Image victory = new Image("file:.\\src\\main\\resources\\victory.png");
	//private Image death = new Image("file:death.png");
	private ArrayList<Thread> threads = new ArrayList<>();
	private ArrayList<Creature> goodguys = new ArrayList<>();
	private ArrayList<Creature> badguys = new ArrayList<>();
	private Thread monitor;
	private Thread replay;
	public MyCanvas can = new MyCanvas();
	private int round = 0;
	private int width = 9;
	private int height = 7;
	public ArrayList<String> replaydata = new ArrayList<>();
	public ArrayList<ArrayList<String>> record = new ArrayList<>();
	//private String ;
	private IOController ioc = new IOController();
	private Stage stage;
	private boolean isStart = false;
	public Battlefield() {
		units = new Unit[width][height];
		unitsInit();
		guysInit();
		guiInit();
		threadAdd();
	}
	public void unitsInit() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				units[j][i] = new Unit(new Position(j,i));
			}
		}
	}
	public void guysInit() {
		goodguys = new ArrayList<>();
		badguys = new ArrayList<>();
		goodguys.add(new Grandpa(this));
		for (int i=0; i<7; i++) {
			goodguys.add(new CalabashBrother(i,this));
		}
		badguys.add(new Scorpion(this));
		badguys.add(new Serpent(this));
		for (int i=0; i<5; i++) {
			badguys.add(new Minion(this));
		}
		setGuys(badguys, new BadCampFormation());
		setGuys(goodguys, new GoodCampFormation());
	}
	public void guiInit() {
		can.gc.fillText("葫芦娃大战妖怪", 320, 250, 260);
		can.gc.fillText("Press SPACE to start", 280, 300, 340);
		can.gc.fillText("Press L to reload", 300, 350, 300);
		can.gc.fillText("Press ESC to quit", 300, 400, 300);
		can.addEventFilter(KeyEvent.KEY_PRESSED, new KeyEventHandler());
	}
	public void threadAdd() {
		for (Creature c : goodguys) {
			threads.add(new Thread(c));
		}
		for (Creature c : badguys) {
			threads.add(new Thread(c));
		}
		monitor = new MonitorThread(this);
	}
	public void threadStart() {
		isStart = true;
		monitor.start();
		for (Thread t : threads) {
			t.start();
		}
	}
	public void setGuys(ArrayList<Creature> guys, Formation f) {
		Position[] arr = f.getArray();
		for (int i=0; i<guys.size(); i++) {
			units[arr[i].x][arr[i].y].setCreature(guys.get(i));
			guys.get(i).position = new Position(arr[i].x, arr[i].y);
		}
	}
	
	public void guiRefresh() {
		can.gc.clearRect(0, 0, 900, 700);
		can.gc.drawImage(background, 0, 0);
		//避免遮挡问题
		for (Creature c : goodguys) {
			if(!c.isAlive()) {
				can.gc.drawImage(c.getImage(), 100 * c.position.x, 100 *c.position.y);
			}
		}
		for (Creature c : badguys) {
			if(!c.isAlive()) {
				can.gc.drawImage(c.getImage(), 100 * c.position.x, 100 *c.position.y);
			}
		}
		
		for (Creature c : goodguys) {
			if(c.isAlive()) {
				can.gc.drawImage(c.getImage(), 100 * c.position.x, 100 *c.position.y);
			}
		}
		for (Creature c : badguys) {
			if(c.isAlive()) {
				can.gc.drawImage(c.getImage(), 100 * c.position.x, 100 *c.position.y);
			}
		}
	}
    public void guiEnd() {
		can.gc.clearRect(0, 0, 900, 700);
		can.gc.drawImage(background, 0, 0);
		can.gc.drawImage(victory, 300, 200);
		can.gc.fillText("我们活了下来：", 320, 450, 260);
		can.gc.fillText("Press L to reload", 300, 500, 300);
		can.gc.fillText("Press ESC to quit", 300, 250, 300);
		int count = 0;
		for (Creature c : goodguys) {
			if(c.isAlive()) {
				can.gc.drawImage(c.getImage(), 100 * (count + 1), 500);
				count ++;
			}
		}
		for (Creature c : badguys) {
			if(c.isAlive()) {
				can.gc.drawImage(c.getImage(), 100 * (count + 1), 500);
				count ++;
			}
		}
		
	}
	
	public ArrayList<String> bufferReplay() {
		//for multiple replay
		guysInit();
		replay = new ReplayThread(this);
		can.gc.clearRect(0, 0, 900, 700);
		can.gc.drawImage(background, 0, 0);
		FileChooser fileChooser = new FileChooser();
		String path = System.getProperty("user.dir");
		fileChooser.setInitialDirectory(new File(path + File.separator + "record"));
		fileChooser.setTitle("Choose one to replay");
        File file = fileChooser.showOpenDialog(stage);
		ArrayList<String> temp = ioc.read(file);
		replaydata = temp;
		/*for (Creature c : goodguys) {
			c.setAlive();
		}
		for (Creature c : badguys) {
			c.setAlive();
		}*/
		isStart = true;
		replay.start();
        return temp;
	}
	public void recordEveryRound() {
		ArrayList<String> string = new ArrayList<>();
		for (Creature c : goodguys) {
			String str = "";
			str += String.valueOf(round) + "\t";
			str += String.valueOf(c.getPosition().x) + "\t";
			str += String.valueOf(c.getPosition().y) + "\t";
			str += String.valueOf(c.isAlive());
			string.add(str);
		}
		for (Creature c : badguys) {
			String str = "";
			str += String.valueOf(round) + "\t";
			str += String.valueOf(c.getPosition().x) + "\t";
			str += String.valueOf(c.getPosition().y) + "\t";
			str += String.valueOf(c.isAlive());
			string.add(str);
		}
		record.add(string);
	}
	public void writeRecord() {
		ioc.record(record);
	}
	public void replay(ArrayList<String> data, int index) {
		can.gc.clearRect(0, 0, 900, 700);
		can.gc.drawImage(background, 0, 0);
		int count = 0;
		for (Creature c : goodguys) {
			String subdata = data.get(index * 15 + count);
			String[] string = subdata.split("\t");
			//别问我为什么用string[3].length()，如果直接判断string[3] == "false"它一直就是false，我也很无奈啊
			if (string[3].length() == 5)
			{
				c.setDeath();
			}else {
				
			}
			c.setPosition(new Position(Integer.valueOf(string[1]), Integer.valueOf(string[2])));
			count++;
		}
		for (Creature c : badguys) {
			String subdata = data.get(index * 15 + count);
			String[] string = subdata.split("\t");
			if (string[3].length() == 5)
			{
				c.setDeath();
			}else {
				
			}
			c.setPosition(new Position(Integer.valueOf(string[1]), Integer.valueOf(string[2])));
			count++;
		}
		guiRefresh();
	}
	
	public void setEvent() {
		can.getParent().getScene().setOnKeyPressed(new KeyEventHandler());
	}
	
	public MyCanvas getMyCanvas() {
		return can;
	}
	public int getRound() {
		return round;
	}
	
	
	public synchronized boolean move(Creature c) {
		if (c.isAlive() == false)
		{
			//System.out.println(c.getName() + " isDead so he/she can't move");
			return false;
		}
		int x = c.getPosition().x;
		int y = c.getPosition().y;
		int new_x;
		int new_y;
		Random rand = new Random();
		ArrayList<Position> list = new ArrayList<>();
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++) {
				new_x = x + i;
				new_y = y + j;
				try {
					//默认不能不移动，不移动即返回false
					if (!units[new_x][new_y].isFilled()) {
						list.add(new Position(new_x,new_y));
					}
				}catch(Exception e)
				{
					
				}
			}
		}
		if (list.size() == 0) {
			System.out.println(c.getName() + " stay");
			return false;
		}else {
			int index = rand.nextInt(list.size());
			units[list.get(index).x][list.get(index).y].setCreature(c);
			c.position = new Position(list.get(index).x, list.get(index).y);
			units[x][y].clearCreature();
			System.out.println("move" + c.getName() + " from " + x + "," + y + " to " + list.get(index).x + "," + list.get(index).y);
			return true;
		}
			
	}
	public synchronized boolean fight(Creature c1) {
		if (c1.isAlive() == false)
		{
			//System.out.println(c1.getName() + " isDead so he/she can't fight");
			return false;
		}
		Random rand = new Random();
		c1.fightWith(this.getOneToFight(c1), rand.nextDouble());
		return true;
	}
	
	
	
	public synchronized boolean checkRoundEnd() {
		int count = 0;
		for (Creature c : goodguys) {
			if (c.getIsWait() == false && c.isAlive() == true) {
				count++;
			}
		}
		for (Creature c : badguys) {
			if (c.getIsWait() == false && c.isAlive() == true) {
				count++;
			}
		}
		if (count == 0) {
			guiRefresh();
			round++;
			System.out.println("Round:                           " + round);
			for (Creature c : goodguys) {
				c.setIsWait(false);
				//c.notifyAll();
			}
			for (Creature c : badguys) {
				c.setIsWait(false);
				//c.notifyAll();
			}
			for (Creature c : goodguys) {
				synchronized(c) {
					c.notify();
				}
				//c.notifyAll();
			}
			for (Creature c : badguys) {
				synchronized(c) {
					c.notify();
				}
			}
			//notifyAll();
			//System.out.println("After notifyall");
			return true;
		}
		return false;
	}
	
	public boolean isEnd() {
		if (isGoodDead() || isBadDead()) {
			return true;
		}
		return false;
	}
	public boolean isGoodDead() {
		for (Creature c : goodguys) {
			if(c.isAlive()) {
				return false;
			}
		}
		return true;
	}
	public boolean isBadDead() {
		for (Creature c : badguys) {
			if(c.isAlive()) {
				return false;
			}
		}
		return true;
	}
	
	public Creature getOneToFight(Creature c) {
		
		if(c.getClass() == Grandpa.class || c.getClass() == CalabashBrother.class) {
			for (Creature c2 : badguys) {
				if (reachCreature(c, c2) && c2.isAlive()) {
					//System.out.println("returning living creature" + c2.getName());
					return c2;
				}
			}
		}else if(c.getClass() == Minion.class || c.getClass() == Scorpion.class || c.getClass() == Serpent.class){
			for (Creature c2 : goodguys) {
				if (reachCreature(c, c2) && c2.isAlive()) {
					//System.out.println("returning living creature" + c2.getName());
					return c2;
				}
			}
		}else {
			System.out.println("No Creature return");
		}
		throw new NullPointerException();
		//return null;
		
	}
	
	
	public boolean reachCreature(Creature c1, Creature c2) {
		int dx = c2.getPosition().x - c1.getPosition().x;
		int dy = c2.getPosition().y - c1.getPosition().y;
		if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
			return true;
		}
		return false;
	}
	public void disappearCreature(Creature c) {
		int x = c.getPosition().x;
		int y = c.getPosition().y;
		units[x][y].clearCreature();
	}
	
	
	class KeyEventHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event){
			if(event.getCode() == KeyCode.SPACE){
				threadStart();
			}else if(event.getCode() == KeyCode.L && (isStart == false || isEnd() == true) ) {
				bufferReplay();
			}else if(event.getCode() == KeyCode.ESCAPE) {
				System.exit(0);
			}else {
			}
	    }
	}
}
