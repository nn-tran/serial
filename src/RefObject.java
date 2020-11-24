import java.io.Serializable;

public class RefObject implements Serializable{
	int value;
	RefObject parent;
	
	public RefObject(){
		value = 0;
	}

	public RefObject(int v){
		setValue(v);
	}
	
	public void setParent(RefObject parent) {
		this.parent = parent;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
