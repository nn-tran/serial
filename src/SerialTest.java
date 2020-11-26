import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class SerialTest {
	
	
	@Test
	void test1() {
		InputStream stream1 = new ByteArrayInputStream("1\ntrue\n5\12.0".getBytes(StandardCharsets.UTF_8));
		Object obj = Serializer.createObj(stream1)[0];
		SimpleObject simObj = new SimpleObject(true, 5, 12.0);
		assertTrue(obj instanceof SimpleObject);
		assertTrue(simObj.equals(obj));
	}

	void test2() {
		InputStream stream1 = new ByteArrayInputStream("2\n100\n200".getBytes(StandardCharsets.UTF_8));
		Object[] objArr = Serializer.createObj(stream1);
		assertTrue(((RefObject) objArr[0]).getParent() == objArr[1]);
		assertTrue(((RefObject) objArr[1]).getParent() == objArr[0]);
	}
}
