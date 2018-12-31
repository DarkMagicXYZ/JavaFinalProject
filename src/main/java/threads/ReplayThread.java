package threads;
import main.*;

public class ReplayThread extends Thread {
	Battlefield field;
	public ReplayThread(Battlefield bf){
		field = bf;
	}
	@Override
	public void run(){
		try{Thread.sleep(500);}catch(Exception e) {}
		int i = 0;
		while(i < field.replaydata.size() / 15) {
			//刷新时间700ms
			try{Thread.sleep(700);}catch(Exception e) {}
			field.replay(field.replaydata, i);
			i++;
		}
		field.guiEnd();
	}
}
