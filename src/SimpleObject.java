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

	public void setAlarm() {
		sleeping = true;
		hour = 8;
		timer = 6.0;
	}
	
	public void setAlarm(int n) {
		sleeping = true;
		hour = n;
		timer = 0.0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) { 
            return true; 
        } 
        if (!(o instanceof SimpleObject)) { 
            return false; 
        } 
          
        SimpleObject sim = (SimpleObject) o; 
          
        // Compare the data members and return accordingly  
        return (sim.hour == this.hour);
	}
}
