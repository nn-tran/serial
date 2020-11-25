import java.io.Serializable;
import java.util.ArrayList;

public class CollectionObject implements Serializable{
	ArrayList<SimpleObject> simList;
	
	public CollectionObject(int size) {
		simList = new ArrayList<SimpleObject>();
		for (int i = 0; i < size; ++i) {
			SimpleObject sim = new SimpleObject(false, i, 5.0);
			simList.add(sim);
		}
	}

}
