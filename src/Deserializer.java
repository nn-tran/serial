package serial;

import java.lang.reflect.*;
import java.util.*;
import javax.json.*;
import java.io.*;
import java.net.*;

/**
 * CPSC 501 
 * .json deserializer
 *
 * @author Nguyen Tran, based on code by Jonathan Hudson
 */
public class Deserializer {

    public static Object deserializeObject(JsonObject json_object) throws Exception {
        JsonArray object_list = json_object.getJsonArray("objects");
        Map object_tracking_map = new HashMap();
        createInstances(object_tracking_map, object_list);
        assignFieldValues(object_tracking_map, object_list);
        return object_tracking_map.get("0");
    }
//

    private static void createInstances(Map object_tracking_map, JsonArray object_list) throws Exception {
        for (int i = 0; i < object_list.size(); i++) {
            JsonObject object_info = object_list.getJsonObject(i);
            Class object_class = Class.forName(object_info.getString("class"));
            Constructor constructor = object_class.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }
            Object object_instance = constructor.newInstance();
            //Make object
            object_tracking_map.put(object_info.getString("id"), object_instance);
        }
    }

    private static void assignFieldValues(Map object_tracking_map, JsonArray object_list) throws Exception {
    }

    public static void main(String[] args){  
    	try{  
	    	
	    	Socket s = new Socket("localhost",8192); 
	    	DataInputStream dis = new DataInputStream(s.getInputStream());  
	    	String  str = (String) dis.readUTF();  
	    	System.out.println("message = "+str);  
	    	  
	    	} catch (Exception e){System.out.println(e);}  
    	}  
}