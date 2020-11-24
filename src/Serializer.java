

import java.util.*;
import javax.json.*;
import java.io.*;
import java.net.*;

/**
 * CPSC 501 
 * .json serializer
 *
 * @author Nguyen Tran, based on code by Jonathan Hudson
 */
public class Serializer {

    public static JsonObject serializeObject(Object object) throws Exception {
        JsonArrayBuilder object_list = Json.createArrayBuilder();
        serializeHelper(object, object_list, new IdentityHashMap<Object, String>());
        JsonObjectBuilder json_base_object = Json.createObjectBuilder();
        json_base_object.add("objects", object_list);
        return json_base_object.build();
    }

    private static void serializeHelper(Object source, JsonArrayBuilder object_list, Map<Object, String> object_tracking_map) throws Exception {
        String object_id = Integer.toString(object_tracking_map.size());
        object_tracking_map.put(source, object_id);
        Class object_class = source.getClass();
        JsonObjectBuilder object_info = Json.createObjectBuilder();
        object_info.add("class", object_class.getName());
        object_info.add("id", object_id);
        object_list.add(object_info);
    }
    
    private static Object[] createObj() {
    	System.out.println("Choose option 1-5 (refer to assignment description):");
    	Scanner sc = new Scanner(System.in);
    	int mode = sc.nextInt();
    	Object[] output;
    	switch (mode) {
	    	case 1:
	    		System.out.println("Simple object, enter bool, int, double:");
	    		boolean b = sc.nextBoolean();
	    		int i = sc.nextInt();
	    		double d = sc.nextDouble();
	    		Object simObj = new SimpleObject(b,i,d);
	    		output = new Object[1];
	    		output[0] = simObj;
	    		break;
	    	case 2:
	    		System.out.println("Object with reference, enter 2 integers for 2 objects:");
	    		Object ref1 = new RefObject(sc.nextInt());
	    		Object ref2 = new RefObject(sc.nextInt());
	    		((RefObject) ref1).setParent((RefObject) ref2);
	    		((RefObject) ref2).setParent((RefObject) ref2);
	    		output = new Object[2];
	    		output[0] = ref1;
	    		output[1] = ref2;
	    		break;
	    	case 3: 
	    		System.out.println("Object with array, enter array length:");
	    		break;
	    	case 4: 
	    		System.out.println("Object with array of references, enter array length:");
	    		break;
	    	case 5: 
	    		System.out.println("Object with collection, enter collection sizes");
	    		break;
    	
    		
    	}
    	
    	
    	return null;
    } 
    
    public static void main(String[] args) {  
    	try{      
    		ServerSocket ss = new ServerSocket(8192);
    		Socket s = ss.accept();//establishes connection   
	    	DataOutputStream dout = new DataOutputStream(s.getOutputStream());
	    	dout.writeUTF("Hello Client");
	    	dout.flush();
	    	dout.close();
	    	s.close();
	    	ss.close();
    	} catch (Exception e){System.out.println(e);}  
    }  
}