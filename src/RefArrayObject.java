import java.io.Serializable;

public class RefArrayObject implements Serializable {
	public SimpleObject[] simArray;
	
	public RefArrayObject(int len){
		simArray = new SimpleObject[len];
		for (SimpleObject sim : simArray) {
			
			sim = new SimpleObject();
			
		}
	}

	public SimpleObject[] getSimArray() {
		return simArray;
	}

	public void setSimArray(SimpleObject[] simArray) {
		this.simArray = simArray;
	}

	public void setArrayElement(SimpleObject newElement, int index) {
		simArray[index] = newElement;
	}
}
