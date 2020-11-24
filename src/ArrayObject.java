import java.io.Serializable;

public class ArrayObject implements Serializable{
	boolean victory;
	int[] scoreboard;
	
	public ArrayObject() {
		victory = false;
		scoreboard = new int[10];
		for (int i = 0; i < 10; ++i) {
			scoreboard[i]=i;
		}
	}

}
