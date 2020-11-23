package serial;

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
    	System.out.println("Number of objects:");
    	Scanner sc = new Scanner(System.in);
    	int num = sc.nextInt();
    	for (int i = 0;i < num;++i) {
    		System.out.printf("Creating object %d:\n", i);
    		
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