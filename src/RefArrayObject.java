import java.io.Serializable;

public class RefArrayObject implements Serializable {
	SimpleObject[] simArray;
	
	public RefArrayObject(){
		simArray = null;
	}
	
	public RefArrayObject(int len){
		simArray = new SimpleObject[len];
		for (SimpleObject sim : simArray) {
			sim.setAlarm(len);
		}
	}

}
