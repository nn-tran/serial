import java.io.Serializable;

public class SimpleObject implements Serializable{
	boolean sleeping;
	int hour;
	double timer;
	
	public SimpleObject(){
		sleeping = false;
		hour = 1;
		timer = 12.0;
	}
	
	public SimpleObject(boolean s, int h, double t){
		sleeping = s;
		hour = h;
		timer = t;
	}

	public void setAlarm1() {
		sleeping = true;
		hour = 8;
		timer = 6.0;
	}
	
	public void setAlarm2() {
		sleeping = true;
		hour = 24;
		timer = 0.0;
	}
}
