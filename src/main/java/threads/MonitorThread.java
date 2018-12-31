package threads;
import main.*;

public class MonitorThread extends Thread {
	Battlefield field;
	public MonitorThread(Battlefield bf){
		field = bf;
	}
	@Override
	public void run(){
		try{Thread.sleep(1000);}catch(Exception e) {}
		field.guiRefresh();
		field.recordEveryRound();
		while(!field.isEnd()) {
			//刷新时间700ms
			try{Thread.sleep(700);}catch(Exception e) {}
			boolean temp = field.checkRoundEnd();
			if (temp) {
			field.recordEveryRound();
			}
			
		}
		field.writeRecord();
		field.guiEnd();
	}
}
