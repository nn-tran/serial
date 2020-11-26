import java.io.Serializable;

public class RefObject implements Serializable{
	int value;
	RefObject child, parent;
	
	
	public RefObject(){
		value = 0;
	}

	public RefObject(int v){
		setValue(v);
	}

	public RefObject getParent() {
		return parent;
	}
	
	public void setParent(RefObject parent) {
		this.parent = parent;
	}
	
	public void setChild(RefObject child) {
		this.child = child;
	}


	public void setValue(int value) {
		this.value = value;
	}
	
}
